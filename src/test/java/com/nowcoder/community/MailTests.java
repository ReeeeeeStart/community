package com.nowcoder.community;


import com.nowcoder.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {

    @Autowired
    private MailClient mailClient;

    //主动调用thymeleaf模板引擎 为了生成一个html的content（String）
    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail () {
        mailClient.sendMail("1416906645@qq.com","Test","Hello");
    }

    @Test
    public void testHtmlMail() {
        Context context = new Context();
        context.setVariable("username","sb陈小奇");

        //content即生成的动态网页 把参数传到模板引擎中 模板引擎==>即找到其对应的路径
        String content = templateEngine.process("/mail/demo",context);
        System.out.println(content);

        mailClient.sendMail("" +"15960529588@163.com","html测试！",content);
    }

}
