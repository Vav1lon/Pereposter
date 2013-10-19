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

        socialAccountVkontakteEnable1.setSocialUser(globalSocialUser1);
        socialAccountFaceBookEnabled1.setSocialUser(globalSocialUser1);

        socialAccountVkontakteEnable2.setSocialUser(globalSocialUser2);
        socialAccountFaceBookEnabled2.setSocialUser(globalSocialUser2);

        globalSocialUser1.setAccounts(Arrays.asList(socialAccountVkontakteEnable1, socialAccountFaceBookEnabled1));
        globalSocialUser2.setAccounts(Arrays.asList(socialAccountVkontakteEnable2, socialAccountFaceBookEnabled2));

        getSession().saveOrUpdate(globalSocialUser1);
        getSession().saveOrUpdate(globalSocialUser2);
    }

    @Test
    public void createUserAndSocialAccount() {

        List list = getSession().createSQLQuery("SELECT ur.* FROM SOCIAL_USER ur INNER JOIN SOCIAL_USER_ACCOUNT usa ON usa.USER_ID = ur.ID").list();

        Assert.assertNotSame(0, list.size());
    }


}
