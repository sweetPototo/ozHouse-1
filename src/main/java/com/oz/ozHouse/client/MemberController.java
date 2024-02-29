package com.oz.ozHouse.client;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.oz.ozHouse.client.bean.LoginOkBean;
import com.oz.ozHouse.client.bean.TSL;
import com.oz.ozHouse.client.service.MemberMapper;
import com.oz.ozHouse.dto.MemberDTO;

@Controller
public class MemberController {
	@Autowired
	private MemberMapper memberMapper;
	
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    private MemberDTO getMember(HttpServletRequest req) {
    	HttpSession session = req.getSession();
    	LoginOkBean loginMember = (LoginOkBean)session.getAttribute("loginMember");
    	MemberDTO dto = memberMapper.getMember(loginMember.getMember_id());
    	return dto;
    }
    
    @RequestMapping(value = "/member_join.do", method = RequestMethod.GET)
    public String member_join() {
        return "client/member/member_join";
    }
    
    // 가입 인증 이메일
    @RequestMapping("/member_send_email.do")
    public String emailAuth(HttpServletRequest req, MemberDTO dto) {
        String email = req.getParameter("email");
        if (memberMapper.checkEmail(email) != null) {
			req.setAttribute("msg", "이미 가입되어 있습니다");
			req.setAttribute("url", "member_join.do");
			return "message";
        }
        
        // 메일 전송
        int num = TSL.sendEmailCheck(email);
        String checkNum = Integer.toString(num);
        req.setAttribute("checkNum", checkNum);
        req.setAttribute("email", email);
        req.setAttribute("member", dto);
        return "client/member/member_join_check";
    }
    
    // 회원 가입
    @RequestMapping("/email_join_check.do")
    public String emailAuthCheck(HttpServletRequest req, @ModelAttribute MemberDTO dto, @RequestParam Map<String, String> params) {
    	if (params.get("checkNum").equals(params.get("checkNumCheck"))) {
        	
        	HttpSession session = req.getSession();
        	MemberDTO insert = (MemberDTO)session.getAttribute("insertMember");
        	if (insert != null) dto.setMember_image(insert.getMember_image()); 
        	String passwd = dto.getMember_passwd();
        	dto.setMember_passwd(passwordEncoder.encode(passwd));
        	
        	int res = memberMapper.insertMember(dto);
    		if (res>0) {
    			req.setAttribute("msg", "회원 가입 성공 : 안녕하세요!");
    			req.setAttribute("url", "main.do");
    		}else if (res<0){
    			req.setAttribute("msg", "회원 가입 실패 : 다시 시도해 주세요");
    			req.setAttribute("url", "member_join.do");
    		}
        	return "message";
        }else {
			req.setAttribute("msg", "회원 가입 실패 : 다시 시도해 주세요");
			req.setAttribute("url", "member_join.do");
        	return "message";
        }
    }
    
    
    @RequestMapping(value = "/member_oauth.do", method = RequestMethod.POST)
    public String member_oauth(HttpServletRequest req, @ModelAttribute MemberDTO dto) {
	    int res = memberMapper.insertMember(dto);
		if (res>0) {
				req.setAttribute("msg", "sns 계정으로 회원 가입 성공 : 안녕하세요!");
				req.setAttribute("url", "main.do");
			}else if (res<0){
				req.setAttribute("msg", "회원 가입 실패 : 다시 시도해 주세요");
				req.setAttribute("url", "member_join.do");
			}
	    	return "message";
    }

    @RequestMapping(value = "/member_update.do", method = RequestMethod.GET )
    public String memberUpdate(HttpServletRequest req){
		HttpSession session = req.getSession();
    	LoginOkBean login = (LoginOkBean)session.getAttribute("loginMember");
    	MemberDTO dto = memberMapper.getMember(login.getMember_id());
    	
        req.setAttribute("upPath", req.getServletContext().getRealPath("/resources/image"));

    	if (dto.getMember_address1() != null) { 
    		String address1 = dto.getMember_address1();
    		String[] address = address1.split("/");
            if (address.length >= 3) {  // Ensure there are at least 3 components
                req.setAttribute("address1_ad1", address[0]);
                req.setAttribute("address1_ad2", address[1]);
                req.setAttribute("address1_ad3", address[2]);
                System.out.println(address[0]);
            }
    	}
        req.setAttribute("member", dto);
    	return "client/member/member_update";
    }
    
    @RequestMapping(value = "/member_update.do", method = RequestMethod.POST)
    public String updateMember(HttpServletRequest req, 
			@ModelAttribute MemberDTO dto, BindingResult result) {
		if (result.hasErrors()) {
			dto.setMember_image("");
		}
		MultipartHttpServletRequest mr = (MultipartHttpServletRequest)req;
		MultipartFile mf = mr.getFile("member_image");
		String filename = mf.getOriginalFilename();
		String path = req.getServletContext().getRealPath("/resources/image");
		System.out.println(path);
		File file = new File(path, filename);
		
		if (filename == null || filename.trim().equals("")) {
			dto.setMember_image(req.getParameter("member_image2"));
			System.out.println(req.getParameter("member_image2"));
		}else {
			try {
				mf.transferTo(file);
			}catch(IOException e) {
				req.setAttribute("msg", "이미지 업로드 실패 : 다시 확인해 주세요");
				req.setAttribute("url", "");
				return "message";
			}
			dto.setMember_image(filename);
		}
		
		HttpSession session = req.getSession();
		String ad1 = req.getParameter("sample6_address");
		String ad2 = req.getParameter("sample6_detailAddress");
		String ad3 = req.getParameter("sample6_extraAddress");
		session.setAttribute("member_image", filename);
		dto.setMember_address1(ad1 + "/" + ad2 + "/" + ad3);
		dto.setMember_image(filename);
		int res = memberMapper.updateMember(dto);
		if (res>0) {
			req.setAttribute("msg", "회원 정보가 수정되었습니다");
			req.setAttribute("url", "");
		}else if (res<0){
			req.setAttribute("msg", "회원 정보 수정 실패");
			req.setAttribute("url", "redirect:member_update.do");
		}
    	return "message";
    }
    
    public boolean isValid(String str) {
        return Pattern.matches("^[a-zA-Z0-9-_]*$", str);
    }
    
    @RequestMapping("/member_checkId.do")
    @ResponseBody
    public String checkId(@RequestParam("member_id") String id) {
        String result="N";
        if (memberMapper.checkId(id) != null) result = "Y"; 	
        if (id.trim().equals("")) result = "E";					
        if (id.length() < 6 || id.length() > 12) result = "L";
        if (isValid(id) == false) result = "V";				
        
        return result;
    }
    
    @RequestMapping(value="/member_delete.do", method = RequestMethod.GET)
    public String memberDelete(HttpServletRequest req) {
    	return "client/member/member_delete";
    }
    
    @RequestMapping(value="/member_delete.do", method = RequestMethod.POST)
    public String memberdelete(HttpServletRequest req) {
    	String con = req.getParameter("confirmed");
    	MemberDTO dto = getMember(req);
    	int res = 0;
    	
    	if (con.equals("on")) {
    		res = memberMapper.deleteMember(dto);
    	}
    	
    	if (res >= 1){
    		HttpSession session = req.getSession();
    		session.invalidate();
        	req.setAttribute("msg", "회원 탈퇴 : 완료되었습니다");
        	req.setAttribute("url", "main.do");
    	}else {
        	req.setAttribute("msg", "회원 탈퇴 : 실패하였습니다");
        	req.setAttribute("url", "main.do");
    	}

    	return "message";
    }
}
