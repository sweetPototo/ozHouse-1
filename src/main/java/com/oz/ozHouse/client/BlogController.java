package com.oz.ozHouse.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.oz.ozHouse.client.bean.LoginOkBean;
import com.oz.ozHouse.client.service.BlogMapper;
import com.oz.ozHouse.client.service.MemberMapper;
import com.oz.ozHouse.dto.BlogDTO;
import com.oz.ozHouse.dto.MemberDTO;

@Controller
public class BlogController {
	@Autowired
	private BlogMapper blogMapper;
	@Autowired
	private MemberMapper memberMapper;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    
    private static final String BLOG_PATH = "C:\\Users\\moonj\\Desktop\\ozHouse\\ozHouse\\src\\main\\webapp\\resources\\client\\blog_image";
	
    // 블로그 글쓰기 페이지
	@RequestMapping(value = "blog_write.do", method = RequestMethod.GET)
	public String blogWrite(HttpServletRequest req, 
			 @RequestParam(name = "mode", defaultValue = "photo") String mode) {

		HttpSession session = req.getSession();
		LoginOkBean loginMember = (LoginOkBean)session.getAttribute("loginMember");
		
		if(loginMember != null) {
			if(mode != null && (mode.equals("photo") || mode.equals("video"))) {
			    req.setAttribute("mode", mode);
			}
			
			return "client/blog/blog_write";
		} else if(loginMember == null) {
			req.setAttribute("msg", "로그인 후 글을 작성할 수 있습니다.");
			req.setAttribute("url", "member_login.do");
		}
		
		return "message";
	}	
	
	// 블로그 글 작성
	@RequestMapping(value = "blog_write2.do", method = RequestMethod.POST)
	public String blogWrite2(HttpServletRequest req, @ModelAttribute BlogDTO dto, BindingResult result) {
	      
	    if (result.hasErrors()) {
	        dto.setBlog_image("");
	    }
	      
	    MultipartHttpServletRequest mr = (MultipartHttpServletRequest) req;
	    List<MultipartFile> blogImages = mr.getFiles("blog_image");
	    String[] blogSubjects = mr.getParameterValues("blog_subject");
	    String[] blogContents = mr.getParameterValues("blog_content");
	    String[] blogRoomTypes = mr.getParameterValues("blog_room_type");
	    
	    List<String> encodedImages = new ArrayList<>();
	      
	    // 각 파일을 개별적으로 저장
	    String[] fileNames = new String[blogImages.size()];
	    for (int i = 0; i < blogImages.size(); i++) {
	        MultipartFile file = blogImages.get(i);
	        fileNames[i] = file.getOriginalFilename();

	        File currentFile = new File(BLOG_PATH, fileNames[i]);

	        try {
	            file.transferTo(currentFile);
	            
		        if (currentFile.exists()) {
		            String encodedImage = encodeImageToBase64(currentFile);
		            encodedImages.add(encodedImage);
		        }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    // 파일 이름들을 쉼표로 결합
	    String fileName = String.join(",", fileNames);

	    String subjectString = String.join(",", blogSubjects);
	    String contentString = String.join(",", blogContents);
	    String roomTypeString = String.join(",", blogRoomTypes);
	    
	    // BlogDTO에 문자열로 된 이미지 파일 이름 설정
	    dto.setBlog_image(fileName);
	    dto.setBlog_subject(subjectString);
	    dto.setBlog_content(contentString);
	    dto.setBlog_room_type(roomTypeString);
	    
	    HttpSession session = req.getSession();
	    LoginOkBean loginMember = (LoginOkBean)session.getAttribute("loginMember");
    	String member_id = loginMember.getMember_id();
    	
    	dto.setMember_id(member_id);
    	
    	int res = blogMapper.insert_blog(dto);
        
	    MemberDTO member = getMember(member_id);
  
        
        session.setAttribute("memberInfo", member);
	    session.setAttribute("blogDTO", dto);
	    session.setAttribute("imagePath", BLOG_PATH);
	    session.setAttribute("encodedImages", encodedImages);
	    
	    return "client/blog/blog_contents";
	    
	}
	
	// member의 정보
	public MemberDTO getMember(String member_id) {
	    MemberDTO member = memberMapper.getMember(member_id);
	    return member;
	}
	
	// 블로그 메인 페이지
	@RequestMapping(value = "blog_main.do", method = RequestMethod.GET)
	public String blog_getContents (HttpServletRequest req) {  
		List<BlogDTO> blogList = blogMapper.blog_list();
		List<String> encodedImages = new ArrayList<>();
		
	    if (!blogList.isEmpty()) {
			for (BlogDTO blog : blogList) {
			    // 여러 이미지 파일 이름을 콤마로 분리하여 배열에 저장
			    String[] fileNames = blog.getBlog_image().split(",");
			    
			    // 대표 이미지 파일 이름
			    String representativeImageName = fileNames.length > 0 ? fileNames[0] : null;
			    // 대표 이미지 파일 경로
			    File blog_img = new File(BLOG_PATH, representativeImageName);

			    try {
			        if (blog_img.exists()) {
			            String encodedImage = encodeImageToBase64(blog_img);
			            encodedImages.add(encodedImage);
			        }
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			}	    		
	    }
	    	
        // req에 속성 설정
        req.setAttribute("blog_list", blogList);
        req.setAttribute("encodedImages", encodedImages);
		
		return "client/blog/blog_main";
	}
	
	private String encodeImageToBase64(File file) throws IOException {
	    byte[] fileContent = FileUtils.readFileToByteArray(file);
	    return Base64.getEncoder().encodeToString(fileContent);
	}
	
	// 블로그 개별 페이지
	@RequestMapping(value = "blog_get.do", method = RequestMethod.GET)
	public String getBlog(HttpServletRequest req, @RequestParam(name = "num") int num) {        
	    BlogDTO blogDTO = blogMapper.getBlog(num);
	    plusReadcount(num);
	    MemberDTO member = getMember(blogDTO.getMember_id());

	    List<String> encodedImages = new ArrayList<>();

	    // 여러 이미지 파일 이름을 콤마로 분리하여 배열에 저장
	    String[] fileNames = blogDTO.getBlog_image().split(",");

	    // 대표 이미지 파일 이름
	    String representativeImageName = fileNames.length > 0 ? fileNames[0] : null;

	    for (String fileName : fileNames) {
	        // 이미지 파일 경로
	        File blogImg = new File(BLOG_PATH, fileName);
	        System.out.println("블로그 이미지 이름 : " + blogImg);
	        try {
	            if (blogImg.exists()) {
	                String encodedImage = encodeImageToBase64(blogImg);
	                encodedImages.add(encodedImage);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    req.setAttribute("blogDTO", blogDTO);
	    req.setAttribute("memberInfo", member);
	    req.setAttribute("encodedImages", encodedImages);
	    return "client/blog/blog_contents";
	}


	
	public void plusReadcount(int num) {
		int res = blogMapper.plusReadcount(num);
	}
}
