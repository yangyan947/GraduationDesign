package com.tianm.service;

import com.tianm.domain.User;
import com.tianm.repository.UserRepository;
import com.tianm.service.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by SunYi on 2016/4/6/0006.
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userDao;

    public Message login(User user) {
        Message message = new Message();
        if (user == null || user.getEmail() == null) {
            message.setSuccess(false);
            message.setReason("输入为空。");
            return message;
        }
        User getUser = userDao.findByEmail(user.getEmail());
        if (getUser == null || !getUser.getPassword().equals(user.getPassword())) {
            message.setSuccess(false);
            message.setReason("用户名或者密码错误。");
            return message;
        }
        message.setReason("登陆成功！");
        message.setOthers(getUser);
        return message;
    }

    public Message register(User user) {
        Message message = new Message();
        if (user == null || user.getEmail() == null || user.getPassword() == null || user.getEmail() == null) {
            message.setSuccess(false);
            message.setReason("有未填写信息");
            return message;
        }
        User getUser = userDao.findByEmail(user.getEmail());
        if (getUser != null) {
            message.setSuccess(false);
            message.setReason("该用户已经存在");
            return message;
        }
        userDao.save(user);
        message.setReason("注册成功");
        return message;
    }

    public Message getById(Long id) {
        User user = userDao.findOne(id);
        Message message;
        if (user == null) {
            message = new Message(false, "木有该ID！");
        }
        else {
            user.setPassword("不给看");
            message = new Message(true, "查找成功！", user);
        }

        return message;
    }
}
