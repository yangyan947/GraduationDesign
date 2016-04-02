package com.tianm.controller;

import com.tianm.domain.User;
import com.tianm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by SunYi on 2016/3/24/0024.
 */
@RestController
//@RequestMapping("/test")
public class UserController {


    @RequestMapping(value = "/user/{email}", method = RequestMethod.GET)
    public User getByEmail(@PathVariable(value = "email") String email) {

        return userDao.findByEmail(email);
    }

    @Autowired
    private UserRepository userDao;
}
