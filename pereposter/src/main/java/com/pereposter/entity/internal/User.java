package com.pereposter.entity.internal;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "USER_PEREPOSTER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String name;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "USER_ID", nullable = false)
    private Set<UserSocialAccount> accounts;

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

    public Set<UserSocialAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<UserSocialAccount> accounts) {
        this.accounts = accounts;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
