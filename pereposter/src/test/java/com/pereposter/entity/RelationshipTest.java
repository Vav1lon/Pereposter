package com.pereposter.entity;

import com.pereposter.AbstractTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class RelationshipTest extends AbstractTest {

    @Before
    public void setUp() {
        super.setUp();

        socialAccountVkontakteEnable1.setUser(globalUser1);
        socialAccountFaceBookEnabled1.setUser(globalUser1);

        socialAccountVkontakteEnable2.setUser(globalUser2);
        socialAccountFaceBookEnabled2.setUser(globalUser2);

        globalUser1.setAccounts(Arrays.asList(socialAccountVkontakteEnable1, socialAccountFaceBookEnabled1));
        globalUser2.setAccounts(Arrays.asList(socialAccountVkontakteEnable2, socialAccountFaceBookEnabled2));

        getSession().saveOrUpdate(globalUser1);
        getSession().saveOrUpdate(globalUser2);
    }

    @Test
    public void createUserAndSocialAccount() {

        //test for check use database tables, problem in pereposter-web

        List list = getSession().createSQLQuery("SELECT ur.* FROM USER_PEREPOSTER ur INNER JOIN USER_SOCIAL_ACCOUNT usa ON usa.USER_ID = ur.ID").list();

        Assert.assertNotSame(0, list.size());
    }


}
