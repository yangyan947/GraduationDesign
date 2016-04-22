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

    @RequestMapping(value = "")
    public RedirectView index() {
        return new RedirectView("/admin/blogList", true, false, true);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginGet(Model model, @RequestParam(value = "result", defaultValue = "") String result) {
        if (result != null && !result.equals("")) {
            model.addAttribute("result", result);
        }
        return "pages/admin/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public RedirectView loginPost(Model model,
                                  @ModelAttribute(value = ADMIN) Admin admin,
                                  HttpSession session) {
        Message result = adminService.login(admin);
        model.addAttribute("result", result.getReason());
        if (result.isSuccess()) {
            //添加到session中，session是一次会话，在本次会话中都可以取到session中的值
            //若是session中有用户存在则会覆盖原来的user，当session中的user存在时判定用户存在
            session.setAttribute(ADMIN, result.getOthers());
        }
        return new RedirectView("/admin/blogList", true, false, true);
    }

    @RequestMapping(value = "/loginOut", method = RequestMethod.GET)
    public RedirectView AdminLoginOut(HttpSession session) {
        //从session中删除user属性，用户退出登录
        session.removeAttribute(ADMIN);
        return new RedirectView("/admin", true, false, true);
    }

    @RequestMapping(value = "/userList", method = RequestMethod.GET)
    public String userList(HttpSession session, Model model,
                           @RequestParam(value = "page", defaultValue = "1") Integer index,
                           @RequestParam(value = "status", defaultValue = "all") String status) {
        if (session.getAttribute(ADMIN) == null) {
            model.addAttribute("result", "权限不足");
            return "pages/admin/login";
        } else {
            Page<User> userPage;
            if (status.equals("all")) {
                userPage = userService.getPageUser(index);
            } else {
                userPage = userService.getPageUserByStatus(index, status);
            }
            model.addAttribute("userPage", userPage);
            return "pages/admin/userList";
        }
    }

    @RequestMapping(value = "/commentList", method = RequestMethod.GET)
    public String commentList(HttpSession session, Model model,
//                              @ModelAttribute(value = "commentPage") Page<Comment> commentPage2,
                              @RequestParam(value = "page", defaultValue = "1") Integer index,
                              @RequestParam(value = "status", defaultValue = "all") String status) {

        if (session.getAttribute(ADMIN) == null) {
            model.addAttribute("result", "权限不足");
            return "pages/admin/login";
        } else {
            Page<Comment> commentPage;
            if (status.equals("all")) {
                commentPage = commentService.getCommentPage(index);
            } else {
                commentPage = commentService.getCommentPageByStatus(index, status);
            }
            model.addAttribute("commentPage", commentPage);
            return "pages/admin/commentList";
        }
    }

    @RequestMapping(value = "/blogList", method = RequestMethod.GET)
    public String blogList(HttpSession session, Model model,
                           @RequestParam(value = "status", defaultValue = "all") String status,
                           @RequestParam(value = "page", defaultValue = "1") Integer index
//                           ,@ModelAttribute(value = "blogPage") Page<Blog> blogPages
    ) {
        if (session.getAttribute(ADMIN) == null) {
            model.addAttribute("result", "权限不足");
            return "pages/admin/login";
        } else {
            Page<Blog> blogPage;
            if (status.equals("all")) {
                blogPage = blogService.getAllBlog(index);
            } else {
                blogPage = blogService.getAllBlogNyStatus(index, status);
            }
            model.addAttribute("blogPage", blogPage);
            return "pages/admin/blogList";
        }
    }

    @RequestMapping(value = "/setBlog/{status}/{id}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String setBlog(HttpSession session, Model model, @PathVariable(value = "status") String status, @PathVariable(value = "id") Long id) {
        Message message = blogService.setBlogStatus(id,
                (Admin) session.getAttribute(ADMIN),
                status);
        return message.toString();
    }

    @RequestMapping(value = "/setBlogStatus", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String setBlogStatus(HttpSession session, Model model, @RequestParam(value = "status") String status, @RequestParam(value = "id") Long id) {
        Message message = blogService.setBlogStatus(id,
                (Admin) session.getAttribute(ADMIN),
                status);
        return message.toString();
    }

    @RequestMapping(value = "/setUserStatus", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String setUserStatus(HttpSession session, Model model, @RequestParam(value = "status") String status, @RequestParam(value = "id") Long id) {
        Message message = userService.setUserStatus(id,
                (Admin) session.getAttribute(ADMIN),
                status);
        return message.toString();
    }

    @RequestMapping(value = "/setCommentStatus", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String setCommentStatus(HttpSession session, Model model, @RequestParam(value = "status") String status, @RequestParam(value = "id") Long id) {
        Message message = commentService.setCommentStatus(id,
                (Admin) session.getAttribute(ADMIN),
                status);
        return message.toString();
    }
}
