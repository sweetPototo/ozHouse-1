package com.oz.ozHouse.merchant.bean;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class TSL{
    public static int sendEmailCheck(String email) {

        final String username = "(실제 email)@gmail.com";
        final String password = "(실제 비밀번호)";
        
        // 6자리 난수 생성
        Random random = new Random();
        int checkNum = random.nextInt(888888) + 111111;
 
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        
        String content = "인증 코드는 " + checkNum + " 입니다. 해당 인증 코드를 인증 코드 확인란에 기입하여 주세요.";
        System.out.println(checkNum);

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("from@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("oz@admin.com, " + email)
            );
            message.setSubject("[OZ의 집] 회원 가입 인증 이메일 입니다.");
            message.setText(content);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        
        return checkNum;
    }

}