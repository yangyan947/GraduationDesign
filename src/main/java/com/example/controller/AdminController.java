package com.example.controller;

import com.example.domain.Admin;
import com.example.domain.Blog;
import com.example.domain.Comment;
import com.example.domain.User;
import com.example.service.AdminService;
import com.example.service.BlogService;
import com.example.service.CommentService;
import com.example.service.UserService;
import com.example.service.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;

/**
 * Created by SunYi on 2016/4/19/0019.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private CommentService commentService;
    public static final String ADMIN = "admin";

    @RequestMapping(value = "/Login", method = RequestMethod.GET)
    public String loginGet(Model model, @RequestParam(value = "result", defaultValue = "") String result) {
        if (result != null && !result.equals("")) {
            model.addAttribute("result", result);
        }
        return "pages/login";
    }

    @RequestMapping(value = "/Login", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String loginPost(Model model,
                            @ModelAttribute(value = ADMIN) Admin admin,
                            HttpSession session) {
        Message result = adminService.login(admin);
        model.addAttribute("result", result.getReason());
        if (result.isSuccess()) {
            //添加到session中，session是一次会话，在本次会话中都可以取到session中的值
            //若是session中有用户存在则会覆盖原来的user，当session中的user存在时判定用户存在
            session.setAttribute(ADMIN, result.getOthers());
        }
        return result.toString();

    }

    @RequestMapping(value = "/LoginOut", method = RequestMethod.GET)
    public RedirectView AdminLoginOut(HttpSession session) {
        //从session中删除user属性，用户退出登录
        session.removeAttribute(ADMIN);
        return new RedirectView("/index", true, false, true);
    }

    @RequestMapping(value = "/userList", method = RequestMethod.GET)
    public String userList(HttpSession session, Model model,
                           @RequestParam(value = "page", defaultValue = "1") Integer index) {
        if (session.getAttribute(ADMIN) == null) {
            model.addAttribute("result", "权限不足");
            return "index";
        } else {
            Page<User> userPage = userService.getPageUser(index);
            model.addAttribute("userPage", userPage);
            return "userList";
        }
    }

    @RequestMapping(value = "/commentList", method = RequestMethod.GET)
    public String commentList(HttpSession session, Model model,
                              @RequestParam(value = "page", defaultValue = "1") Integer index) {
        if (session.getAttribute(ADMIN) == null) {
            model.addAttribute("result", "权限不足");
            return "index";
        } else {
            Page<Comment> commentPage = commentService.getCommentPage(index);
            model.addAttribute("commentPage", commentPage);
            return "commentList";
        }
    }

    @RequestMapping(value = "/blogList", method = RequestMethod.GET)
    public String blogList(HttpSession session, Model model,
                           @RequestParam(value = "page", defaultValue = "1") Integer index) {
        if (session.getAttribute(ADMIN) == null) {
            model.addAttribute("result", "权限不足");
            return "index";
        } else {
            Page<Blog> blogPage = blogService.getAllBlog(index);
            model.addAttribute("blogList", blogPage);
            return "blogList";
        }
    }

    @RequestMapping(value = "/setBlogFreeze", method = RequestMethod.POST)
    public String setBlogStatus(HttpSession session, Model model, @RequestParam(value = "blogId") Long blogId) {
        Message message = blogService.setBlogStatus(blogId,
                (Admin) session.getAttribute(ADMIN),
                BlogService.STATUS_FREEZE);
        return message.toString();
    }
}
