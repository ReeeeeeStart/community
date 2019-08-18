package com.nowcoder.community;

import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.service.AlphaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)

//以src下的CommunityApplication.class为配置类进行测试
//继承该接口：ApplicationContextAware 并实现set方法，spring容器便会找到该类，然后把自己（applicationContext）作为参数传进去。
public class CommunityApplicationTests implements ApplicationContextAware {
	//参数 spring容器 ApplicationContext 是一个接口  继承于 BeanFactory（spring容器的顶层接口）

	private  ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		//把传进来的容器 暂存引用。
		this.applicationContext = applicationContext;
	}

	@Test
	public void testApplicationContext() {
		System.out.println(applicationContext);
		//GenericWebApplicationContext@a3d8174 实现的类名@哈希code
		//r如何使用容器管理bean？

		//通过类名获取容器里面的Bean对象。
		//调用的地方是 写两个实现类（Bean）的接口 需要更换实现类 只要在实现类上面添加@Primary注解就可以优先加载想要的实现类
		//所以调用的地方 代码是不变的 “面向接口”的实现。 spring容器降低了Bean之间的耦合度，调用方 和 实现类 不会发生 直接的关系
		AlphaDao alphaDao = applicationContext.getBean(AlphaDao.class);
		System.out.println(alphaDao.select());

		//要是程序 还想要 接口的另一个实现类 Hibernate 的？
		//首先在 实现类上面的注解 给该bean取一个 名字 之后调用的 时候 直接调用改实现类的 bean
		alphaDao = applicationContext.getBean("alphaHibernate",AlphaDao.class);
		System.out.println(alphaDao.select());

	}

	//测试 容器 管理bean对象
	@Test
	public void testBeanManagement() {
		AlphaService alphaService = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);

		//被spring容器管理的bean 默认是 单例模式  只被实例化一次 销毁一次
		alphaService = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);

	}

	//测试容器是否能取到第三方中的bean
	@Test
	public void testBeanConfig() {
		SimpleDateFormat simpleDateFormat = applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(new Date());
		System.out.println(simpleDateFormat.format(new Date()));
	}

	//依赖注入
	@Autowired
	@Qualifier("alphaHibernate") //@Primary已经写在MyBatis上 可以以此选择另一种实例化的方式
	private AlphaDao alphaDao;//容器直接将bean注入给这个属性 直接使用属性就可以 bean通过接口调用 底层实现不直接跟它耦合

	@Autowired
	private AlphaService alphaService;

	@Autowired
	private SimpleDateFormat simpleDateFormat;

	@Test
	public void testDI() {
		System.out.println(alphaDao);
		System.out.println(alphaService);
		System.out.println(simpleDateFormat);
	}
}
