package com.pereposter.entity.internal;

import com.pereposter.utils.usertype.JodaTimeTypes;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
@Table(name = "SOCIAL_USER_ACCOUNT")
public class SocialUserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "social_user_account_seq")
    @SequenceGenerator(name = "social_user_account_seq", sequenceName = "social_user_account_seq")
    @Column(name = "ID")
    private Long id;

    @Column(name = "USERNAME", length = 256, nullable = false, unique = true)
    private String username;

    @Column(name = "PASSWORD", length = 512, nullable = false)
    private String password;

    @Column(name = "SOCIAL_NETWORK", nullable = false)
    @Type(type = "com.pereposter.utils.usertype.GenericEnumUserType",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "enumClass",
                            value = "com.pereposter.entity.internal.SocialNetworkEnum"),
                    @org.hibernate.annotations.Parameter(
                            name = "identifierMethod",
                            value = "getId"),
                    @org.hibernate.annotations.Parameter(
                            name = "valueOfMethod",
                            value = "fromInt")
            })
    private SocialNetworkEnum socialNetwork;

    @Column(name = "ENABLED", nullable = false)
    private boolean enabled;

    @Column(name = "LAST_POST_ID", length = 256)
    private String lastPostId;

    @Column(name = "CREATE_DATE_LAST_POST")
    @Type(type = JodaTimeTypes.DATE_TIME)
    private DateTime createDateLastPost;

    @Column(name = "SOCIAL_USER_ID", length = 128)
    private String socialUserId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "USER_ID")
    @ForeignKey(name = "USER_SOCIAL_ACCOUNT_SOCIAL_USER_FK")
    private SocialUser socialUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SocialNetworkEnum getSocialNetwork() {
        return socialNetwork;
    }

    public void setSocialNetwork(SocialNetworkEnum socialNetwork) {
        this.socialNetwork = socialNetwork;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getLastPostId() {
        return lastPostId;
    }

    public void setLastPostId(String lastPostId) {
        this.lastPostId = lastPostId;
    }

    public DateTime getCreateDateLastPost() {
        return createDateLastPost;
    }

    public void setCreateDateLastPost(DateTime createDateLastPost) {
        this.createDateLastPost = createDateLastPost;
    }

    public String getSocialUserId() {
        return socialUserId;
    }

    public void setSocialUserId(String socialUserId) {
        this.socialUserId = socialUserId;
    }

    public SocialUser getSocialUser() {
        return socialUser;
    }

    public void setSocialUser(SocialUser socialUser) {
        this.socialUser = socialUser;
    }
}
