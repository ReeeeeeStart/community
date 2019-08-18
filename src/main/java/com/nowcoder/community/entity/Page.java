package com.nowcoder.community.entity;

//封装分页相关的信息 组件
public class Page {
    //页面传给服务器的：

    //解决页面传过来的数据  当前页码 默认数为1
    private int current = 1;
    //页 显示的上限
    private int limit = 10;

    //服务器传给页面的：

    //数据总数  服务端查询到的 用于计算总的页数
    private int rows;
    //查询路径 页面上显示的页码 得对应一个路径  复用分页的链接
    private String path;

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getCurrent() {
        return current;
    }

    public int getLimit() {
        return limit;
    }

    public int getRows() {
        return rows;
    }

    public String getPath() {
        return path;
    }

    //这个offset是 sql语句里面limit限制显示查询结果返回的行数需要的，offset指在查询的结果（行）中从第几行开始返回。
    //返回的第一行 和 页面显示的第一行 都是 起始行
    public int getOffset() {
        //current * limit - limit
        return (current - 1) * limit;
    }

    //获取总页数——> 显示页码
    public int getTotal() {
        //rows / limit [+1]
        if (rows % limit == 0) {
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }

    //获取起始页码   显示5个
    public int getFrom() {
        int from = current - 2;
        return from < 1 ? 1 : from;
    }

    //获取结束页码
    public int getTo() {
        int to = current + 2;
        int total = getTotal();
        return to > total ? total : to;
    }

}
