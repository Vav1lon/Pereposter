package com.pereposter.social.vkontakte;

import com.pereposter.social.api.entity.Response;
import org.apache.cxf.helpers.IOUtils;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.InputStream;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/context-test.xml")
public abstract class AbstractTest {

    public Response readFileToString(String path) {

        Response result = null;

        ClassLoader classLoader = AbstractTest.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(path);

        try {
            result = new Response();
            result.setBody(IOUtils.toString(inputStream, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

}
