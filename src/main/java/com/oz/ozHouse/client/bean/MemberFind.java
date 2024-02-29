package com.oz.ozHouse.client.bean;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class MemberFind {
   public static String replaceMiddleWithAsterisks(String str) {
        if (str == null || str.length() < 3) {
            return str; // 짧은 문자열은 변경하지 않음
        }

        int middle = str.length() / 2;
        if (str.length() % 2 == 0) {
            return str.substring(0, middle - 2) + "***" + str.substring(middle + 2);
        } else {
            return str.substring(0, middle - 2) + "***" + str.substring(middle + 2);
        }
    }
   
    public static int sendEmailCheck(String member_email, String member_id) {

        final String username = "me1taphor1@gmail.com";
        final String password = "";
        
        // 난수의 범위 111111 ~ 999999 (6자리 난수)
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
        
        // 이메일 보낼 양식
        String content = "회원님의 아이디는" + replaceMiddleWithAsterisks(member_id) + ", 비밀번호 인증 코드는 " + checkNum + " 입니다. 해당 인증 코드를 인증 코드 확인란에 기입하여 주세요.";
        System.out.println(checkNum);

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("from@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse("oz@admin.com, " + member_email)
            );
            message.setSubject("[오즈의 집] 회원정보 찾기 이메일입니다");
            message.setText(content);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        
        return checkNum;
    }
}