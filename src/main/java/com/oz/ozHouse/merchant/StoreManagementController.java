package com.oz.ozHouse.merchant;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StoreManagementController {

	@RequestMapping(value="/store_Main.do")
	public String storeMain() {
		return "merchant/store/storeMain/storeMain_main";
	}
	
}
