package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//controller的访问路径是可以省略的  这一级就是空的 直接访问方法就可以
@Controller
public class HomeController {
    @Autowired
    private DiscussPostService discussPostService;
    //DiscussPostService(service) --> DiscussPostMapper(dao) --> DiscussPost(entity)

    @Autowired
    private UserService userService;

    //返回 String 是视图的名称
    //通过model携带数据给模板
    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page)
    {
        //spring MVC中 方法的参数（model, 参数） 在方法调用之前 都是由前端控制器 来实例化的 同时会把 page注入到model里面
        //在thymeleaf中可以直接访问page对象中的数据。
        //完善page组件的值 直接查 数据库 返回新的值
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        //page的属性 在每次请求来之后都会 被重新实例化 以更新current的值
        //这样getOffset返回的值 也更新了 在这次请求返回的 结果中所查到的 数据库数据是 新的数据

        //装配discussPostService对象后 调用它的方法 即controller调用service层方法
        //current的值更新，调用page.getOffset()方法，更新offset的值，从数据库中得到想要的行list：offset~offset+limit
        List<DiscussPost> list = discussPostService.findDiscussPosts(0,page.getOffset(),page.getLimit());

        //下面定义的集合 包含了 DiscussPost 和 User 对象 放到一个map里面 map就是List的E （再写一个类/数组/map）
        List<Map<String,Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            for (DiscussPost post: list) {
                //遍历list的过程中 一个map里面放 一个post 和 一个根据post查询到的User
                Map<String,Object> map = new HashMap<>();

                map.put("post",post);

                User user = userService.findUserById(post.getUserId());
                map.put("user",user);
                //把这一个map加到List里面
                discussPosts.add(map);
            }
        }
        //把要展示的数据 装到model里面
        model.addAttribute("discussPosts",discussPosts);
        //templates目录下
        return "/index";
    }



}
