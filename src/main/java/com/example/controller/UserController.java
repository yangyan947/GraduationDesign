package com.example.controller;

import com.example.domain.User;
import com.example.service.UserService;
import com.example.service.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by SunYi on 2016/2/1/0001.
 */
@Controller
//控制类，控制页面跳转，数据传输
public class UserController {
    //自动注入userService，用来处理业务
    @Autowired
    private UserService userService;

    //跳转链接，跳转到主页xx
    @RequestMapping("/")
    public String index(Model model,
                        HttpServletResponse response) {
        //对应到templates文件夹下面的index
        return "index";
    }

    //真正主页，用户在访问 XXXXX/index就会跳转该方法，
    // 这个XXXXX是你的域名，自己电脑上的话一般都是127.0.0.1:8080或者是localhost：8080
    // 8080是端口号，端口号根据tomcat设置而改变，默认值是8080
    @RequestMapping("/index")
    public String home(Model model, @RequestParam(value = "result", defaultValue = "欢迎！") String result) {
        //对应到templates文件夹下面的index
        if (result != null) {
            model.addAttribute("result", result);
        }
        return "index";
    }

    //进入注册页面，使用Get请求，REST风格的URL能更有雅的处理问题
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerGet() {
        return "pages/register";
    }

    //注册用户，使用POST，传输数据
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public RedirectView registerPost(Model model,
                                     //这里和模板中的th:object="${user}"对应起来
                                     @ModelAttribute(value = "user") User user,
                                     HttpServletResponse response) {
        //使用userService处理业务
        Message result = userService.register(user);
        //将结果放入model中，在模板中可以取到model中的值
        model.addAttribute("result", result.getReason());
        return new RedirectView("/index", true, false, true);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginGet(Model model, @RequestParam(value = "result", defaultValue = "") String result) {
        if (result != null || result.equals("")) {
            model.addAttribute("result", result);
        }
        return "pages/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public RedirectView loginPost(Model model,
                                  @ModelAttribute(value = "user") User user,
                                  HttpSession session) {

        Message result = userService.login(user);

        model.addAttribute("result", result.getReason());
        if (result.isSuccess()) {
            //添加到session中，session是一次会话，在本次会话中都可以取到session中的值
            //若是session中有用户存在则会覆盖原来的user，当session中的user存在时判定用户存在
            session.setAttribute("user", result.getOthers());
            return new RedirectView("/index", true, false, true);
        } else {
            return new RedirectView("/login", true, false, true);
        }

    }

    @RequestMapping(value = "/loginOut", method = RequestMethod.GET)
    public RedirectView loginOut(HttpSession session) {
        //从session中删除user属性，用户退出登录
        session.removeAttribute("user");
        return new RedirectView("/index", true, false, true);
    }

    @RequestMapping(value = "/userCenter", method = RequestMethod.GET)
    public String userCenter(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("result", "用户未登录!");
            return "index";
        } else {
            model.addAttribute("user", user);
            return "userCenter";
        }
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public RedirectView user(Model model,
                             @ModelAttribute(value = "user") User user,
                             @RequestParam(value = "sex") String sex,
                             HttpSession session) {
        if (sex.equals("male")) {
            user.setSex(1);
        }
        Message result = userService.save(user);

        model.addAttribute("result", result.getReason());
        if (result.isSuccess()) {
            //添加到session中，session是一次会话，在本次会话中都可以取到session中的值
            //若是session中有用户存在则会覆盖原来的user，当session中的user存在时判定用户存在
            session.setAttribute("user", result.getOthers());
            return new RedirectView("/index", true, false, true);
        } else {
            return new RedirectView("/login", true, false, true);
        }
    }

    @RequestMapping(value = "/userList", method = RequestMethod.GET)
    public RedirectView user(Model model,
                             @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                             HttpSession session) {

        List<User> users = userService.getAllUser(pageNumber);

        if (users != null) {
            //添加到session中，session是一次会话，在本次会话中都可以取到session中的值
            //若是session中有用户存在则会覆盖原来的user，当session中的user存在时判定用户存在
            model.addAttribute("userList", users);
            return new RedirectView("/userList", true, false, true);
        }
        model.addAttribute("result", "无结果");
        return new RedirectView("/userList", true, false, true);
    }
}
