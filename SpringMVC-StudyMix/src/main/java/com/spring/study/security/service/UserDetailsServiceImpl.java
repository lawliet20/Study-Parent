package com.spring.study.security.service;

import com.spring.study.dao.UserDao;
import com.spring.study.model.User;
import com.spring.study.model.pageModel.UserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * *
 *
 * @author tzz
 * @功能描述
 * @date 2016/5/3
 * 修改人    修改时间   修改说明
 * **
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    UserDao customUserDao;

    private Map<String, UserInfo> userMap = null;
    protected final Log logger = LogFactory.getLog(getClass());
    protected final MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    public UserDetailsServiceImpl() {
        userMap = new HashMap<>();
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException, DataAccessException {

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        list.add(grantedAuthority);
        return new org.springframework.security.core.userdetails.User("wwj","123",list);
        /*SecurityContextHolder.getContext()
                .getAuthentication().getName();*/
        /*List<User> users = loadUsersByUsername(username);
        if (users.size() == 0) {
            logger.debug("Query returned no results for user '" + username + "'");
            throw new UsernameNotFoundException(messages.getMessage(
                    "JdbcDaoImpl.notFound", new Object[]{username},
                    "Username {0} not found"));
        }
        User user = (User) users.get(0);
        Set<GrantedAuthority> dbAuthsSet = new HashSet<>();

        dbAuthsSet.addAll(loadUserAuthorities(user.getUserId()));
        dbAuthsSet.add(new SimpleGrantedAuthority("ROLE_STATIC"));
        dbAuthsSet.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        List<GrantedAuthority> dbAuths = new ArrayList<>(dbAuthsSet);

        if (dbAuths.size() == 0) {
            logger.debug("User '" + username
                    + "' has no authorities and will be treated as 'not found'");

            throw new UsernameNotFoundException(messages.getMessage(
                    "JdbcDaoImpl.noAuthority", new Object[]{username},
                    "User {0} has no GrantedAuthority"));
        }
        return createUserDetails(username, user, dbAuths);*/
    }

    protected UserDetails createUserDetails(String username, User userFromUserQuery, List<GrantedAuthority> combinedAuthorities) {
        String returnUsername = userFromUserQuery.getUserName();
        UserInfo user = new UserInfo(returnUsername, userFromUserQuery.getPassword(),
                true, true, true, true, combinedAuthorities);
        user.setUserId(userFromUserQuery.getUserId());
        user.setBirthday(userFromUserQuery.getBirthday());
        user.setUserName(userFromUserQuery.getUserName());
        user.setUserRoles(userFromUserQuery.getUserRoles());
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
            List<GrantedAuthority> list = new ArrayList<>();
            list.add(new SimpleGrantedAuthority(getRolePrefix() + "USER"));
            return list;
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
    protected List<User> loadUsersByUsername(String username) {
        try {
            List<User> list = customUserDao.queryUserByName(username);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
