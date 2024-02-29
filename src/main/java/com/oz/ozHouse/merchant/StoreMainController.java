package com.oz.ozHouse.merchant;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oz.ozHouse.dto.NoticeDTO;
import com.oz.ozHouse.merchant.bean.MerchantLoginBean;
import com.oz.ozHouse.merchant.service.StoreMainMapper;

@Controller
public class StoreMainController {
	
	@Autowired
	private StoreMainMapper storeMainMapper;
	
	@RequestMapping("/main_storeManagement.do")
	public String home(HttpServletRequest req, @RequestParam Map<String, String> map) {
		HttpSession session = req.getSession();
		MerchantLoginBean loginOk = (MerchantLoginBean)session.getAttribute("merchantLoginMember");
		if (loginOk == null) {
			String msg = "로그인을 먼저 진행하신 후 이용 바랍니다.";
			String url = "merchant_login.do";
			req.setAttribute("msg", msg);
			req.setAttribute("url", url);
			return "forward:message.jsp";
		}
		if(loginOk.getMer_isbrand().equals("f")) {
			String msg = "입점이 승인된 상점만 이용 가능합니다.";
			String url = "merchant_main.do";
			req.setAttribute("msg", msg);
			req.setAttribute("url", url);
			return "forward:message.jsp";
		}
		
		int count = storeMainMapper.allCount(map);
		req.setAttribute("allCount", count);
		
		int waitCount = storeMainMapper.allCount(map);
		req.setAttribute("product_approval_status", map.get("product_approval_status"));
		req.setAttribute("waitCount", waitCount);
		
		int requestCount = storeMainMapper.requestCount(map);
		req.setAttribute("requestCount",  requestCount);
		
		int cancleCount = storeMainMapper.cancleCount(map);
		req.setAttribute("cancleCount", cancleCount);
		
		int cancleRequest = storeMainMapper.requestCancle(map);
		req.setAttribute("requestCancle", cancleRequest);
		
		int saleOk = storeMainMapper.saleOk(map);
		req.setAttribute("saleOk",  saleOk);
		
		List<NoticeDTO> list = storeMainMapper.noticeTitleList();
		req.setAttribute("noticeTitleList", list);
		
		int boardCount = storeMainMapper.productCount(map)
				+ storeMainMapper.orderCount(map);
		req.setAttribute("boardCount", boardCount);
		
		int exchangeCount = storeMainMapper.exchangeCount(map);
		req.setAttribute("exchangeCount", exchangeCount);
		
		int returnCount = storeMainMapper.returnCount(map);
		req.setAttribute("returnCount", returnCount);
		
		int readyCount = storeMainMapper.readyCount(map);
		req.setAttribute("readyCount", readyCount);
		
		int deliveryCount = storeMainMapper.deliveryCount(map);
		req.setAttribute("deliveryCount",  deliveryCount);
		
		int completeCount = storeMainMapper.completeCount(map);
		req.setAttribute("completeCount",  completeCount);
		
		return "merchant/store/storeMain/storeManagementMain";
	}
	
}
