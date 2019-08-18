package com.nowcoder.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration
public class AlphaConfig {

    //定义第三方的bean
    @Bean
    public SimpleDateFormat simpleDateFormat() {
        return  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //方法所返回的对象将被装到容器里面 bean的名字就是方法名
    }
}
