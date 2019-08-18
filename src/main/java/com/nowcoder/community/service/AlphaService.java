package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class AlphaService {

    //通过属性依赖注入 dao的bean AlphaDao
    @Autowired
    private AlphaDao alphaDao;

    public AlphaService() {
        System.out.println("实例化AlphaService");
    }

    //spring 容器管理 bean对象的初始化方法和销毁方法

    //这个方法会在构造器之后调用 初始化
    @PostConstruct
    public  void  init() {
        System.out.println("初始化AlphaService");
    }

    //在销毁对象之前调用它
    @PreDestroy
    public void destroy() {
        System.out.println("销毁AlphaService");
    }

    //模拟实现查询业务  通过alphaDao访问数据库
    public String find() {
        return alphaDao.select();
    }


}
