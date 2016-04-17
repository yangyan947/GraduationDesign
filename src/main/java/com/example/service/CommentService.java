package com.example.service;

import com.example.dao.BlogDao;
import com.example.dao.CommentDao;
import com.example.dao.UserDao;
import com.example.domain.Blog;
import com.example.domain.Comment;
import com.example.domain.User;
import com.example.service.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by SunYi on 2016/4/15/0015.
 */
@Service
@Transactional
public class CommentService {
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private BlogDao blogDao;
    @Autowired
    private UserDao userDao;

    public Message addComment(Comment comment, Long blogId, User user) {
        Message message;

        Blog blog = blogDao.findOne(blogId);
        if (user == null) {
            message = new Message(false, "未登录");
        } else if (blog == null) {
            message = new Message(false, "该微博不存在");
        } else {
            comment.setUser(user);
            comment.setBlog(blog);
//        user.addComment(comment);
//        blog.addComment(comment);
            commentDao.save(comment);
            message = new Message(true, "评论成功！");
        }
        return message;
    }
}
