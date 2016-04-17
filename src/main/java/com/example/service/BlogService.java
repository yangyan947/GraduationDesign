package com.example.service;

import com.example.dao.BlogDao;
import com.example.dao.UserDao;
import com.example.domain.Blog;
import com.example.domain.User;
import com.example.service.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by SunYi on 2016/4/15/0015.
 */
@Service
@Transactional
public class BlogService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private BlogDao blogDao;
    public static final int PAGE_SIZE = 10;


    public static final String NORMAL = "normal";
    public static final String FREEZE = "freeze";

    public Message publishBlog(Blog blog, Long userId) {
        Message message = new Message(true, "发表成功!");
        User user = userDao.findOne(userId);
        user.addBlog(blog);
        blogDao.save(blog);
        userDao.save(user);
        return message;
    }

    public Message deleteBlog(Blog blog, Long userId) {
        Message message;
        if (blog.getUser().getId() == userId) {
            blogDao.delete(blog);
            message = new Message(true, "删除成功");
        } else {
            message = new Message(false, "你不是该微博拥有者，不能删除");
        }
        return message;
    }

    public Message pointBlog(Blog blog, User user) {
        Message message;
        if (blog.getPointsUsers().contains(user)) {
            message = new Message(false, "已经点过赞了");
        } else {
            blog.getPointsUsers().add(user);
            blog = blogDao.save(blog);
            message = new Message(true, "点赞成功");
        }
        return message;
    }

    public Message freezeBlog(Blog blog) {
        Message message;
        if (!blog.getStatus().equals(FREEZE)) {
            blog.setStatus(FREEZE);
            blogDao.save(blog);
            message = new Message(true, "冻结成功");
        } else {
            message = new Message(false, "已经冻结，不能再次冻结");
        }

        return message;
    }

    public Message recoveryBlog(Blog blog) {
        Message message;
        if (!blog.getStatus().equals(NORMAL)) {
            blog.setStatus(NORMAL);
            blogDao.save(blog);
            message = new Message(true, "恢复成功");
        } else {
            message = new Message(false, "已经为正常状态");
        }
        return message;
    }

    public Message getAttentionUsersBlog(User user, Integer index) {
        Message message;
        Pageable pageable = new PageRequest(index - 1, PAGE_SIZE);
        Page<Blog> blogPage = blogDao.getByUsers(user.getAttentionUsers(),pageable);
        if (blogPage != null && blogPage.getSize() == 0) {
            message = new Message(false, "获取结果为空");
        } else {
            message = new Message(true, "获取成功", blogPage);
        }
        return message;
    }
}
