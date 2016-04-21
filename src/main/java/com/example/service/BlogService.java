package com.example.service;

import com.example.dao.BlogDao;
import com.example.dao.UserDao;
import com.example.domain.Admin;
import com.example.domain.Blog;
import com.example.domain.User;
import com.example.service.message.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    //普通，冻结，热门
    public static final String STATUS_NORMAL = "normal";
    public static final String STATUS_FREEZE = "freeze";
    public static final String STATUS_HOT = "hot";

    /**
     * 发布微博
     *
     * @param blog
     * @param user
     * @return
     */
    public Message publishBlog(Blog blog, User user) {
        Message message;
        if (user == null) {
            message = new Message(false, "未登录!");
        } else {
            blog.setUser(user);
            blog = blogDao.save(blog);
            user.getBlogs().add(blog);
            user = userDao.save(user);
            message = new Message(true, "发表成功!", blog);
        }
        return message;
    }

    /**
     * 删除微博
     *
     * @param blogId 删除微博Id
     * @param user   操作用户
     * @return
     */
    public Message deleteBlog(Long blogId, User user) {
        Message message;
        Blog blog = blogDao.findOne(blogId);
        if (blog == null) {
            message = new Message(true, "微博不存在");
        }
        else if (user != null && blog.getUser().getId() == user.getId()) {
            user = userDao.findOne(user.getId());
            user.getBlogs().remove(blog);
            userDao.save(user);
            blogDao.delete(blog);
            message = new Message(true, "删除成功");
        } else {
            message = new Message(false, "你不是该微博拥有者，不能删除");
        }
        return message;
    }

    /**
     * 点赞
     *
     * @param blogId 对应微博Id
     * @param user   点赞用户
     * @return
     */
    public Message pointBlog(Long blogId, User user) {
        Message message;
        Blog blog = blogDao.findOne(blogId);
        if (user == null) {
            message = new Message(false, "未登录!");
        } else if (blog == null) {
            message = new Message(false, "微博不存在!");
        } else if (user.isOwner(blogId)) {
            message = new Message(false, "无法点赞自己的微博");
        } else if (blog.isPoint(user.getId()) ){
            message = new Message(false, "已经点过赞了");
        }else{
            blog.getPointsUsers().add(user);
            blog = blogDao.save(blog);
            message = new Message(true, "点赞成功");
        }
        return message;
    }

    /**
     * 取消点赞
     *
     * @param blogId 对应微博Id
     * @param user   点赞用户
     * @return
     */
    public Message unPointBlog(Long blogId, User user) {
        Message message;
        Blog blog = blogDao.findOne(blogId);
        if (user == null) {
            message = new Message(false, "未登录!");
        } else if (blog == null) {
            message = new Message(false, "微博不存在!");
        } else if (!blog.isPoint(user.getId())) {
            message = new Message(false, "未点赞，无法取消点赞。");
        } else {
            user = userDao.getOne(user.getId());
            blog.getPointsUsers().remove(user);
            blog = blogDao.save(blog);
            message = new Message(true, "取消点赞成功");
        }
        return message;
    }

    /**
     * 获得所有关注微博
     *
     * @param user  操作用户
     * @param index 分页
     * @return message Page<Blog>
     */
    public Message getAttentionUsersBlog(User user, Integer index) {
        Message message;
        if (user == null) {
            message = new Message(false, "未登录!");
        } else {
            Pageable pageable = new PageRequest(index - 1, PAGE_SIZE);
            Page<Blog> blogPage = blogDao.getByUsers(user.getAttentionUsers(), pageable);
            if (blogPage != null && blogPage.getSize() == 0) {
                message = new Message(false, "获取结果为空");
            } else {
                message = new Message(true, "获取成功", blogPage);
            }
        }
        return message;
    }

    /**
     * 设置微博状态
     *
     * @param blogId 操作微博id
     * @param admin  管理员
     * @param status 设置微博状态
     * @return
     */
    public Message setBlogStatus(Long blogId, Admin admin, String status) {
        Message message;
        Blog blog = blogDao.findOne(blogId);
        if (!status.equals(STATUS_FREEZE) && !status.equals(STATUS_HOT) && !status.equals(STATUS_NORMAL)) {
            message = new Message(false, "状态设置错误");
        } else if (admin == null) {
            message = new Message(false, "权限不足");
        } else if (blog == null) {
            message = new Message(false, "微博不存在");
        } else if (!blog.getStatus().equals(status)) {
            blog.setStatus(status);
            blogDao.save(blog);
            message = new Message(true, "设置微博状态" + status + "成功", blog.getStatusZn());
        } else {
            message = new Message(false, "微博已经状态为" + blog.getStatusZn() + "，不需要再次设置");
        }
        return message;
    }

    /**
     * 获得所有blog
     *
     * @param index 页码
     * @return
     */
    public Page<Blog> getAllBlog(Integer index) {
        if (index <= 0) {
            index = 1;
        }
//        return blogDao.findAll(new PageRequest(index - 1, PAGE_SIZE));
        return blogDao.findAll(new PageRequest(index - 1, PAGE_SIZE, new Sort(Sort.Direction.DESC, "createTime")));
    }

    public Page<Blog> getSearch(Integer index, String search) {
        if (index <= 0) {
            index = 1;
        }
        search = "%" + search + "%";
        return blogDao.getByContextLike(search, new PageRequest(index - 1, PAGE_SIZE, new Sort(Sort.Direction.DESC, "createTime")));
    }

    /**
     * 获得所有blog
     *
     * @param index 页码
     * @return
     */
    public Page<Blog> getAllBlogNyStatus(Integer index, String status) {
        if (index <= 0) {
            index = 1;
        }
        return blogDao.getByStatus(status, new PageRequest(index - 1, PAGE_SIZE, new Sort(Sort.Direction.DESC, "createTime")));
    }
    /**
     * 获得所有blog
     *
     * @param index 页码
     * @return
     */
    public Page<Blog> getByStatusIsNot(Integer index, String status) {
        if (index <= 0) {
            index = 1;
        }
        return blogDao.getByStatusIsNot(status, new PageRequest(index - 1, PAGE_SIZE, new Sort(Sort.Direction.DESC, "createTime")));
    }
}
