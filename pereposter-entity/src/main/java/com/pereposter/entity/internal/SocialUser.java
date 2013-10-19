package com.pereposter.entity.internal;

import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "SOCIAL_USER")
public class SocialUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "social_user_seq")
    @SequenceGenerator(name = "social_user_seq", sequenceName = "social_user_seq")
    @Column(name = "ID")
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(nullable = false, referencedColumnName = "ID")
    @ForeignKey(name = "SOCIAL_USER_SOCIAL_USER_ACCOUNT_FK")
    private List<SocialUserAccount> accounts;

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

    public List<SocialUserAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<SocialUserAccount> accounts) {
        this.accounts = accounts;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
