package com.nowcoder.community.controller;


import com.google.code.kaptcha.Producer;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptchaProducer;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    //点击导航栏的 注册 按钮 跳转到 注册页面
    @RequestMapping(path = "/register",method = RequestMethod.GET)
    public String getRegisterPage() {
        return "/site/register";
    }

    //点击导航栏的 登录 按钮 跳转到 登录页面
    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String getLoginPage() {
        return "/site/login";
    }

    //form表单的 th:action="@{/register}"  当在注册页面 要提交注册信息 点击注册按钮时 POST请求服务器资源
    @RequestMapping(path ="/register",method = RequestMethod.POST)
    public String register(Model model, User user) {
        //@RequestMapping修饰的方法 的参数user，相当于之前的 Page  客服端在请求链接时所携带的参数
        //如果name的值和User的属性相匹配 springMVC 基于同名原则 会自动把值自动注入到方法的参数 User对象的属性中

        //调用service层的register方法 完成注册功能 所需的参数为 方法里已经被注入属性值的User对象
        Map<String, Object> map = userService.register(user);
        if(map == null || map.isEmpty()) {
            model.addAttribute("msg","注册成功，我们已向您的邮箱发送了一份激活邮件，请尽快激活！");
            model.addAttribute("target","/index");
            return "/site/operate-result";
            //注册成功就跳转到 等待页面
        } else {
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));
            return "/site/register";
            //提交的表单有问题 就重新回到 注册页面
        }

    }

    //激活的链接过来 后面两个是需要的参数 path里面要定义变量 方便下面方法从路径中取值
    @RequestMapping(path="/activation/{userId}/{code}",method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code) {
        int result = userService.activation(userId, code);
        if (result == ACTIVATION_SUCCESS) {
            model.addAttribute("msg","激活成功，您的账号已经可以正常使用了！");
            model.addAttribute("target","/login");
        } else if (result == ACTIVATION_REPEAT) {
            model.addAttribute("msg","无效操作，该账号已经激活过了！");
            model.addAttribute("target","/index");
        } else {
            model.addAttribute("msg","激活失败，您提供的激活码不正确！");
            model.addAttribute("target","/index");
        }

        return "/site/operate-result";
    }

    //验证码
    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        //生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        //将验证码存入session
        session.setAttribute("kaptcha", text);

        //将图片输出给浏览器
        response.setContentType("image/png");


        //os 由spring容器管理 会自动关闭
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            logger.error("响应验证码失败：" + e.getMessage());
        }


    }

    //当登录不成功，要重新返回login页面给浏览器时，需要把之前提交过的form表单的信息 重新填到要返回的页面中
    //如果这个mapper的参数是 一个实体类 springMvc 会自动把实体类 装到model中
    //如果这个mapper的参数是 基本类型 springMvc 不会把它 装到model中
    // 有两种方法解决
    //   1:在mapper方法里面 把 基本类型 添加到model里面
    //   2:通过request.getParameter()获取 在页面中 th:value="${param.username}"
    //      因为这个mapper的参数来的时候 是存在于 request中的 request还是可以传给页面的
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(Model model, HttpSession session, HttpServletResponse response,
                        String username, String password, String code, boolean rememberme) {
        //HttpSession是为了 验证码 HttpServletResponse 是为了使用cookies保存凭证 rememberme 是“记住我”

        System.out.println(rememberme);
        //首先判断验证码是否正确 属于表现层 跟业务层无关
        String kaptcha = (String) session.getAttribute("kaptcha");
        if(StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
            model.addAttribute("codeMsg","验证码不正确！");
            return "/site/login";
        }

        //检查账号，密码 要调用业务层方法
        int expiredSeconds = rememberme ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDE;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if(map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(contextPath);
            //凭证 cookie的有效范围 整个项目都有效
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:index";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/login";
        }
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/login";
        //login 有两个类型一个是get 一个是post 重定向 默认是get
    }


}
