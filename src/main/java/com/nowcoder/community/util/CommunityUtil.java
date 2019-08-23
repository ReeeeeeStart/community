package com.nowcoder.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class CommunityUtil {

    //简单的静态方法 不用交给容器去管理

    //生成一个随机字符串 激活码或者上传文件时命名用。
    //java自带的一个工具 UUID
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }


    //MD5 加密 密码  能加密不能解密
    //密码加盐
    public static String md5(String key) {
        //先判断是不是为空 为空就没有必要加密 null或者空格或空串
        if (StringUtils.isBlank(key)) {
            return null;
        }

        //spring自带的一个工具 把传入的字节加密成一个十六进制的字符串
        return DigestUtils.md5DigestAsHex(key.getBytes());

    }

}
