package com.nowcoder.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommunityApplication {

	// String[] args ？
	public static void main(String[] args) {
		//底层启动tomcat、自动创建了spring容器，再去自动扫描某些包下的某些Bean，再装配到容器里。
		//扫描 配置类 所在的包 及 其子包下 有类似Controller（Component实现的）功能注解 的 Bean
		//参数是一个类 是配置文件 由@SpringBootApplication 注解标示
		SpringApplication.run(CommunityApplication.class, args);
	}

}
