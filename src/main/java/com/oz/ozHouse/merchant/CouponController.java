package com.oz.ozHouse.merchant;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.oz.ozHouse.dto.Mer_CouponDTO;
import com.oz.ozHouse.merchant.service.CouponMapper;

@Controller
public class CouponController {
	
	@Autowired
	private CouponMapper couponMapper;
	
	@RequestMapping(value = "/getCouponMessage.do", method = RequestMethod.GET, produces = "text/plain; charset=UTF-8")
	@ResponseBody
	public String getCouponMessage(HttpServletResponse resp, @RequestParam("mer_couponnum") int merCouponNum) {
	    resp.setCharacterEncoding("UTF-8");
	    System.out.println(merCouponNum);
	    String msg = couponMapper.mer_msgString(merCouponNum);
	    System.out.println(msg); 
	    return msg != null ? msg : "No message found";
	}

	@RequestMapping(value="/coupon_insert.do", method=RequestMethod.GET)
	public String couponInput() {
		return "merchant/store/coupon/coupon_insert";
	}
	
	@RequestMapping(value="/coupon_insert.do", method=RequestMethod.POST)
	public ModelAndView couponInputOk(@ModelAttribute Mer_CouponDTO dto, HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		if (dto.getMer_couponimage()==null) {
	    dto.setMer_couponimage("0");
		}
		int res = couponMapper.mer_couponInsert(dto);
		ModelAndView mav = new ModelAndView("forward:message.jsp");
		String msg = null;
		String url = null;
		if (res > 0) {
			msg = "������Ͻ�û�� �Ϸ�Ǿ����ϴ�.";
			url = "coupon_list.do?mer_num=" + dto.getMer_num();
		}else {
			msg = "������Ͻ�û�� �Ϸ���� �ʾҽ��ϴ�.";
			url = "coupon_insert.do";
		}
		mav.addObject("msg", msg);
		mav.addObject("url", url);
		return mav;
	}
	
	@RequestMapping(value="/coupon_list.do", method=RequestMethod.GET)
	public String couponList(int mer_num, HttpServletRequest req) {
		List<Mer_CouponDTO> list = couponMapper.mer_couponList(mer_num);
		int couponCount = couponMapper.mer_couponCount(mer_num);
		req.setAttribute("checkOptions", settinOption("all"));
		req.setAttribute("radioOptions", settinRadio("all"));
		req.setAttribute("dateOptions", dateOption("mer_couponusedate"));
		req.setAttribute("mer_num", mer_num);
		req.setAttribute("couponList", list);
		req.setAttribute("couponCount", couponCount);
		return "merchant/store/coupon/coupon_list";
	}
	
	@RequestMapping(value="/coupon_listSearch.do", method=RequestMethod.POST)
	public String couponListSearch(@RequestParam Map<String, String> map, HttpServletRequest req) {
		try {
			char a = map.get("startDate").charAt(0);
			char b = map.get("endDate").charAt(0);
		}catch(StringIndexOutOfBoundsException e) {
			map.put("startDate", null);
			map.put("endDate", null);
		}
		List<Mer_CouponDTO> list;
		list = couponMapper.mer_searchCouponList(map);
		int couponCount = couponMapper.mer_couponSearchCount(map);
		req.setAttribute("checkOptions", settinOption(map.get("search")));
		req.setAttribute("radioOptions", settinRadio(map.get("mer_isapproval")));
		req.setAttribute("dateOptions", dateOption("mer_couponusedate"));
		req.setAttribute("couponList", list);
		req.setAttribute("couponCount", couponCount);
		req.setAttribute("map", map);
		return "merchant/store/coupon/coupon_list";
	}
	
	@RequestMapping(value="/coupon_delete.do")
	public ModelAndView couponDelete(@RequestParam int mer_couponnum, int mer_num) {
		int res = couponMapper.mer_couponDelete(mer_couponnum);
		couponMapper.mer_deletemsg(mer_couponnum);
		ModelAndView mav = new ModelAndView("forward:message.jsp");
		String msg = null;
		String url = "coupon_list.do?mer_num=" + mer_num;
		if (res > 0) {
			msg = "���������� �Ϸ�Ǿ����ϴ�.";
		}else {
			msg = "���������� �Ϸ���� �ʾҽ��ϴ�.";
		}
		mav.addObject("msg", msg);
		mav.addObject("url", url);
		return mav;
	}
	
	private String settinOption(String search) {
		String options = "";
		if(search.equals("all")) {
			options = "<option value=\"all\" selected='selected'>��ü</option>"
				 	+ "<option value=\"mer_couponname\">�����̸�</option>\n"
				 	+ "<option value=\"mer_couponnum\">������ȣ</option>\n";
		}else if(search.equals("mer_couponname")) {
			options = "<option value=\"all\">��ü</option>"
				 	+ "<option value=\"mer_couponname\" selected='selected'>�����̸�</option>\n"
				 	+ "<option value=\"mer_couponnum\">������ȣ</option>\n";
		}else if(search.equals("mer_couponnum")) {
			options = "<option value=\"all\">��ü</option>"
				 	+ "<option value=\"mer_couponname\">�����̸�</option>\n"
				 	+ "<option value=\"mer_couponnum\" selected='selected'>������ȣ</option>\n";
		}
		return options;
	}
	
	private String settinRadio(String mer_isapproval) {
		System.out.println(mer_isapproval);
		String options = "";
		if(mer_isapproval.equals("all")) {
			options = "<input type=\"radio\" name=\"mer_isapproval\" value=\"all\" checked=\"checked\">��ü" + 
					"<input type=\"radio\" name=\"mer_isapproval\" value=\"t\">���οϷ�" + 
					"<input type=\"radio\" name=\"mer_isapproval\" value=\"f\">������" + 
					"<input type=\"radio\" name=\"mer_isapproval\" value=\"c\">���ΰ���";
		}else if(mer_isapproval.equals("t")) {
			options = "<input type=\"radio\" name=\"mer_isapproval\" value=\"all\">��ü" + 
					"<input type=\"radio\" name=\"mer_isapproval\" value=\"t\" checked=\"checked\">���οϷ�" + 
					"<input type=\"radio\" name=\"mer_isapproval\" value=\"f\">������" + 
					"<input type=\"radio\" name=\"mer_isapproval\" value=\"c\">���ΰ���";
		}else if(mer_isapproval.equals("f")) {
			options = "<input type=\"radio\" name=\"mer_isapproval\" value=\"all\">��ü" + 
					"<input type=\"radio\" name=\"mer_isapproval\" value=\"t\">���οϷ�" + 
					"<input type=\"radio\" name=\"mer_isapproval\" value=\"f\" checked=\"checked\">������" + 
					"<input type=\"radio\" name=\"mer_isapproval\" value=\"c\">���ΰ���";
		}else if(mer_isapproval.equals("c")) {
			options = "<input type=\"radio\" name=\"mer_isapproval\" value=\"all\">��ü" + 
					"<input type=\"radio\" name=\"mer_isapproval\" value=\"t\">���οϷ�" + 
					"<input type=\"radio\" name=\"mer_isapproval\" value=\"f\">������" + 
					"<input type=\"radio\" name=\"mer_isapproval\" value=\"c\" checked=\"checked\">���ΰ���";
		}
		return options;
	}

	private String dateOption(String search) {
		String options = "";
		if(search.equals("mer_couponusedate")) {
			options = "<option value=\"mer_couponusedate\" selected='selected'>��������</option>"
				 	+ "<option value=\"mer_couponenddate\">���������</option>\n";
		}else if(search.equals("mer_couponenddate")) {
			options = "<option value=\"mer_couponusedate\">��������</option>\n"
				 	+ "<option value=\"mer_couponenddate\" selected='selected'>���������</option>\n";
		}
		return options;
	}
}
