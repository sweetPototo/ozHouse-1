package com.oz.ozHouse.merchant;

import java.io.File;
import java.io.IOException;
import java.util.Map;

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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.oz.ozHouse.dto.MerchantDTO;
import com.oz.ozHouse.merchant.service.MyInformMapper;
import com.oz.ozHouse.merchant.BrandController;
import com.oz.ozHouse.merchant.bean.MerchantLoginBean;


@Controller
public class MyInformController {
	
	
	
	@Autowired
	private MyInformMapper myInformMapper;
	
	@RequestMapping(value="/myInform_view.do", method=RequestMethod.GET)
	public String myInform_view(HttpServletRequest req, int mer_num){
		HttpSession session = req.getSession();
		MerchantDTO dto = myInformMapper.myInformView(mer_num);
		String resultCate = null;
		if(dto.getMer_category()==null) {
			resultCate = "미등록";
		}else {
			String cate[] = dto.getMer_category().split(",");
			String category[] = new String[cate.length];
			for(int i=0; i<category.length; ++i) {
				category[i] = myInformMapper.selectCateName(Integer.valueOf(cate[i]));
			}
			resultCate = String.join(",", category);
		}
		session.setAttribute("resultCate", resultCate);
		session.setAttribute("merchantUpdate", dto);
		return "merchant/main/myInform_view";
	}
	
	@RequestMapping(value="/myInform_view.do", method=RequestMethod.POST)
	public String myInform_update(HttpServletRequest req, String mode) {
		req.setAttribute("mode", mode);
		return "merchant/main/myInform_updateCheck";
	}
	
	@RequestMapping(value="/myInform_update.do", method=RequestMethod.GET)
	public String myInform_updateForm(HttpServletRequest req, @ModelAttribute MerchantDTO dto) {
		return "merchant/main/myInform_update";
	}
	
	@RequestMapping(value="/myInform_update.do", method=RequestMethod.POST)
	public String myInform_updateFormOk(HttpServletRequest req, @ModelAttribute MerchantDTO dto,
			BindingResult result) throws IllegalStateException, IOException {
		String url = "myInform_view.do?mer_num="+dto.getMer_num();

        String ad1 = req.getParameter("sample6_address");
		String ad2 = req.getParameter("sample6_detailAddress");
		String ad3 = req.getParameter("sample6_extraAddress");
		dto.setMer_business_adress(ad1 + "/" + ad2 + "/" + ad3);
		
		String ole_business = req.getParameter("old_mer_business_registration");
		String ole_file = req.getParameter("old_mer_file");
		MultipartHttpServletRequest mr = (MultipartHttpServletRequest)req;
		
        MultipartFile business = mr.getFile("mer_business_registration");
        if (business == null || business.getSize()<=0) {
			dto.setMer_business_registration(ole_business);
		}else {
			String businessFileName = business.getOriginalFilename();
			File businessFile = new File(MerchantLoginController.BUSINESSFILEPATH + businessFileName);
			try {
				business.transferTo(businessFile);
			}catch(IOException e) {
				req.setAttribute("msg", "사업자등록증 수정 중 오류가 발생하였습니다.");
				req.setAttribute("url", url);
				return "forward:message.jsp";
			}
			File deleteFile = new File(MerchantLoginController.BUSINESSFILEPATH + ole_business);
			if (deleteFile.exists()){
				deleteFile.delete();
			}
			dto.setMer_business_registration(businessFileName);
		}
        
        MultipartFile file = mr.getFile("mer_file");
        if (file == null || file.getSize()<=0) {
			dto.setMer_file(ole_file);
		}else {
        	String filename = file.getOriginalFilename();
			File merFile = new File(BrandController.FILEPATH + filename);
			try {
				file.transferTo(merFile);
			}catch(IOException e) {
				req.setAttribute("msg", "상품판매 파일 수정 중 오류가 발생하였습니다.");
				req.setAttribute("url", url);
				return "forward:message.jsp";
			}
			File deleteFile = new File(BrandController.FILEPATH + ole_file);
			if (deleteFile.exists()){
				deleteFile.delete();
			}
			dto.setMer_file(filename);
		}
        
		int res = myInformMapper.updateMerchant(dto);
		if(res>0) {
			req.setAttribute("msg","정보 수정이 완료되었습니다.");
		}else {
			req.setAttribute("msg","정보 수정을 완료하지 못하였습니다.");
		}
		req.setAttribute("url", url);
		return "forward:message.jsp";
	}
	
	@RequestMapping(value="/myInform_updatePass.do", method=RequestMethod.GET)
	public String myInformUpdatePass() {
		return "merchant/main/myInform_updatePass";
	}
	
	@RequestMapping(value="/myInform_updatePass.do", method=RequestMethod.POST)
	public String myInformUpdatePassOk(HttpServletRequest req, @RequestParam Map<String, String> map) {
		int res = myInformMapper.updatePass(map);
		String msg = null, url = "myInform_view.do?mer_num="+map.get("mer_num");
		if(res>0) {
			msg = "비밀번호 변경이 완료되었습니다.";
			HttpSession session = req.getSession();
			MerchantLoginBean loginOk = (MerchantLoginBean)session.getAttribute("merchantLoginMember");
			loginOk.setMer_pw(map.get("mer_pw"));
		}else {
			msg = "비밀번호 변경 중 오류가 발생하였습니다.";
		}
		req.setAttribute("msg", msg);
		req.setAttribute("url", url);
		return "forward:message.jsp";
	}
	
	@RequestMapping(value="/memberOut.do", method=RequestMethod.GET)
	public String memberOut(HttpServletRequest req, HttpServletResponse resp, String mer_num) {
		int res = myInformMapper.memberOut(mer_num);
		String msg, url = null;
		if(res>0) {
			msg = "회원탈퇴가 완료되었습니다. 판매자님의 정보는 5년 후 폐기될 예정입니다.";
			url = "merchant_main.do";
			HttpSession session = req.getSession();
	    	session.invalidate();
	    	Cookie ck = new Cookie("saveId", null);
	    	ck.setMaxAge(0);
	    	resp.addCookie(ck); //    信  ߰  ؼ               
		}else {
			msg = "회원탈퇴 중 오류가 발생하였습니다.";
			url = "myInform_view.do?mer_num=" + mer_num;
		}
		req.setAttribute("msg", msg);
		req.setAttribute("url", url);
		return "forward:message.jsp";
	}

}
