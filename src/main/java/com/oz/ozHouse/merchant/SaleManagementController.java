package com.oz.ozHouse.merchant;


import java.time.LocalDateTime;
import java.time.Period;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oz.ozHouse.dto.ListDTO;
import com.oz.ozHouse.merchant.service.SalesMapper;

@Controller
public class SaleManagementController {

	@Autowired
	private SalesMapper salesMapper;
	
	private Calendar cal = Calendar.getInstance();
	
	@RequestMapping(value="/sales_list.do")
	public String salesList(@RequestParam Map<String, String> map, HttpServletRequest req) {
		List<ListDTO> list = null;
		//�뜝�떦釉앹삕�뜝�룞�삕�쉶
		if(map.get("mode").equals("day")) {
			if(!map.containsKey("startDate") && !map.containsKey("endDate")) {
				String today = cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.DATE);
				today = today.substring(2);
				map.put("startDate", today);
				map.put("endDate", today);
			}
			list = salesMapper.selectListDay(map);
		//�뜝�룞�삕�뜝�룞�삕�뜝�룞�삕�쉶
		}else if(map.get("mode").equals("month")) {
			Map options = selectMonth(map.get("sYear"), map.get("sMonth"),map.get("eYear"), map.get("eMonth"));  //jsp �뜝�룞�삕�뜝�룞�삕
			Map date = settingMonth(map.get("sYear"), map.get("sMonth"),map.get("eYear"), map.get("eMonth"));		//query �뜝�룞�삕�뜝�룞�삕
			map.put("startDate", (String)date.get("startDate"));
			map.put("endDate", (String)date.get("endDate"));
			list = salesMapper.selectListMonth(map);
			req.setAttribute("options", options);
		//�뜝�룞�삕�뜝�룞�삕�뜝�룞�삕�쉶
		}else {
			Map options = selectYear(map.get("sYear"), map.get("eYear"));
			Map date = settingYear(map.get("sYear"),map.get("eYear"));
			map.put("startYear", (String)date.get("startYear"));
			map.put("endYear", (String)date.get("endYear"));
			list = salesMapper.selectListYear(map);
			req.setAttribute("options", options);
		}
		req.setAttribute("map", map);
		req.setAttribute("salesList", list);
		return "merchant/store/sales/sales_list";
	}
	
	//�뜝�룞�삕�뜝�룞�삕�뜝占� �뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕, �뜝�룞�삕�뜝�룞�삕�뜝占� �뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕 jsp�뜝�룞�삕�뜝�떦�슱�삕 �뜝�룞�삕�뜝�룞�삕
	private Map selectMonth (String sYear, String sMonth, String eYear, String eMonth) {  
		Map <String, String> options = new HashMap<String, String>();
		int nowYear = cal.get(Calendar.YEAR);		//�뜝�룞�삕�뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕
		int nowMonth = cal.get(Calendar.MONTH)+1;	//�뜝�룞�삕�뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕
		String oldDate = salesMapper.month();
		int startYear = Integer.parseInt(oldDate.substring(0,2))+2000;
		int settingYear = startYear;
		int startMonth = Integer.parseInt(oldDate.substring(3));
		int endYear = nowYear;
		int endMonth = nowMonth;
		
		if(sYear != null && sMonth != null) {
			startYear = Integer.parseInt(sYear);	//�뜝�룞�삕�뜝�떆�슱�삕�뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕�뜝�룞�삕 int�뜝�룞�삕 �뜝�뙐�먯삕�뜝�뙇源띿삕
			startMonth = Integer.parseInt(sMonth);
		}
		if(eYear != null && eMonth != null) {
			endYear = Integer.parseInt(eYear);	//�뜝�룞�삕�뜝�떆�슱�삕�뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕�뜝�룞�삕 int�뜝�룞�삕 �뜝�뙐�먯삕�뜝�뙇源띿삕
			endMonth = Integer.parseInt(eMonth);
		}
		
		//option �뜝�룞�삕�뜝�룞�삕�뜝�룞�삕�뜝�뙇�뙋�삕 �뜝�룞�삕�뜝占�
		String syOptions = "";  //�뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕
		for(int year=settingYear; year<=nowYear; ++year) {
			if(sYear == null && year == startYear) {		//泥섇뜝�룞�삕 李썲뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕
				syOptions += "<option value ='" + year + "' selected='selected'>" + year + "년</option>";
			}else if(sYear != null && year == startYear) {
				syOptions += "<option value ='" + year + "' selected='selected'>" + year + "년</option>";
			}else {
				syOptions += "<option value ='" + year + "'>" + year + "년</option>";
			}
		}
		String smOptions = "";  //�뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕
		for(int month=1; month<=12; ++month) {
			if(sMonth == null && month == startMonth) {		//泥섇뜝�룞�삕 李썲뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕
				smOptions += "<option value ='" + month + "' selected='selected'>" + month + "월</option>";
			}else if(sMonth != null && month == startMonth) {
				smOptions += "<option value ='" + month + "' selected='selected'>" + month + "월</option>";
			}else {
				smOptions += "<option value ='" + month + "'>" + month + "월</option>";
			}
		}
		String eyOptions = "";  //�뜝�룞�삕�뜝�룞�삕 �뜝�뙇�룞�삕 �뜝�룞�삕�뜝�룞�삕
		for(int year=settingYear; year<=nowYear; ++year) {
			if(sYear == null && year == nowYear) {		//泥섇뜝�룞�삕 李썲뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕
				eyOptions += "<option value ='" + year + "' selected='selected'>" + year + "년</option>";
			}else if(sYear != null && year == endYear) {
				eyOptions += "<option value ='" + year + "' selected='selected'>" + year + "년</option>";
			}else {
				eyOptions += "<option value ='" + year + "'>" + year + "년</option>";
			}
		}
		String emOptions = "";  //�뜝�룞�삕�뜝�룞�삕 �뜝�뙇�룞�삕 �뜝�룞�삕
		for(int month=1; month<=12; ++month) {
			if(sMonth == null && month == nowMonth) {		//泥섇뜝�룞�삕 李썲뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕
				emOptions += "<option value ='" + month + "' selected='selected'>" + month + "월</option>";
			}else if(sMonth != null && month == endMonth) {
				emOptions += "<option value ='" + month + "' selected='selected'>" + month + "월</option>";
			}else {
				emOptions += "<option value ='" + month + "'>" + month + "월</option>";
			}
		}
		options.put("syOptions", syOptions);
		options.put("smOptions", smOptions);
		options.put("eyOptions", eyOptions);
		options.put("emOptions", emOptions);
		return options;
	}
	
	private Map selectYear (String sYear, String eYear) {  
		Map <String, String> options = new HashMap<String, String>();
		int nowYear = cal.get(Calendar.YEAR);		//�뜝�룞�삕�뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕
		String oldDate = salesMapper.month();
		int startYear = Integer.parseInt(oldDate.substring(0,2))+2000;
		int settingsYear = startYear;
		int endYear = nowYear;
		
		if(sYear != null) {
			startYear = Integer.parseInt(sYear);	//�뜝�룞�삕�뜝�떆�슱�삕�뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕�뜝�룞�삕 int�뜝�룞�삕 �뜝�뙐�먯삕�뜝�뙇源띿삕
		}
		if(eYear != null) {
			endYear = Integer.parseInt(eYear);	//�뜝�룞�삕�뜝�떆�슱�삕�뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕�뜝�룞�삕 int�뜝�룞�삕 �뜝�뙐�먯삕�뜝�뙇源띿삕
		}
		String syOptions = "";  //�뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕
		for(int year=settingsYear; year<=nowYear; ++year) {
			if(sYear == null && year == startYear) {		//泥섇뜝�룞�삕 李썲뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕
				syOptions += "<option value ='" + year + "' selected='selected'>" + year + "년</option>";
			}else if(sYear != null && year == startYear) {
				syOptions += "<option value ='" + year + "' selected='selected'>" + year + "년</option>";
			}else {
				syOptions += "<option value ='" + year + "'>" + year + "년</option>";
			}
		}
		String eyOptions = "";  //�뜝�룞�삕�뜝�룞�삕 �뜝�뙇�룞�삕 �뜝�룞�삕�뜝�룞�삕
		for(int year=settingsYear; year<=nowYear; ++year) {
			if(sYear == null && year == nowYear) {		//泥섇뜝�룞�삕 李썲뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕
				eyOptions += "<option value ='" + year + "' selected='selected'>" + year + "월</option>";
			}else if(sYear != null && year == endYear) {
				eyOptions += "<option value ='" + year + "' selected='selected'>" + year + "월</option>";
			}else {
				eyOptions += "<option value ='" + year + "'>" + year + "월</option>";
			}
		}
		options.put("syOptions", syOptions);
		options.put("eyOptions", eyOptions);
		return options;
	}
	
	//�뜝�룞�삕�뜝�룞�삕�뜝�룞�삕�쉶�뜝�룞�삕 �뜝�룞�삕�쉶�뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕,�뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕 -> input �뜝�룞�삕�뜝�룞�삕
		private Map settingMonth(String sYear, String sMonth, String eYear, String eMonth) {
			Map <String, String> date = new HashMap<String, String>();
			if(sYear==null&&sMonth==null&&eYear==null&&eMonth==null) {
				String oldDate = salesMapper.month();  // �뜝�룞�삕�뜝�룞�삕�뜝占� : 22/11
				String endYear = String.valueOf(cal.get(Calendar.YEAR)-2000);
				String endMonth = String.valueOf(cal.get(Calendar.MONTH)+1);
				if(endMonth.length()==1) endMonth = "0" + endMonth;
				date.put("startDate", oldDate);
				date.put("endDate", endYear + "/" + endMonth);
			}else {
				date.put("startDate", sYear.substring(2) + "/" + sMonth);
				date.put("endDate", eYear.substring(2) + "/" + eMonth);
			}
			return date;
		}
	
	//�뜝�룞�삕�뜝�룞�삕�뜝�룞�삕�쉶�뜝�룞�삕 �뜝�룞�삕�쉶�뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕 �뜝�룞�삕�뜝�룞�삕 -> input �뜝�룞�삕�뜝�룞�삕
	private Map settingYear(String sYear,String eYear) {
		Map <String, String> date = new HashMap<String, String>();
		if(sYear==null&&eYear==null) {
			String oldYear = salesMapper.month().substring(0,2);  // �뜝�룞�삕�뜝�룞�삕�뜝占� : 22
			String endYear = String.valueOf(cal.get(Calendar.YEAR)-2000);
			String endMonth = String.valueOf(cal.get(Calendar.MONTH)+1);
			if(endMonth.length()==1) endMonth = "0" + endMonth;
			date.put("startYear", oldYear);
			date.put("endYear", endYear);
		}else {
			date.put("startYear", sYear.substring(2));
			date.put("endYear", eYear.substring(2));
		}
		return date;
	}
}
