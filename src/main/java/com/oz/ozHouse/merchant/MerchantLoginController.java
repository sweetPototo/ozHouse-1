package com.oz.ozHouse.merchant;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.oz.ozHouse.dto.CategoryDTO;
import com.oz.ozHouse.dto.MerchantDTO;
import com.oz.ozHouse.merchant.service.MerchantLoginMapper;
import com.oz.ozHouse.merchant.service.MyInformMapper;
import com.oz.ozHouse.merchant.service.ProductManagementMapper;
import com.oz.ozHouse.merchant.bean.*;


@Controller
public class MerchantLoginController {
	
	static final String BUSINESSFILEPATH = 
			"C:\\Users\\moonj\\Desktop\\ozHouse\\ozHouse\\src\\main\\webapp\\resources\\merchant\\business";
	
	@Autowired
	private MerchantLoginMapper merchantMapper;
	
	@Autowired
	private MyInformMapper myInformMapper;
	
	@RequestMapping(value="/merchant_login.do", method=RequestMethod.GET)
	public String login() {
		return "merchant/join/merchant_login";
	}
	@RequestMapping(value="/merchant_login.do", method=RequestMethod.POST)
	public String loginOk(HttpServletRequest req, HttpServletResponse resp, 
			@ModelAttribute MerchantLoginBean loginOk, @RequestParam(required=false) String saveId) {
		int res = loginOk.loginOk(merchantMapper);
		String msg = null, url = null;
		switch(res){
		case MerchantLoginBean.OK :
			Cookie ck = new Cookie("saveId", loginOk.getMer_id());
			if (saveId != null) {
				ck.setMaxAge(7*24*60*60);
			}else {
				ck.setMaxAge(0);
			}
			resp.addCookie(ck);
			HttpSession session = req.getSession();
			session.setAttribute("merchantLoginMember", loginOk);
			msg = loginOk.getMer_id() + "님, OZ의 집에 오신걸 환영합니다.";
			url = "merchant_main.do";
			break;
		case MerchantLoginBean.NOT_ID :
			msg = "아이디 확인 후 다시 시도해 주세요";
			url = "merchant_login.do";
			break;
		case MerchantLoginBean.NOT_PW :
			msg = "비밀번호 확인 후 다시 시도해 주세요";
			url = "merchant_login.do";
			break;
		case MerchantLoginBean.ERROR : 
			msg = "DB 접속 오류! 관리자에게 문의해 주세요";
			url = "merchant_main.do";
			break;
		case MerchantLoginBean.DELETE_ID : 
			msg = "사용 중지된 ID입니다.";
			url = "merchant_main.do";
			break;
		}
		req.setAttribute("msg", msg);
		req.setAttribute("url", url);
    	return "forward:message.jsp";
	}
	
	@RequestMapping(value="/merchant_join.do", method=RequestMethod.GET)
	public String join() {
		return "merchant/join/merchant_join";
	}
	
	@RequestMapping(value="/merchant_send_email.do")
    public String merchantEmailAuth(HttpServletRequest req, @ModelAttribute MerchantDTO dto, 
    		BindingResult result) throws IllegalStateException, IOException {  //dto 뿉 MultipertFile 쓣 諛쏅뒗 怨쇱젙 뿉 꽌 BindingException 諛쒖깮, BindingResult濡   옟 쓬
		Map<String, String> comNum = new HashMap<String, String>();
		comNum.put("mer_comnum1", dto.getMer_comnum1());
    	comNum.put("mer_comnum2", dto.getMer_comnum2());
    	comNum.put("mer_comnum3", dto.getMer_comnum3());
    	MerchantDTO check = merchantMapper.merchant_checkBsNum(comNum);
    	if(check != null) {
    		String msg = "이미 가입된 사업자등록번호 입니다.";
    		String url = "merchant_login.do";
    		req.setAttribute("msg", msg);
			req.setAttribute("url", url);
    		return "message";
    	}
		String email = req.getParameter("mer_email");
        if (merchantMapper.merchant_checkEmail(email) != null) {
			req.setAttribute("msg", "이미 가입되어있는 이메일주소입니다.");
			req.setAttribute("url", "merchant_join.do");
			return "message";
        }
        
        HttpSession session = req.getSession();
		MultipartHttpServletRequest mr = (MultipartHttpServletRequest)req;
        MultipartFile mFile = mr.getFile("mer_business_registration");
    	if (mFile != null && mFile.getSize() > 0) {
    		String saveName = mFile.getOriginalFilename();
    		File file = new File(BUSINESSFILEPATH + saveName);
    		mFile.transferTo(file); //             
	        session.setAttribute("saveName", saveName);
    	}else {
    		req.setAttribute("msg", "회원가입 실패 : 사업자등록증 전송 중 오류가 발생하였습니다.");
			req.setAttribute("url", "merchant_main.do");
			return "message";
        }
        
        String ad1 = req.getParameter("sample6_address");
		String ad2 = req.getParameter("sample6_detailAddress");
		String ad3 = req.getParameter("sample6_extraAddress");
		dto.setMer_business_adress(ad1 + "/" + ad2 + "/" + ad3);
        
        int num = TSL.sendEmailCheck(email);
        String checkNum = Integer.toString(num);
        req.setAttribute("checkNum", checkNum);
        req.setAttribute("email", email);
        session.setAttribute("insertMerchant", dto);
        return "merchant/join/merchant_join_check";
    }
	
	@RequestMapping("/merchant_email_join_check.do")
    public String emailAuthCheck(HttpServletRequest req, @RequestParam Map<String, String> params) 
    		throws IllegalStateException, IOException {
		HttpSession session = req.getSession();
		String saveName = (String)session.getAttribute("saveName");
		File deleteFile = new File(BUSINESSFILEPATH + saveName);
    	if (params.get("checkNum").equals(params.get("checkNumCheck"))) {
        	MerchantDTO dto = (MerchantDTO)session.getAttribute("insertMerchant");
//        	String passwd = dto.getMer_pw();
//        	dto.setMer_pw(passwordEncoder.encode(passwd));
        	
    		dto.setMer_business_registration(saveName);	
        	int res = merchantMapper.insertMerchant(dto);
        	if (res>0) {
    			req.setAttribute("msg", "회원가입 성공 : 로그인해 주시기 바랍니다.");
    			req.setAttribute("url", "merchant_main.do");
    		}else if (res<0){
    			req.setAttribute("msg", "회원가입 실패 : 회원가입시 오류가 발생하였습니다. 관리자에게 문의하여주세요");
    			req.setAttribute("url", "forward:merchant_main.do");
    			if (deleteFile.exists()){
    				deleteFile.delete();
    			}
    		}
        	return "message";
        	
        }else {
			req.setAttribute("msg", "인증번호가 올바르지 않습니다.");
			req.setAttribute("url", "merchant_join.do");
			if (deleteFile.exists()){
				deleteFile.delete();
			}
        	return "message";
        }
    }
	
    public boolean isValid(String str) {
        // 占쎌젟域뱀뮉紐댐옙 겱占쎈뻼占쎌뱽 占쎄텢占쎌뒠占쎈릭占쎈연 占쎌겫 눧占 , 占쎈떭占쎌쁽, - 占쎌굢占쎈뮉 _揶쏉옙 占쎈툡占쎈빒  눧紐꾩쁽揶쏉옙 占쎈７占쎈맙占쎈┷占쎈선 占쎌뿳占쎈뮉筌욑옙 野껓옙占쎄텢
        return Pattern.matches("^[a-zA-Z0-9-_]*$", str);
    }
	
    @RequestMapping("/mer_checkId.do")
    @ResponseBody
    public String checkId(@RequestParam("mer_id") String id) {
        String result="N";
        if (merchantMapper.merchant_checkMerId(id) != null) result = "Y"; 	// 占쎈툡占쎌뵠占쎈탵 占쎄텢占쎌뒠  겫 뜃占쏙옙 뮟
        if (id.trim().equals("")) result = "E";					// 占쎈툡占쎌뵠占쎈탵  뜮袁⑸선 占쎌뿳占쎌뱽 占쎈르
        if (id.length() < 5 || id.length() > 12) result = "L";	// length 占쎌궎 몴占 
        if (isValid(id) == false) result = "V";					//  눧紐꾩쁽占쎈였 野껓옙占쎄텢
        return result;
    }
    
    @RequestMapping(value="/merchant_logout.do")
    public String logout(HttpServletRequest req) {
    	HttpSession session = req.getSession();
    	session.invalidate();
    	String msg = "로그아웃 되었습니다.";
    	String url = "merchant_main.do";
    	req.setAttribute("msg", msg);
    	req.setAttribute("url", url);
    	return "forward:message.jsp";
    }
    
    @RequestMapping(value="/merchant_find.do", method=RequestMethod.GET)
	public String searchMember() {
		return "merchant/join/merchant_find";
	}
    
 //  씠硫붿씪  삎 떇 寃 利 
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";
    
    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    
    @RequestMapping(value="/merchant_send_find_email.do", method=RequestMethod.POST)
	public String sendEmail1(HttpServletRequest req, String mer_email) {
    	String mer_id = merchantMapper.checkMerchantIdEmail(mer_email);
    	if (mer_id == null) {
	         req.setAttribute("msg", "가입되어 있지 않은 아이디입니다");
	         req.setAttribute("url", "merchant_find.do");
	         return "message";
	    }

    	int oauthNum = MemberFind.sendEmailCheck(mer_email, mer_id);
    	req.setAttribute("oauthNum", oauthNum);
    	req.setAttribute("member_email", mer_email);
		return "merchant/join/merchant_send_find_email";
    }
    
    @RequestMapping(value="/merchant_find.do", method=RequestMethod.POST)
	public String searchMember(HttpServletRequest req, String mer_email) {
		String member_id = merchantMapper.checkMerchantIdEmail(mer_email);
		
		int oauthNum = Integer.parseInt(req.getParameter("oauthNum"));
		int userSendNum = Integer.parseInt(req.getParameter("userSendNum"));
		
		if (oauthNum == userSendNum) {
			req.setAttribute("msg", "비밀번호를 재설정해 주세요");
			req.setAttribute("url", "merchant_changePass.do?mode=find&id=" + member_id);
		}else {
			req.setAttribute("msg", "인증 번호가 다릅니다 : 다시 시도해 주세요");
			req.setAttribute("url", "merchant_find.do");
		}
		return "message";
	}
    
    @RequestMapping(value = "merchant_changePass.do", method = RequestMethod.GET)
    public String mypage_updatePasswd(HttpServletRequest req) {
    	String mode = req.getParameter("mode");
    	
    	if (mode != null) {
    		req.setAttribute("mode", "find");
    		req.setAttribute("member_id", req.getParameter("id"));
    	}
        return "merchant/join/merchant_changePass";
    }
    
    private MerchantDTO getMember(HttpServletRequest req) {
    	HttpSession session = req.getSession();
    	MerchantLoginBean merchantLoginMember = (MerchantLoginBean)session.getAttribute("merchantLoginMember");
    	MerchantDTO dto = merchantMapper.merchant_getMember(merchantLoginMember.getMer_id());
    	return dto;
    }
    
    @RequestMapping(value = "merchant_changePass.do", method = RequestMethod.POST)
    public String mypage_updatePasswdPro(HttpServletRequest req) {
    	MerchantDTO dto = new MerchantDTO();
    	String mode = req.getParameter("mode");
    	String id = req.getParameter("member_id");
    	String new_pass = req.getParameter("new_member_passwd");
    	
        boolean passwd = false;
        
        if (!mode.equals("find")) {
        	dto = getMember(req);
            String old_pass = req.getParameter("member_passwd");
        	//passwd = passwordEncoder.matches(old_pass, dto.getMember_passwd());
        }else if(mode.equals("find")) {
        	passwd = true;
        	dto.setMer_id(id);
        }
    	
    	if (passwd) {
    		dto.setMer_pw(new_pass);
    		Map <String, String> map = new HashMap<String, String>();
    		map.put("mer_id", dto.getMer_id());
    		map.put("mer_pw", dto.getMer_pw());
        	int res = myInformMapper.updatePass(map);
        	if (res>0) {
    			req.setAttribute("msg", "비밀번호가 업데이트 되었습니다");
    			req.setAttribute("url", "merchant_login.do");
    		}else if (res<0){
    			req.setAttribute("msg", "비밀번호 업데이트 실패 : 수정하실 비밀번호를 확인해 주세요");
    			req.setAttribute("url", "merchant_login.do");
    		}
    	}else {
    		req.setAttribute("msg", "비밀번호 업데이트 실패 : 다시 확인해 주세요");
			req.setAttribute("url", "merchant_login.do");
    	}
    	
        return "message";
    }
}
