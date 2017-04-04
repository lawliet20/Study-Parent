package com.wwj.service;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.wwj.dao.UserDao;
import com.wwj.model.User;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    UserDao customUserDao;
    JdbcUserDetailsManager k;
    private Map<String, User> userMap = null;
    protected final Log logger = LogFactory.getLog(getClass());
    protected final MessageSourceAccessor messages = SpringSecurityMessageSource
            .getAccessor();
    private String usersByUsernameQuery = "SELECT account,pwd,stat,salt,id,company_id,name,login_stat,login_date ,login_ip FROM USER_ACCOUNT WHERE ACCOUNT = ?";
    private String authoritiesByUsernameQuery = "SELECT NAME,POWER_CODE FROM VW_USER_POWER WHERE ACCOUNT_ID = ?";

    public UserDetailsServiceImpl() {
        userMap = new HashMap<>();
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException, DataAccessException {
        /*SecurityContextHolder.getContext() 
                .getAuthentication().getName();*/
        List<UserDetails> users = loadUsersByUsername(username);
        if (users.size() == 0) {
            logger.debug("Query returned no results for user '" + username + "'");
            throw new UsernameNotFoundException(messages.getMessage(
                    "JdbcDaoImpl.notFound", new Object[]{username},
                    "Username {0} not found"));
        }
        User user = (User) users.get(0);
        Set<GrantedAuthority> dbAuthsSet = new HashSet<>();

        dbAuthsSet.addAll(loadUserAuthorities(user.getId()));
        dbAuthsSet.add(new SimpleGrantedAuthority("ROLE_STATIC"));
        List<GrantedAuthority> dbAuths = new ArrayList<>(dbAuthsSet);

        if (dbAuths.size() == 0) {
            logger.debug("User '" + username
                    + "' has no authorities and will be treated as 'not found'");

            throw new UsernameNotFoundException(messages.getMessage(
                    "JdbcDaoImpl.noAuthority", new Object[]{username},
                    "User {0} has no GrantedAuthority"));
        }
        return createUserDetails(username, user, dbAuths);
        //return user;  
    }

    protected UserDetails createUserDetails(String username,
                                            UserInfo userFromUserQuery, List<GrantedAuthority> combinedAuthorities) {
        String returnUsername = userFromUserQuery.getUsername();
        UserInfo user = new UserInfo(returnUsername, userFromUserQuery.getPassword(), userFromUserQuery.isEnabled(), true, true, true,
                combinedAuthorities);
        user.setId(userFromUserQuery.getId());
        user.setCompanyId(userFromUserQuery.getCompanyId());
        user.setName(userFromUserQuery.getName());
        user.setLoginStat(userFromUserQuery.getLoginStat());
        user.setLoginDate(userFromUserQuery.getLoginDate());
        user.setLoginIP(userFromUserQuery.getLoginIP());
        user.setSalt(userFromUserQuery.getSalt());
        return user;
    }

    /**
     * Loads authorities by executing the SQL from
     * <tt>groupAuthoritiesByUsernameQuery</tt>.
     *
     * @return a list of GrantedAuthority objects for the user
     */
    protected List<GrantedAuthority> loadUserAuthorities(int userId) {
        try {
            return customUserDao.queryForList(this.authoritiesByUsernameQuery,
                    new Object[]{userId}, (rs, rowNum) -> {
                        String roleName = getRolePrefix() + rs.getString(2);
                        return new SimpleGrantedAuthority(roleName);
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getRolePrefix() {
        return "ROLE_";
    }

    /**
     * Loads authorities by executing the SQL from <tt>authoritiesByUsernameQuery</tt>.
     *
     * @return a list of GrantedAuthority objects for the user
     */
    protected List<UserDetails> loadUsersByUsername(String username) {
        try {

            return customUserDao.queryForList(this.usersByUsernameQuery, new Object[]{username},
                    (rs, rowNum) -> {
                        String username1 = rs.getString(1);
                        String password = rs.getString(2);
                        boolean enabled = rs.getBoolean(3);
                        UserInfo user = new UserInfo(username1, password, enabled, true, true, true,
                                AuthorityUtils.NO_AUTHORITIES);
                        user.setSalt(rs.getString(4));
                        user.setId(rs.getInt(5));
                        user.setCompanyId(rs.getInt(6));
                        user.setName(rs.getString(7));
                        user.setLoginStat(rs.getInt(8));
                        user.setLoginDate(rs.getLong(9));
                        user.setLoginIP(rs.getString(10));
                        return user;
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}  