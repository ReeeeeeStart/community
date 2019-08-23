package com.nowcoder.community.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

//表示一个通用的bean 由spring容器去管理
@Component
public class MailClient {


    //之后要记录一些日志
    private static final Logger logger = LoggerFactory.getLogger(MailClient.class);

    //要用到一个spring java的核心组件 JavaMailSender 也由spring容器管理
    @Autowired
    private JavaMailSender mailSender;

    //把配置文件中的信息注入到属性中
    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(String to, String subject, String content) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            //将一个MimeMessage绑定给helper 通过helper的方法完善message 之后get出来给mailSender
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            //把异常error记录到日志中
            logger.error("发送邮件失败：" + e.getMessage());
        }

    }


}
