package com.spring.study.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Tmenu entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "tmenu", catalog = "sy")
public class Tmenu implements java.io.Serializable {

    // Fields

    private String id;
    private Tmenu tmenu;
    private String iconcls;
    private String text;
    private String seq;
    private String url;
    private Set<Tmenu> tmenus = new HashSet<Tmenu>(0);

    // Constructors

    /**
     * default constructor
     */
    public Tmenu() {
    }

    /**
     * minimal constructor
     */
    public Tmenu(String id, String text) {
        this.id = id;
        this.text = text;
    }

    /**
     * full constructor
     */
    public Tmenu(String id, Tmenu tmenu, String iconcls, String text,
                 String seq, String url, Set<Tmenu> tmenus) {
        this.id = id;
        this.tmenu = tmenu;
        this.iconcls = iconcls;
        this.text = text;
        this.seq = seq;
        this.url = url;
        this.tmenus = tmenus;
    }

    // Property accessors
    @Id
    @Column(name = "id", unique = true, nullable = false)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PID")
    public Tmenu getTmenu() {
        return this.tmenu;
    }

    public void setTmenu(Tmenu tmenu) {
        this.tmenu = tmenu;
    }

    @Column(name = "ICONCLS")
    public String getIconcls() {
        return this.iconcls;
    }

    public void setIconcls(String iconcls) {
        this.iconcls = iconcls;
    }

    @Column(name = "text", nullable = false)
    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Column(name = "SEQ")
    public String getSeq() {
        return this.seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    @Column(name = "URL")
    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tmenu")
    public Set<Tmenu> getTmenus() {
        return this.tmenus;
    }

    public void setTmenus(Set<Tmenu> tmenus) {
        this.tmenus = tmenus;
    }

}