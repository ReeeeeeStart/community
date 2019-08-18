package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    //考虑到将来 查看我的帖子的 功能  参数有userId 首页的时候 不用userId 让其为0即可
    //还需要 动态sql语句
    //offset:每一页起始行行号 limit:每一页最多显示多少条数据

    //在写sql配置的时候 不用说明List<T> 这个java自动会识别 而DiscussPost需要说明
    //<select id="selectDiscussPosts" resultType="DiscussPost">
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    //查询表里一共有多少个帖子
    //@Param 给参数起别名 如果只有一个参数，并且在<if>里面使用，则必须加别名 不然会报错
    int selectDiscussPostRows(@Param("userId") int userId);
}
