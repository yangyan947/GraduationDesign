package com.example.service;

import com.example.dao.BlogDao;
import com.example.dao.CommentDao;
import com.example.dao.UserDao;
import com.example.domain.Admin;
import com.example.domain.Blog;
import com.example.domain.Comment;
import com.example.domain.User;
import com.example.service.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.service.BlogService.PAGE_SIZE;

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
    //冻结，普通
    public static final String STATUS_FREEZE = "freeze";
    public static final String STATUS_ACTIVE = "normal";

    /**
     * 添加评论
     * @param comment 添加评论
     * @param blogId  对应微博
     * @param user  操作用户
     * @return
     */
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
            comment =  commentDao.save(comment);
            user.addComment(comment);
            blog.addComment(comment);
            blogDao.save(blog);
            userDao.saveAndFlush(user);
            message = new Message(true, "评论成功！");
        }
        return message;
    }

    /**
     * 删除评论
     * @param commentId 操作评论id
     * @param user  操作用户
     * @return
     */
    public Message deleteComment(Long commentId, User user) {
        Message message;
        Comment comment = commentDao.findOne(commentId);

        if (user == null) {
            message = new Message(false, "未登录");
        } else if (comment == null){
            message = new Message(false, "评论不存在");
        }
        else{
            User oUser = comment.getUser();
            Blog oBlog = comment.getBlog();
            user.getComments().remove(comment);
            oBlog.getComments().remove(comment);
            commentDao.delete(comment);
            userDao.save(oUser);
            blogDao.save(oBlog);
            message = new Message(true, "删除成功");
        }
        return message;
    }

    /**
     * 获得以微博的评论
     * @param blogId 微博id
     * @param user 操作用户
     * @return
     */
    public Message getBlogComment(Long blogId, User user) {
        Message message;
        Blog blog = blogDao.findOne(blogId);
        if (user == null) {
            message = new Message(false, "未登录");
        } else if (blog == null) {
            message = new Message(false, "微博不存在");
        }
        else{
            message = new Message(true, "评论查找成功", blog.getComments());
        }
        return message;
    }

    /**
     * 设置评论状态
     * @param commentId 评论id
     * @param admin 管理员
     * @param status 设置状态
     * @return
     */
    public Message setCommentStatus(Long commentId, Admin admin,String status) {
        Message message;
        Comment comment = commentDao.findOne(commentId);
        if (!status.equals(STATUS_FREEZE)  && !status.equals(STATUS_ACTIVE)) {
            message = new Message(false, "状态设置错误");
        }
        else if (admin == null) {
            message = new Message(false, "权限不足");
        } else if (comment == null) {
            message = new Message(false, "评论不存在");
        } else if (!comment.getStatus().equals(status)) {
            comment.setStatus(status);
            commentDao.save(comment);
            message = new Message(true, "设置状态"+comment.getStatusZn()+"成功", comment.getStatusZn());
        } else {
            message = new Message(false, "已经状态为" + comment.getStatusZn()+ "不需要再次设置");
        }
        return message;
    }

    public Page<Comment> getCommentPage(Integer index) {
        return commentDao.findAll(new PageRequest(index - 1, PAGE_SIZE, new Sort(Sort.Direction.DESC,"createTime")));
    }
    public Page<Comment> getCommentPageByStatus(Integer index,String status) {
        return commentDao.getByStatus(status,new PageRequest(index - 1, PAGE_SIZE, new Sort(Sort.Direction.DESC,"createTime")));
    }
}
