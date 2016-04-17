package com.example.controller;

import com.example.domain.Comment;
import com.example.domain.User;
import com.example.service.BlogService;
import com.example.service.CommentService;
import com.example.service.UserService;
import com.example.service.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * Created by SunYi on 2016/2/1/0001.
 */
@Controller
//控制类，控制页面跳转，数据传输
public class CommentController {
    //自动注入userService，用来处理业务
    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CommentService commentService;


    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    public String addComment(HttpSession session, Model model, @ModelAttribute(value = "comment") Comment comment, @RequestParam(value = "blogId") Long blogId) {

        Message message = commentService.addComment(comment, blogId, (User) session.getAttribute("user"));
        if (message.isSuccess()) {
            model.addAttribute("result", message.getReason());
        } else {

        }
        return "index";
    }

}
