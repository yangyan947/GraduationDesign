package com.example.controller;

import com.baidu.ueditor.ActionEnter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Sunxiupeng on 2015年3月14日
 */
@Controller
@RequestMapping(value = "/ueditor")
public class UEditorController {

    @RequestMapping(value = "/dispatch",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public void config(HttpServletRequest request, HttpServletResponse response, String action) {
        String rootPath = request.getSession().getServletContext().getRealPath("/");

        try {
            String exec = new ActionEnter(request, rootPath).exec();
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}