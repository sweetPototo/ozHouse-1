package com.oz.ozHouse.client;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawling2 {
		// 크롤링 할 URL을 가지고 온다.
    private static String URL = "https://www.namuddl.co.kr/shop/shopbrand.html?xcode=016&type=X";

    public static void main(String[] args) throws IOException {
        // 1. Document를 가져온다.
        Document doc = Jsoup.connect(URL).get();
        
        // 2. 목록을 가져온다.
        Elements els = doc.select(".item");
        //System.out.println(els);
        
        // 3. 목록(배열)에서 정보를 가져온다.
        Map<String, String> map = new HashMap<String, String>();
        for (Element imageElement : els) {

						// imageElement.select("img") >> img 태그
						// .attr("src") >> scr 속성을 가지고 온다.
						// 그러므로 아래의 코드를 해석하면 img태그의 src속성을 imageUrl로 저장하겠다.
            String imageUrl = imageElement.select("img").attr("src");
            
						// D드라이브 안의 new2라는 폴더를 생성하고 만약 생성되지 않았다면 생성하는 코드
            File tFolder = new File("d:/new2/");
            if (!tFolder.exists()) {
                tFolder.mkdirs();
            }
            //System.out.println(imageUrl);
            
						// 파일 이름을 지정하기 위해서 작성한 코드
            String pid = imageUrl.substring(imageUrl.indexOf("namuddl/") + "namuddl/".length());
            String finalString = pid.substring(0, pid.indexOf(".jpg"));          
//            String finalResult = finalString.replaceAll("/_[^/]+", "");
//            System.out.println(pid);
//            System.out.println(finalString);
//            System.out.println(finalResult);
           
			// 이미지를 가져올 URL을 지정한다.
            URL url = new URL("https://www.namuddl.co.kr" + imageUrl);
            //System.out.println(url);
            
			// 이 코드는 url 객체가 특정 URL을 나타내고, 
			// 해당 URL에서 데이터를 읽어오는 InputStream을 열어서 이를 변수 is에 할당.
            InputStream is = url.openStream();

			// 파일을 저장할 때 d:/new2/경로에 finalString.jsp로 저장이된다.
			// finalString의 예시는 /shopimages/namuddl/0160010004432.jpg?1694599677
			// imageUrl이 이렇게 나올 경우 namuddl/뒤의 0160010004432만 들고와서
			// 파일 이름으로 지정해놓았다.
            FileOutputStream fos = new FileOutputStream("d:/new2/"+finalString+".jpg");
            int b;
            while ((b = is.read()) != -1) {
                fos.write(b);
            }
            fos.close();
            
            System.out.println(fos);
            System.out.println("완료");
        }
    }
}
