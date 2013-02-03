package com.pereposter.entity;

import com.pereposter.AbstractTest;
import com.pereposter.entity.internal.UserSocialAccount;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class RelationshipTest extends AbstractTest {

    @Before
    public void setUp() {
        super.setUp();

        globalUser1.setAccounts(new HashSet<UserSocialAccount>(Arrays.asList(socialAccountVkontakteEnable1, socialAccountFaceBookEnabled1)));
        globalUser2.setAccounts(new HashSet<UserSocialAccount>(Arrays.asList(socialAccountVkontakteEnable2, socialAccountFaceBookEnabled2)));

        getSession().saveOrUpdate(globalUser1);
        getSession().saveOrUpdate(globalUser2);
    }

    @Test
    public void createUserAndSocialAccount() {

        //test for check use database tables, problem in pereposter-web

        List list = getSession().createSQLQuery("SELECT * FROM USER_PEREPOSTER_USER_SOCIAL_ACCOUNT").list();

        Assert.assertNotSame(0, list.size());
    }


}
