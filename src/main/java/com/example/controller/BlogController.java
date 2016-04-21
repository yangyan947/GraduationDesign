package com.example.controller;

import com.example.domain.Blog;
import com.example.domain.User;
import com.example.service.BlogService;
import com.example.service.UserService;
import com.example.service.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static com.example.controller.UserController.USER;

/**
 * Created by SunYi on 2016/2/1/0001.
 */
@Controller
//控制类，控制页面跳转，数据传输
public class BlogController {
    //自动注入userService，用来处理业务
    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    //发布微博
    @RequestMapping(value = "/publishBlog", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String publishBlog(HttpSession session, Model model, @ModelAttribute(value = "blog") Blog blog) {
        Message message = blogService.publishBlog(blog, (User) session.getAttribute(USER));
//        model.addAttribute("result", message.getReason());

        return message.toString();
    }

    //点赞
    @RequestMapping(value = "/pointBlog", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String pointBlog(HttpSession session, @ModelAttribute(value = "blogId") Long blogId) {
        Message message = blogService.pointBlog(blogId, (User) session.getAttribute(USER));
        if (message.isSuccess()) {
            update(session);
        }
        return message.toString();
    }

    //点赞
    @RequestMapping(value = "/unPointBlog", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String unPointBlog(HttpSession session, @ModelAttribute(value = "blogId") Long blogId) {
        Message message = blogService.unPointBlog(blogId, (User) session.getAttribute(USER));
        if (message.isSuccess()) {
            update(session);
        }
        return message.toString();
    }

    //删除
    @RequestMapping(value = "/deleteBlog", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String deleteBlog(HttpSession session, @ModelAttribute(value = "blogId") Long blogId) {
        Message message = blogService.deleteBlog(blogId, (User) session.getAttribute(USER));
        if (message.isSuccess()) {
            update(session);
        }
        return message.toString();
    }

    //微博列表页
    @RequestMapping(value = "/blogList", method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String blogList(HttpSession session, Model model, @RequestParam(value = "page", defaultValue = "1") Integer index) {
        Page<Blog> blogPage = blogService.getAllBlog(index);
        model.addAttribute("blogPage", blogPage);
        return "blogList";
    }

    //微博列表页
    @RequestMapping(value = "/attendUserBlogList", method = RequestMethod.GET,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String attendUserBlogList(HttpSession session, Model model, @RequestParam(value = "page", defaultValue = "1") Integer index) {
        Message message = blogService.getAttentionUsersBlog((User) session.getAttribute(USER), index);
        if (message.isSuccess()) {
            model.addAttribute("blogPage", message.getOthers());
        } else {
            model.addAttribute("result", message.getReason());
        }
        return "blogList";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(HttpSession session, Model model,
                         @RequestParam(value = "search", defaultValue = "all") String search,
                         @RequestParam(value = "page", defaultValue = "1") Integer index
    ) {
        if (session.getAttribute(USER) == null) {
            model.addAttribute("result", "未登录");
            return "pages/login";
        } else {
            Page<Blog> blogPage;
            if (search.equals("all")) {
                blogPage = blogService.getAllBlog(index);
            } else {
                blogPage = blogService.getSearch(index, search);
            }
            model.addAttribute("blogPage", blogPage);
            model.addAttribute("user", session.getAttribute(USER));
            return "pages/search";
        }
    }
    public  User update(HttpSession session) {
        User oldUser = (User) session.getAttribute(USER);
        session.removeAttribute(USER);
        User newUser = userService.update(oldUser);
        session.setAttribute(USER, newUser);
        return newUser;
    }

}
