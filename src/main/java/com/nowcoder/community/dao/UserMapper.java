package com.nowcoder.community.dao;

//访问数据库 mybatis中的数据库访问组件称为mapper 只需要写接口 不需要写实现类

import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.Mapper;
//也可以用Repository mapper注解是mybatis下的
@Mapper
public interface UserMapper {
    //声明一些增删改查的方法 再写上对应的sql配置文件 mapper目录下的xml文件
    //在mapper.xml中要写上每一个 接口中的方法所对应的标签
    //通过id查user
    User selectById(int id);
    //通过用户名查user
    User selectByName(String username);
    //通过email查user
    User selectByEmail(String email);

    //增加一个用户 返回一个整数 即插入数据的行数 成功的话就1行
    int insertUser(User user);
    //修改user状态 返回的是条数
    int updateStatus(int id, int status);
    //更新头像
    int updateHeader(int id, String headerUrl);
    //更新密码
    int updatePassword(int id,String password);



}
