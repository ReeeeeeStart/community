package com.nowcoder.community.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {

    //spring boot 没有为kaptcha提供自动配置
    //声明了 它将会被spring 容器所管理装配 kaptcha其核心的对象 为一个接口
    @Bean
    public Producer kaptchaProducer() {
        //Producer是kaptcha的核心接口
        //DefaultKaptcha是Kaptcha核心接口的默认实现类

        //利用properties初始化kaptcha对象
        Properties properties = new Properties();
        properties.setProperty("Kaptcha.image.width", "100");
        properties.setProperty("Kaptcha.image.height", "40");
        properties.setProperty("Kaptcha.textproducer.font.size", "32");
        properties.setProperty("Kaptcha.textproducer.font.color", "0,0,0");
        properties.setProperty("Kaptcha.textproducer.char.string", "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        properties.setProperty("Kaptcha.textproducer.char.length", "4");
        properties.setProperty("Kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise");


        DefaultKaptcha kaptcha = new DefaultKaptcha();
        //kaptcha 需要传入一些 参数对象 
        Config config = new Config(properties);
        kaptcha.setConfig(config);
        return kaptcha;
    }

}
