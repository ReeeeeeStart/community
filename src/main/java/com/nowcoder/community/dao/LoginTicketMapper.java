package com.nowcoder.community.dao;

import com.nowcoder.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LoginTicketMapper {

    //insert 一个凭证数据
    //采用注解的形式 完善mapper 书写简单 不用多写一个文件 不方便阅读
    //sql语句由几个字符串 拼接而成 #{} 为接口方法的参数或参数中的属性名
    @Insert({
            "insert into login_ticket(user_id,ticket,status,expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"
    })
    //声明主键自生成
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);


    //依据 ticket 查询凭证 ticket是要发送给客服端 让它保存的
    @Select({
            "select id,user_id,ticket,status,expired ",
            "from login_ticket where ticket=#{ticket}"
    })
    LoginTicket selectByTicket(String ticket);


    //注解写动态sql语句
    //"<script>",
    //"update login_ticket set status=#{status} where ticket=#{ticket}",
    //"<if test=\"ticket!=null\">",
    //"and 1=1",
    //"</if>",
    //"</script>"
    //
    //
    //
    @Update({
            "update login_ticket set status=#{status} where ticket=#{ticket}"
    })
    int updateStatus(String ticket, int status);
}
