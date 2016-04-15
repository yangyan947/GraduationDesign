package com.tianm;

import com.tianm.domain.User;
import com.tianm.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by SunYi on 2016/3/25/0025.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TianmApplication.class)
@Transactional
@PropertySource("classpath:application.properties")
@Rollback(value = true)
public class TestUserDao {
    @Autowired UserRepository userRepository;
    @Test
    public void testAddUser(){
        User user = new User();
        user.setEmail("123");
        user.setName("123");
        user.setPassword("123");
//        user.setId(1L);
        userRepository.save(user);
        User user1 = userRepository.findById(1L);
        System.out.println(user1.getName());
        Assert.assertEquals(user1.getName(), user.getName());
    }
    @Test
    public void testGetUser(){
        User user = userRepository.getOne(1L);
        System.out.println(user.getName());
        Assert.assertEquals(user.getName(), "111");
    }
}
