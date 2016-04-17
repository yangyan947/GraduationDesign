package com.example.service;

import com.example.dao.UserDao;
import com.example.domain.User;
import com.example.service.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by SunYi on 2016/2/1/0001.
 */
@Service
@Transactional
public class UserService {
    //自动注入一个userDao
    @Autowired
    private UserDao userDao;
//    @Autowired
//    private RoleDao roleDao;

    static private int PAGE_SIZE = 10;

    //用户注册逻辑
    public Message register(User user) {
        Message message;
        //判断用户是否存在
        if (!userDao.getByEmail(user.getEmail()).isPresent()) {
//            user.setRole(roleDao.getOne(2L));
            userDao.save(user);
            message = new Message(true, "注册成功");
        } else {
            message = new Message(false, "该用户名已被使用");
        }
        return message;
    }

    //用户登陆逻辑
    public Message login(User user) {
        final Message[] message = new Message[1];

        //通过用户名获取用户
        Optional<User> dbGetUser = userDao.getByEmail(user.getEmail());
        //若获取失败
        if (!dbGetUser.isPresent()) {
            message[0] = new Message(false, "该用户不存在");
            return message[0];
        }
        //获取成功后，将获取用户的密码和传入密码对比
        dbGetUser.ifPresent(user1 -> {
            if (!user1.getPassword().equals(user.getPassword())) {
                message[0] = new Message(false, "密码错误");
            } else {
                //若密码也相同则登陆成功
                //让传入用户的属性和数据库保持一致
                user.setId(user1.getId());
                user.setCreateTime(user1.getCreateTime());
                message[0] = new Message(true, "登陆成功", user1);
            }
        });
        return message[0];

    }

    //    用户信息修改
    public Message save(User user) {
        Message message;
        //通过用户名获取用户
        Optional<User> dbUser = userDao.getByEmail(user.getNickname());
        //若获取失败
        if (!dbUser.isPresent()) {
            message = new Message(false, "该用户不存在");
        } else {
            user = userDao.save(dbUser.get().update(user));
            message = new Message(true, "修改成功", user);
        }
        return message;
    }

    public Message attendUser(User user, Long attendId) {
        Message message;
        User attendUser = userDao.findOne(attendId);
        if (user == null) {
            message = new Message(false, "未登录");
        } else if (attendUser != null) {
            user.getAttentionUsers().add(attendUser);
            user = userDao.save(user);
            message = new Message(true, "关注成功", user);
        } else {
            message = new Message(false, "关注失败，关注的用户不存在");
        }
        return message;

    }

//    public User getOne(Long id) {
//        return userDao.findOne(id);
//    }
//    public Message GetUserOwnProject(User user) {
//        Message message ;
//        user.getOwnProjects()
//        return message;
//    }

//    public List<User> getAllUser() {
//        return userDao.findAll();
//    }
//
//    public List<User> getAllUser(int index) {
//        Pageable pageable = new PageRequest(index-1, PAGE_SIZE);
//        return  userDao.findAll(pageable).getContent();
//    }
}
