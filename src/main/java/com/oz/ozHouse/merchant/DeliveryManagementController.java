package com.oz.ozHouse.merchant;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.oz.ozHouse.dto.ListDTO;
import com.oz.ozHouse.merchant.service.DeliveryMapper;

@Controller
public class DeliveryManagementController {
	
	@Autowired
	private DeliveryMapper deliveryMapper;

	//�뜝�뙏�뙋�삕 �뜝�룞�삕�뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕泥� �뜝�룞�삕�뜝�룞�삕�듃 �뜝�떛源띿삕
	@RequestMapping(value="/deliveryList.do", method=RequestMethod.GET)
	public String returnCancelList(@RequestParam Map<String, String> map, HttpServletRequest req) {
		List<ListDTO> list = null;
		int deliveryCount;
		if(map.get("mode").equals("all")) {
			list = deliveryMapper.deliveryList(map);
			deliveryCount = deliveryMapper.countDelivery(map);
		}else {
			list = deliveryMapper.deliveryLikeList(map);
			deliveryCount = deliveryMapper.countDeliveryLike(map);
		}
		
		String options = settinOption("all");
		req.setAttribute("deliveryCount", deliveryCount);
		req.setAttribute("options", options);
		req.setAttribute("deliveryList", list);
		req.setAttribute("map", map);
		return "merchant/store/delivery/delivery_list";
	}
	
	@RequestMapping(value="/delivery_listSearch.do", method=RequestMethod.POST)
	public String returnCancelListSearch(@RequestParam Map<String, String> map, HttpServletRequest req) {
		try {
			char a = map.get("startDate").charAt(0);
			char b = map.get("endDate").charAt(0);
		}catch(StringIndexOutOfBoundsException e) {
			map.put("startDate", null);
			map.put("endDate", null);
		}
		req.setAttribute("map", map);
		List<ListDTO> list = deliveryMapper.searchDeliveryList(map);
		System.out.println("map.get(\"search\") : " + map.get("search"));
		String options = settinOption(map.get("search"));
		int deliveryCount = deliveryMapper.countSearchDelivery(map);
		req.setAttribute("deliveryCount", deliveryCount);
		req.setAttribute("options", options);
		req.setAttribute("deliveryList", list);
	
		return "merchant/store/delivery/delivery_list";
	}
	
	private String settinOption(String search) {
		System.out.println("search : " + search);
		String options = "";
		if(search.equals("all")) {
			options = "<option value=\"all\" selected='selected'>전체</option>"
				 	+ "<option value=\"member_id\">id</option>\n"
				 	+ "<option value=\"product_name\">상품명</option>\n"
				 	+ "<option value=\"order_num\">주문번호</option>";
		}else if(search.equals("member_id")) {
			options = "<option value=\"all\">전체</option>"
				 	+ "<option value=\"member_id\" selected='selected'>id</option>\n"
				 	+ "<option value=\"product_name\">상품명</option>\n"
				 	+ "<option value=\"order_num\">주문번호</option>";
		}else if(search.equals("product_name")) {
			options = "<option value=\"all\">전체</option>"
				 	+ "<option value=\"member_id\">id</option>\n"
				 	+ "<option value=\"product_name\" selected='selected'>상품명</option>\n"
				 	+ "<option value=\"order_num\">주문번호</option>";
		}else if(search.equals("order_num")) {
			options = "<option value=\"all\"전체</option>"
				 	+ "<option value=\"member_id\">id</option>\n"
				 	+ "<option value=\"product_name\">상품명</option>\n"
				 	+ "<option value=\"order_num\" selected='selected'>주문번호</option>";
		}
		return options;
	}
}
