package com.nowcoder.community.util;

import com.nowcoder.community.entity.User;
import org.springframework.stereotype.Component;

/**
 *主要起到一个容器的作用 持有用户的信息 用于代替session对象
 */

@Component
public class HostHolder {

    //原理：以线程为key 存取对象（值）
    private ThreadLocal<User> users = new ThreadLocal<User>();

    public void setUsers(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }


}
