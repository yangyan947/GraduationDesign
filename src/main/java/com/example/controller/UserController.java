package com.example.controller;

import com.example.domain.User;
import com.example.service.BlogService;
import com.example.service.UserService;
import com.example.service.message.Message;
import com.example.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by SunYi on 2016/2/1/0001.
 */
@Controller
//控制类，控制页面跳转，数据传输
public class UserController {
    //自动注入userService，用来处理业务
    @Autowired
    private UserService userService;
    @Autowired
    private BlogService blogService;
    public static final String USER = "user";

    //跳转链接，跳转到主页xx
    @RequestMapping("/")
    public RedirectView index(Model model,
                              HttpServletResponse response) {
        //对应到templates文件夹下面的index
        return new RedirectView("/index", true, false, true);
    }

    //真正主页，用户在访问 XXXXX/index就会跳转该方法，
    // 这个XXXXX是你的域名，自己电脑上的话一般都是127.0.0.1:8080或者是localhost：8080
    // 8080是端口号，端口号根据tomcat设置而改变，默认值是8080
    @RequestMapping("/index")
    public String home(Model model, @RequestParam(value = "result", defaultValue = "")
                       String result,@RequestParam(value = "page", defaultValue = "1") Integer index, HttpSession session) {
        if (session.getAttribute(USER) == null && result.equals("")) {
            model.addAttribute("result", "请先登录");
            return "pages/login";
        }
        //对应到templates文件夹下面的index
        if (result != null) {
            model.addAttribute("result", result);
        }

        model.addAttribute("blogPage", blogService.getByStatusIsNot(index, "freeze"));
        return "index";
    }

    //进入注册页面，使用Get请求，REST风格的URL能更有雅的处理问题
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerGet() {
        return "pages/register";
    }

    //注册用户，使用POST，传输数据
    @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    public String  registerPost(Model model, @ModelAttribute(value = "user") User user) {
        //使用userService处理业务
        Message result = userService.register(user);
        //将结果放入model中，在模板中可以取到model中的值
        model.addAttribute("result", result.getReason());
        return "pages/login";
    }

    //登陆
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginGet(Model model, @RequestParam(value = "result", defaultValue = "") String result) {
        if (result != null || result.equals("")) {
            model.addAttribute("result", result);
        }
        return "pages/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String loginPost(Model model,
                            @ModelAttribute(value = USER) User user,
                            HttpSession session) {

        Message result = userService.login(user);

        model.addAttribute("result", result.getReason());

        if (result.isSuccess()) {
            //添加到session中，session是一次会话，在本次会话中都可以取到session中的值
            //若是session中有用户存在则会覆盖原来的user，当session中的user存在时判定用户存在
            session.setAttribute(USER, result.getOthers());
        }
        return result.toString();

    }

    //登出
    @RequestMapping(value = "/loginOut", method = RequestMethod.GET)
    public RedirectView loginOut(HttpSession session) {
        //从session中删除user属性，用户退出登录
        session.removeAttribute(USER);
        return new RedirectView("/index", true, false, true);
    }

    //用户个人信息页面
    @RequestMapping(value = "/personalCenter", method = RequestMethod.GET)
    public String userCenter(HttpSession session, Model model) {
        User user = (User) session.getAttribute(USER);
        if (user == null) {
            model.addAttribute("result", "用户未登录!");
            return "pages/login";
        } else {
            update(session);
            model.addAttribute("user", session.getAttribute(USER));
            return "pages/personal";
        }
    }

    //用户个人信息页面
    @RequestMapping(value = "/personalCenter/{id}", method = RequestMethod.GET)
    public String userCenterNormal(HttpSession session, Model model, @PathVariable(value = "id") Long id) {
        User user = (User) session.getAttribute(USER);
        if (user == null) {
            model.addAttribute("result", "用户未登录!");
            return "pages/login";
        } else {
            if (user.getId() == id) {
                update(session);
                model.addAttribute("user", session.getAttribute(USER));
                return "pages/personal";
            } else {
                Message message = userService.getUserById(id);
                model.addAttribute("result", message.getReason());
                if (message.isSuccess()) {
                    model.addAttribute("user", message.getOthers());
                    return "pages/homePage";
                } else {
                    return "index";
                }
            }

        }
    }
    //关注用户
    @RequestMapping(value = "/attendUser", method = RequestMethod.GET)
    public RedirectView attendUser(HttpSession session, Model model, @RequestParam(value = "userId") Long userId) {
        User user = (User) session.getAttribute(USER);
        if (user == null) {
            model.addAttribute("result", "用户未登录!");
            return new RedirectView("/login", true, false, true);
        }
        Message message = userService.attendUser(user, userId);
        if (message.isSuccess()) {
            session.setAttribute(USER, message.getOthers());
        }
        model.addAttribute("result", message.getReason());
        return new RedirectView("/personalCenter/" + userId, true, false, true);

    }
    //关注用户
    @RequestMapping(value = "/unAttendUser", method = RequestMethod.GET)
    public RedirectView unAttendUser(HttpSession session, Model model, @RequestParam(value = "userId") Long userId) {
        User user = (User) session.getAttribute(USER);
        if (user == null) {
            model.addAttribute("result", "用户未登录!");
            return new RedirectView("/login", true, false, true);
        }
        Message message = userService.unAttendUser(user, userId);
        if (message.isSuccess()) {
            session.setAttribute(USER, message.getOthers());
        }
        model.addAttribute("result", message.getReason());
        return new RedirectView("/personalCenter/" + userId, true, false, true);
    }

    //关注用户
    @RequestMapping(value = "/attendUserAjax", method = RequestMethod.GET, produces = "text/html;charset=UTF-8" )
    @ResponseBody
    public String attendUserAjax(HttpSession session, Model model, @RequestParam(value = "userId") Long userId) {
        User user = (User) session.getAttribute(USER);
        Message message = userService.attendUser(user, userId);
        session.setAttribute(USER, message.getOthers());
        return message.toString();
    }

    //用户信息修改
    @RequestMapping(value = "/changeUser", method = RequestMethod.GET)
    public String changeUserGet(HttpSession session, Model model) {
        if (session.getAttribute(USER) == null) {
            model.addAttribute("result", "未登录");
        } else {
            update(session);
            model.addAttribute("user", session.getAttribute(USER));
        }
        return "changeUser";
    }

    @RequestMapping(value = "/changeUser", method = RequestMethod.POST)
    public RedirectView changeUserPost(HttpSession session, Model model, @ModelAttribute(value = USER) User user) {
        Message message = userService.changeUser(user, (User) session.getAttribute(USER));
        model.addAttribute("result", message.getReason());

        if (message.isSuccess()) {
            session.setAttribute(USER, message.getOthers());
            return new RedirectView("/personalCenter", true, false, true);
        } else {

            return new RedirectView("/personalCenter", true, false, true);
        }
    }

    @RequestMapping(value = "/changeUserImg", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public RedirectView changeUserImg(HttpSession session, Model model, @RequestParam(value = "img") MultipartFile file) throws Exception {
        User user = (User) session.getAttribute(USER);
        String imgUrl = saveFile(file, session);
        Message message = userService.changeUserImg(user, imgUrl);
        update(session);
        return new RedirectView("/personalCenter", true, false, true);
    }

    @RequestMapping(value = "/changeUserPassword", method = RequestMethod.POST)
    public RedirectView changeUserPassword(HttpSession session,
                                           Model model, @RequestParam(value = "oldPassword") String oldPassword,
                                           @RequestParam(value = "newPassword") String newPassword) {
        Message message = userService.changeUserPassword((User) session.getAttribute(USER), oldPassword, newPassword);
        model.addAttribute("result", message.getReason());
        if (message.isSuccess()) {
            session.removeAttribute(USER);
            return new RedirectView("/login", true, false, true);
        } else {
            return new RedirectView("/personalCenter", true, false, true);
        }
    }

    public User update(HttpSession session) {
        User oldUser = (User) session.getAttribute(USER);
        session.removeAttribute(USER);
        User newUser = userService.update(oldUser);
        session.setAttribute(USER, newUser);
        return newUser;
    }

    private String saveFile(MultipartFile file, HttpSession session) throws Exception {
        String fileType = file.getContentType().split("/")[1];
        String path = session.getServletContext().getRealPath("/");
        String separator = File.separator;
        String uuid = UUID.randomUUID().toString();
        FileOutputStream fos = null;
        String fileName = null;
        try {
            InputStream fis = file.getInputStream();
            // 转换文件为png格式，并保存在同名目录下
            File files = new File(path + "\\upload");
            // 判断文件夹是否存在,如果不存在则创建文件夹
            if (!files.exists()) {
                files.mkdir();
            }
            if (file.getContentType().split("/")[0].equals("image")) {
                if (path.endsWith(separator))
                    fileName = path + "upload" + separator + uuid + ".png";
                else
                    fileName = path + separator + "upload" + separator + uuid + ".png";
                fos = new FileOutputStream(fileName);
                ImageUtil.convertFormat(fis, fos, fileType, "png", 0, 0);
                fos.flush();
                fos.close();
            }
        } catch (Exception ex) {
            System.out.println("文件取出失败，错误信息: " + ex.getMessage());
            if (fos != null)
                fos.close();
            throw ex;
        }
        return "/upload" + separator + uuid + ".png";
    }
}
