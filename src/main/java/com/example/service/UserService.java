package com.example.service;

import com.example.dao.UserDao;
import com.example.domain.Admin;
import com.example.domain.User;
import com.example.service.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;
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
    //冻结，普通，危险，热门
    public static final String STATUS_FREEZE = "freeze";
    public static final String STATUS_ACTIVE = "normal";
    public static final String STATUS_DANGER = "danger";
    public static final String STATUS_HOT = "hot";

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
                if (user1.getStatus().equals(STATUS_FREEZE)) {
                    message[0] = new Message(false, "用户已经被冻结请联系客服");

                } else {
                    user.setId(user1.getId());
                    user.setCreateTime(user1.getCreateTime());
                    message[0] = new Message(true, "登陆成功", user1);
                }

            }
        });
        return message[0];

    }

    //    用户信息修改
    public Message changeUser(User user, User loginUser) {
        Message message;
        //通过用户名获取用户
        Optional<User> dbUser = userDao.getByEmail(user.getNickname());
        //若获取失败
        if (loginUser == null) {
            message = new Message(false, "未登录");
        } else if (!dbUser.isPresent()) {
            message = new Message(false, "该用户不存在");
        } else {
            user = userDao.save(dbUser.get().update(user));
            message = new Message(true, "修改成功", user);
        }
        return message;
    }

    /**
     * 关注用户
     *
     * @param user     操作用户
     * @param attendId 被关于用户id
     * @return
     */
    public Message attendUser(User user, Long attendId) {
        Message message;
        User attendUser = userDao.findOne(attendId);
        if (user == null) {
            message = new Message(false, "未登录");
        } else if (attendUser != null) {
            user.getAttentionUsers().add(attendUser);
            attendUser.getFollowUsers().add(user);
            user = userDao.save(user);
            attendUser = userDao.save(attendUser);
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

    /**
     * 获得用户列表
     *
     * @return
     */
    public List<User> getAllUser() {
        return userDao.findAll();
    }

    /**
     * 获得用户列表分页
     *
     * @param index
     * @return
     */
    public Page<User> getPageUser(int index) {
        return userDao.findAll(new PageRequest(index - 1, PAGE_SIZE, new Sort(Sort.Direction.DESC, "createTime")));
    }

    public Page<User> getPageUserByStatus(int index, String status) {
        return userDao.getByStatus(status, new PageRequest(index - 1, PAGE_SIZE, new Sort(Sort.Direction.DESC, "createTime")));
    }

    /**
     * 设置用户状态
     *
     * @param userId 操作用户ID
     * @param admin  管理员
     * @param status 设置状态
     * @return
     */
    public Message setUserStatus(Long userId, Admin admin, String status) {
        Message message;
        User user = userDao.findOne(userId);
        if (!status.equals(STATUS_DANGER) && !status.equals(STATUS_ACTIVE) && !status.equals(STATUS_FREEZE) && !status.equals(STATUS_HOT)) {
            message = new Message(false, "状态设置错误");
        } else if (admin == null) {
            message = new Message(false, "权限不足");
        } else if (user == null) {
            message = new Message(false, "用户不存在");
        } else if (!user.getStatus().equals(status)) {
            user.setStatus(status);
            userDao.save(user);
            message = new Message(true, "设置用户状态" + user.getStatusZn() + "成功", user.getStatusZn());
        } else {
            message = new Message(false, "已经为" + user.getStatusZn() + "，不能再次设置");
        }
        return message;
    }

    public User update(User user) {
        return userDao.getOne(user.getId());
    }

}
