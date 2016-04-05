package com.tianm.controller;

import com.tianm.domain.User;
import com.tianm.repository.UserRepository;
import com.tianm.service.UserService;
import com.tianm.service.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Created by SunYi on 2016/3/24/0024.
 */
@RestController
@RequestMapping("/api/")
public class UserController {


    @RequestMapping(value = "user/{id}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public Message getByEmail(@PathVariable(value = "id") Long id) {
        return userService.getById(id);
    }

    @RequestMapping(value = "login", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Message login(@RequestParam(value = "email") String email,
                         @RequestParam(value = "password") String password,
                         HttpSession session) {
        User user = new User(email, "", password);
        Message message = userService.login(user);
        session.setAttribute("user", message.getOthers());
        return message;
    }

    @RequestMapping(value = "user", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public Message register(@RequestParam(value = "email") String email,
                            @RequestParam(value = "name") String name,
                            @RequestParam(value = "password") String password) {
        User user = new User(email, name, password);
        Message message = userService.register(user);
        return message;
    }

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userDao;

}
