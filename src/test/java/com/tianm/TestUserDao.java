package com.tianm;


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
@Transactional
@PropertySource("classpath:application.properties")
@Rollback(value = true)
public class TestUserDao {

}
