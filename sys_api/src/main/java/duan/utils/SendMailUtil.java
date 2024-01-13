package duan.utils;

import com.sun.mail.util.MailSSLSocketFactory;
import duan.config.MailProperties;
import duan.service.impl.PicCheckServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;


@Slf4j
@Component
@EnableScheduling
public class SendMailUtil {

    @Autowired
    private MailProperties mailProperties;
    @Autowired
    private RedisUtil redisUtil;
    @Value("${mail.pichost}")
    public String picHost;
    @Autowired
    private LogUtils logUtils;


    /**
     * 发送邮件
     * @param pids 图片 id
     *
     */
    @Async
    public void sendMail(List<Integer> pids, String mail) throws MessagingException, GeneralSecurityException {
        String senderMail = mailProperties.getSenderMail();         // 发件人电子邮箱
        String mailHost = mailProperties.getHost();                 // 指定发送邮件的主机
        String password = mailProperties.getPassword();             // 发件邮箱的密钥授权码
//        String senderMail="heiheiduan02@163.com";
//        String mailHost="smtp.163.com";
//        String password="UYPDIGARZZAKUDXT";
        // 获取系统属性
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", mailHost);
        properties.put("mail.smtp.auth", "true");

        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);

        // 获取默认 session 对象
        Session session = Session.getDefaultInstance(properties, new Authenticator() {

            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                //发件人邮件用户名、授权码
//                return new PasswordAuthentication("heiheiduan02@163.com", "UYPDIGARZZAKUDXT");
                return new PasswordAuthentication(senderMail, password);
            }

        });
        // 创建默认的 MimeMessage 对象
        MimeMessage message = new MimeMessage(session);
        // Set From: 头部头字段
        message.setFrom(new InternetAddress(senderMail));
        // Set To: 头部头字段
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail));
        // Set Subject: 头部头字段
        String sub = "ATRI-PIC-图片审核通过通知";
//        picHost = "http://localhost:8080";
        message.setSubject(sub);
        String text = "";
        // 设置消息体
        if(pids.size()==1) {
            Integer pid = pids.get(0);
             text= "您好，您上传的id为" + pid + "的图片已通过审核！" + "\n" + "图片地址为：" + picHost + "/art/" + pid;
        }
        else {
            text = "您好，您上传的图片已通过审核！" + "\n" + "图片地址为：";
            for (Integer pid : pids) {
                text += picHost + "/art/" + pid + "\n";
            }

        }
        message.setText(text);
        // 发送消息
        Transport.send(message);
        log.info("发送邮件：\tfrom: {}\tto:{}\tsub: {}\ttext: {}\t邮件发送成功....",  senderMail,mail, sub, text);
    }

    /**
     * 定时发送邮件
     */

    @Scheduled(fixedRate = 60000) // 每隔60秒检测一次redis中是否有待发送的邮件
    public void sendMail() {
//        log.info("test");
        Map<Object, Object> userMails = redisUtil.hmget("userMails");
        if (userMails.size()==0)
            return;
        userMails.forEach((k,v)->{
            String uuid = (String) k;
            PicCheckServiceImpl.UserMail userMail = (PicCheckServiceImpl.UserMail) v;
            String mail = userMail.getMail();
            System.out.println("外层"+userMails);
            if(mail==null||mail.equals("")){
                redisUtil.hdel("userMails",uuid);
                System.out.println("进入"+userMails);
                return;
            }
            List<Integer> pids = userMail.getPids();
            try {
                sendMail(pids,mail);
                log.info("发送邮件成功");
//                LogUtils logUtils = new LogUtils();
//                logUtils.mailLog(pids.get(0),mail);
            } catch (MessagingException | GeneralSecurityException e) {
                throw new RuntimeException(e);
            }
        });
        // 清空redis
        redisUtil.del("userMails");


    }



    public static void main(String[] args) {
        SendMailUtil sendMailUtil = new SendMailUtil();
        try {
            sendMailUtil.sendMail(Arrays.asList(1,2,3), "783809979@qq.com");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    }
