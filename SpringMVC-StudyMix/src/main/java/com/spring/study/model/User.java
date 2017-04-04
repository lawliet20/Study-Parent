package com.spring.study.model;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * User entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "user", catalog = "")
public class User implements java.io.Serializable {

    // Fields

    private Integer userId;
    private String userName;
    private Date birthday;
    private String password;
    private Set<UserRole> userRoles = new HashSet<UserRole>(0);

    // Constructors

    /**
     * default constructor
     */
    public User() {
    }

    /**
     * minimal constructor
     */
    public User(Integer userId) {
        this.userId = userId;
    }

    /**
     * full constructor
     */
    public User(Integer userId, String userName, Date birthday,
                String password, Set<UserRole> userRoles) {
        this.userId = userId;
        this.userName = userName;
        this.birthday = birthday;
        this.password = password;
        this.userRoles = userRoles;
    }

    // Property accessors
    @Id
    @Column(name = "user_id", unique = true, nullable = false)
    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Column(name = "user_name", length = 32)
    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "birthday", length = 10)
    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Column(name = "password", length = 32)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    public Set<UserRole> getUserRoles() {
        return this.userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

}