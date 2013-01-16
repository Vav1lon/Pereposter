package com.pereposter.entity.internal;

import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "USER_PEREPOSTER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @ForeignKey(name = "USER_PEREPOSTER_USER_SOCIAL_ACCOUNT", inverseName = "USER_SOCIAL_ACCOUNT_USER_PEREPOSTER")
    private List<UserSocialAccount> accounts;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserSocialAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<UserSocialAccount> accounts) {
        this.accounts = accounts;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
