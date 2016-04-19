package com.example.service;

import com.example.dao.AdminDao;
import com.example.domain.Admin;
import com.example.service.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by SunYi on 2016/4/19/0019.
 */
@Service
@Transactional
public class AdminService {
    @Autowired
    private AdminDao adminDao;

    //用户登陆逻辑
    public Message login(Admin admin) {
        Message message;
        if (admin == null) {
            return new Message(false, "操作错误，无信息传回");
        }
        Admin dbAdmin = adminDao.getByUsername(admin.getUsername());
        if (dbAdmin == null) {
            message = new Message(false, "该用户不存在");
        } else if (!dbAdmin.getPassword().equals(admin.getPassword())) {
            message = new Message(false, "密码错误");
        }
        else{
            admin = dbAdmin;
            message = new Message(true, "登陆成功", admin);

        }
        return message;
    }
}
