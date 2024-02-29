package com.oz.ozHouse.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawling {

    // URL을 상수로 정의
    private static final String URL = "https://ohou.se/store/category?category_id=10120000";

    public static void main(String[] args) {
        try {
            // 1.Document를 가져온다.
            Document doc = Jsoup.connect(URL).get();
//            System.out.println("doc : " + doc);

            // 2.목록을 가져온다.
            Elements els = doc.select(".production-item-image");
            System.out.println("list : " + els);

            // 3.목록에서 정보를 가져온다.
            Map<String, String> map = new HashMap<String, String>();
            for (Element imageElement : els) {
                // 이미지 URL을 가져옴
//                String imageUrl = imageElement.select("img").attr("src");
//                System.out.println("imageUrl : " + imageUrl);
            	
                // 특정 클래스를 갖는 img 태그를 선택
                Element imgTag = imageElement.select("img.css-1dapu1e").first();
                System.out.println(imgTag);
                if (imgTag != null) {
                    // 이미지 URL을 가져옴
                    String imageUrl = imgTag.attr("src");
                    System.out.println("imageUrl: " + imageUrl);
                }
                
                // 이미지를 저장할 경로 설정
                String path = "C:\\Users\\moonj\\Desktop\\project_img";
                File tFolder = new File(path);
                if (!tFolder.exists()) {
                    tFolder.mkdirs();
                }

//                // 파일 이름 추출
//                String pid = imageUrl.substring(imageUrl.indexOf("productions/") + "productions/".length());
//                System.out.println("pid : " + pid);
//                String finalString = pid.substring(0, pid.indexOf(".jpg"));
//
//                // URL로부터 이미지를 가져와서 저장
//                URL url = new URL("https://ohou.se/store/category?category_id=10120000" + imageUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 예외 처리 코드 추가
        }
    }
}
