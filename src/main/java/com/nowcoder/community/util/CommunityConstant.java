package com.nowcoder.community.util;

public interface CommunityConstant {

    //激活成功
    int ACTIVATION_SUCCESS = 0;

    //重复激活
    int ACTIVATION_REPEAT = 1;

    //激活失败
    int ACTIVATION_FAILURE = 2;

    //默认的登录状态凭证的超时时间 2个小时
    int DEFAULT_EXPIRED_SECONDE =  60 * 60 * 2;

    //记住我的 登录状态凭证的超时时间 一天
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24;
}
