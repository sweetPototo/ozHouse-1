package com.oz.ozHouse.merchant;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.oz.ozHouse.dto.MerchantDTO;
import com.oz.ozHouse.dto.NoticeDTO;
import com.oz.ozHouse.merchant.bean.MerchantLoginBean;
import com.oz.ozHouse.merchant.service.MerchantMainMapper;

@Controller
public class MerchantMainController {
	
	@Autowired
	private MerchantMainMapper mainMapper;
	
	@RequestMapping(value= {"/", "/merchant_main.do"})
	public String mainHome(HttpServletRequest req) {
		List<NoticeDTO> list = mainMapper.noticeMainList();
		req.setAttribute("noticeMainList", list);
		return "merchant/main/main";
	}
	
	@RequestMapping("/notice.do")
	public String notice(HttpServletRequest req) {
		List<NoticeDTO> list = mainMapper.noticeList();
		req.setAttribute("noticeList", list);
		return "merchant/main/notice";
	}
	
	@RequestMapping("/notice_title.do")
	public String notice_title(HttpServletRequest req, int notice_num) {
		List<NoticeDTO> list = mainMapper.noticeDetail(notice_num);
		req.setAttribute("noticeDetail", list);
		return "merchant/main/notice_detail";
	}
	
	@RequestMapping("/findNotice.do")
	public String findNotice(HttpServletRequest req, @RequestParam("search") String search) {
		List<NoticeDTO> list = mainMapper.findNotice(search);
		req.setAttribute("noticeList", list);
		return "merchant/main/notice";
	}
}
