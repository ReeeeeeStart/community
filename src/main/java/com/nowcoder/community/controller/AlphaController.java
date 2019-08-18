package com.nowcoder.community.controller;

import com.nowcoder.community.service.AlphaService;
import com.sun.org.apache.bcel.internal.generic.RET;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sun.rmi.log.LogInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

//这是一个controller 访问路径为/alpha
@Controller
@RequestMapping("/alpha")
public class AlphaController {

    //通过属性的依赖注入 将AlphaService的bean注入给controller层
    @Autowired
    private AlphaService alphaService;

    @RequestMapping("/hello")
    @ResponseBody
    public  String sayHello() {
        return  "Hello Spring Boot.";
    }

    //模拟处理查询请求
    @RequestMapping("/data")
    @ResponseBody
    public String getData() {
        return alphaService.find();
    }

    //1.14 dispatcherServlet 管理 controller model view
    //一、看一下dispatcherServlet 所拥有的 request和response
    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response) {
        // 获取请求数据 在控制台上面输出 dispatcherServlet获取到的请求的具体情况
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration = request.getHeaderNames();
        while(enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ": " + value);
        }
        System.out.println(request.getParameter("code"));

        //dispatcherServlet 返回响应数据 到浏览器 浏览器端显示html页面
        response.setContentType("text/html;charset=utf-8");
        try ( PrintWriter writer = response.getWriter();) {
            writer.write("<h1>牛客网</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //二、dispatcherServlet 具体怎么管理请求

    // 1：GET请求
    //怎么处理请求的数据 get请求（默认发送的请求）（get请求所携带的参数是明面的 而且有长度限制）
    //get 请求中有两种传参方式 一种是 ?xx&xx 另一种是 /xx/xxx 路径传参

    // （1）：/students?current=1&limit=20
    @RequestMapping(path = "/students",method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(
            @RequestParam(name = "current", required = false, defaultValue = "1") int current,
            @RequestParam(name = "limit", required = false, defaultValue = "10")int limit) {
        //dispatcherServlet会检测到请求带过来的参数，之后会与方法的参数名称匹配，直接赋值。
        //在方法的参数声明中 通过@RequestParam注解对参数的注入 做更详细的声明
        System.out.println(current);
        System.out.println(limit);
        //控制台打印 请求所携带的参数
        return "some Students";
        //返回到浏览器的是 一段字符串
    }

    //（2）：参数作为路径的一部分 student/123
    //@RequestMapping 注解中 路径声明 需要用{} 包住 参数名称
    //同时 方法的参数声明中 还有用注解@PathVariable 对参数 进行详细声明
    @RequestMapping(path = "/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id) {
        System.out.println(id);
        return "a student";
    }

    //2：POST 请求
    //浏览器向服务器提交数据 表单提交
    //form表单的action路径即 @RequestMapping所定义 的path路径
    //表单中 input的name 即参数的名称

    @RequestMapping(path = "/student", method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name, int age) {
        //方法的参数和form表单的name参数 一致就可以传递
        System.out.println(name);
        System.out.println(age);
        return "success";
    }

    //三、如何向浏览器返回response数据（动态的html数据，而不是上面的字符串 @ResponseBody）
    //不加ResponseBody注解 默认返回html数据

    //1：第一种方式 返回 ModelAndView
    //需要在 模板引擎templates下 创建view 即html文件
    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    public ModelAndView getTeacher() {
        //创建一个 ModelAndView 对象 然后返回
        //model view 都交给模板引擎templates 去渲染生成的动态html
        //name age 这些object 就是动态的 model 更新到 view中 view中iput需要定义
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name","张三");
        modelAndView.addObject("age",30);
        //设置模板的路径和名字 进行绑定 默认在templates下面
        modelAndView.setViewName("/demo/view");
        return modelAndView;
    }

    //2：第二种更加简洁的方式
    //dispatcherServlet 已经拥有的 Model对象 把引用作为参数 传递给方法
    //方法中完善Model对象 最后再返回一个view在路径 model 和 view 便可以结合
    @RequestMapping(path = "/school", method = RequestMethod.GET)
    public String getSchool(Model model) {
        model.addAttribute("name","北京大学");
        model.addAttribute("age", "80");
        //model 这个对象是由dispatcherServlet创建的 只是作为参数传到这个方法里面
        return "/demo/view";
        //return view的路径给 进行绑定
    }

    //四、向浏览器响应json数据 一般是在异步请求中
    //异步请求：当前网页不动，不刷新，但是有访问服务器，接受数据。
    //json是一个具有特定格式的字符串  java对象 ——> json字符串 ——> Js对象
    //需要@ResponseBody 返回的是字符串 而方法内 返回的是 一个对象 dispatcherServlet会自动将对象转成字符串josn

    @RequestMapping(path = "/emp", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getEmp() {
        Map<String,Object> emp = new HashMap<>();
        emp.put("name","张三");
        emp.put("age","22");
        emp.put("salary",8000.00);
        return emp;
        //dispatcherServlet 在调这个方法的时候 有@ResponseBody注解 而且返回值是一个map对象
        //它自动会将这个对象转为josn字符串
    }

    //map换成list也是一样的 都是对象
    @RequestMapping(path = "/emps", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getEmps() {

        List<Map<String, Object>> list = new ArrayList<>();

        Map<String,Object> emp = new HashMap<>();
        emp.put("name","张三");
        emp.put("age",23);
        emp.put("salary",8000.00);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name","李四");
        emp.put("age",24);
        emp.put("salary",9000.00);
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name","王五");
        emp.put("age",25);
        emp.put("salary",8500.00);
        list.add(emp);

        return list;
    }

}
