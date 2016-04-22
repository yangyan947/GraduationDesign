package com.example.controller;

import com.example.domain.Comment;
import com.example.domain.User;
import com.example.service.CommentService;
import com.example.service.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CommentController {
    //自动注入userService，用来处理业务
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private BlogService blogService;

    @Autowired
    private CommentService commentService;


    @RequestMapping(value = "/addComment", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addComment(HttpSession session, Model model, @ModelAttribute(value = "comment") Comment comment, @RequestParam(value = "blogId") Long blogId) {
        Message message = commentService.addComment(comment, blogId, (User) session.getAttribute(USER));
        return message.toString();
    }
    @RequestMapping(value = "/deleteComment", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String deleteComment(HttpSession session, Model model,  @RequestParam(value = "commentId") Long commentId) {
        Message message = commentService.deleteComment(commentId, (User) session.getAttribute(USER));
        return message.toString();
    }

}
