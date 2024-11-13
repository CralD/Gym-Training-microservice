package com.epam.gymappHibernate;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableEncryptableProperties
public class GymAppHibernateApplicationTests {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Test
    void contextLoads() {
    }

}
