package com.pereposter.entity.internal;

import org.hibernate.annotations.ForeignKey;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "SITE_ROLE")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "site_role_seq")
    @SequenceGenerator(name = "site_role_seq", sequenceName = "site_role_seq")
    @Column(name = "ID")
    private Long id;

    @Column(name = "AUTHORITY", length = 128, nullable = false, unique = true)
    private String authority;

    @ManyToMany
    @JoinTable(name = "USER_ROLE", joinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")}
            , inverseJoinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")})
    @ForeignKey(name = "ROLE_USER_FK")
    private List<User> users;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
