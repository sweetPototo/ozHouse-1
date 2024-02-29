package com.oz.ozHouse.merchant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.oz.ozHouse.dto.ApplicationDTO;
import com.oz.ozHouse.dto.CategoryDTO;
import com.oz.ozHouse.dto.InbrandDTO;
import com.oz.ozHouse.dto.MerchantDTO;
import com.oz.ozHouse.merchant.bean.MerchantLoginBean;
import com.oz.ozHouse.merchant.service.BrandMapper;

@Controller
public class BrandController {

	static final String FILEPATH = 
			"C:\\Users\\moonj\\Desktop\\ozHouse\\ozHouse\\src\\main\\webapp\\resources\\merchant\\storeInform";
	
	@Autowired
	private BrandMapper brandMapper;
	
	@RequestMapping(value="/brand_application.do", method=RequestMethod.GET)
	public String brandApplication(String mer_num, HttpServletRequest req) {
		HttpSession session = req.getSession();
		MerchantLoginBean loginOk = (MerchantLoginBean)session.getAttribute("merchantLoginMember");
		if (loginOk == null) {
			String msg = "로그인 후 이용하시길 바랍니다.";
			String url = "merchant_login.do";
			req.setAttribute("msg", msg);
			req.setAttribute("url", url);
			return "forward:message.jsp";
		}
		int num = Integer.parseInt(mer_num);
		InbrandDTO dto = brandMapper.selectMer(num);
		if(dto != null) {	//      청        獵摸 
			//System.out.println("      청        斂 , ");
			if(!dto.getInbrand_canceldate().equals("0")) {		//      청        斂        청      獵摸 
				//System.out.println("      청     0    틈構 , ");
				Calendar calNow = Calendar.getInstance();		//      청  
				Calendar calEnd= Calendar.getInstance();		//      청    3       
				SimpleDateFormat df = new SimpleDateFormat("yy/MM/dd");
				Date date;
				try {
					date = df.parse(dto.getInbrand_canceldate());
					calEnd.setTime(date);
					calEnd.add(Calendar.MONTH, 3);
					//System.out.println("calStart : " + df.format(calNow.getTime()));
					//System.out.println("calEnd : " + df.format(calEnd.getTime()));
					if(calNow.before(calEnd)) {				//      청 狗觀    3                  
						//System.out.println("      청 狗觀    3               刻年 .");
						String msg = "입점신청 취소, 또는 거절일로부터 3개월이 지나지 않아 신청이 불가합니다.";
						String url = "merchant_main.do";
						req.setAttribute("msg", msg);
						req.setAttribute("url", url);
						return "forward:message.jsp";
					}else {										//      청 狗觀    3           
						//System.out.println("      청 狗觀    3             .");z
						req.setAttribute("mer_num", mer_num);
						return "merchant/brand/brand_application";
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}else {			//      청        斂 ,       청        摸 	
				//System.out.println("      청     0 甄 .");
				String msg = "현재 입점신청 승인중이거나 승인된 상점입니다.";
				String url = "brand_applicationList.do?mer_num=" + mer_num;
				req.setAttribute("msg", msg);
				req.setAttribute("url", url);
				return "forward:message.jsp";
			}
		}
		//      청          摸 
		//System.out.println("      청           .");
		req.setAttribute("mer_num", mer_num);
		return "merchant/brand/brand_application";
	}
	
	@RequestMapping(value="/brand_application.do", method=RequestMethod.POST)
	public String brandApplicationOk(@RequestParam Map<String, String> map, HttpServletRequest req) {
		MerchantDTO result = brandMapper.searchComNum(map.get("mer_num"));
		if(!map.get("inbrand_comnum1").equals(result.getMer_comnum1())||
				!map.get("inbrand_comnum2").equals(result.getMer_comnum2())||
				!map.get("inbrand_comnum3").equals(result.getMer_comnum3())) {
			String msg = "회원가입시의 사업자등록번호와 일치하지 않습니다.";
			String url = "brand_application.do?mer_num=" + map.get("mer_num");
			req.setAttribute("msg", msg);
			req.setAttribute("url", url);
			return "forward:message.jsp";
		}
		
		List<CategoryDTO> category = brandMapper.selectCate();
		req.setAttribute("category", category);
		req.setAttribute("inbrand_comnum1", map.get("inbrand_comnum1"));
		req.setAttribute("inbrand_comnum2", map.get("inbrand_comnum2"));
		req.setAttribute("inbrand_comnum3", map.get("inbrand_comnum3"));
		req.setAttribute("mer_num", map.get("mer_num"));
		return "merchant/brand/brand_inform";
	}
	
	@RequestMapping(value="/brand_inform.do", method=RequestMethod.GET)
	public String brandInform(@RequestParam int mer_num, HttpServletRequest req) {
		req.setAttribute("mer_num", mer_num);
		return "merchant/brand/brand_inform";
	}
	
	@RequestMapping(value="/brand_inform.do", method=RequestMethod.POST)
	public String brandInformOk( HttpServletRequest req, @ModelAttribute InbrandDTO dto, BindingResult result) 
			throws IllegalStateException, IOException {
		
		InbrandDTO befor = brandMapper.selectMer(dto.getMer_num());
		if(befor != null) {
			int res = brandMapper.deleteInbrand(befor.getInbrand_num());
		}
		MultipartHttpServletRequest mr = (MultipartHttpServletRequest)req;
        MultipartFile mFile = mr.getFile("inbrand_file"); 
        String msg = null;
	    String url = null;
        if (mFile != null && mFile.getSize() > 0) { 
            //UUID one = UUID.randomUUID(); //UUID          恝  ->    臼              화
//        	Calendar cal = Calendar.getInstance();
//        	SimpleDateFormat format = new SimpleDateFormat();
//        	format.applyPattern("yyyy-MM-dd_HH:mm:ss");
        	 //        見  ->  퓔      호_yyyy-MM-dd_HH:mm:ss_     見 
            String saveName = mFile.getOriginalFilename(); 
//            String saveName = dto.getMer_num() + "_" + format.format(cal.getTime())+ "_" + mFile.getOriginalFilename(); 

            mFile.transferTo(new File(FILEPATH + saveName)); //             
            dto.setInbrand_file(saveName);			//dto         見  setting
            
            int res = brandMapper.application(dto);
    	    if(res>0) {
    	    	msg = "입점신청이 완료되었습니다.";
    	    	url = "brand_applicationList.do?mer_num=" + dto.getMer_num();
    	    }else {
    	    	msg = "입점신청이 완료되지 않았습니다.";
    	    	url = "brand_application.do?mer_num=" + dto.getMer_num();
    	    }
        }else {
        	msg = "판매 관련 파일 업로드시 오류가 발생하였습니다.";
        	url = "brand_application.do?mer_num=" + dto.getMer_num();
        }
        req.setAttribute("msg", msg);
		req.setAttribute("url", url);
		return "forward:message.jsp";
	}	
	
	@RequestMapping(value="/brand_applicationList.do")
	public String brandApplicationList(int mer_num, HttpServletRequest req) {
		ApplicationDTO dto = brandMapper.applicationList(mer_num);
		if(dto == null) {
			String msg = "입점신청화면으로 이동합니다.";
			String url = "brand_application.do?mer_num=" + mer_num;
			return "forward:message.jsp";
		}else {
			String cate[] = dto.getInbrand_category().split(",");
			String category[] = new String[cate.length];
			for(int i=0; i<category.length; ++i) {
				category[i] = brandMapper.selectCateName(Integer.valueOf(cate[i]));
			}
			String resultCate = String.join(",", category);
			req.setAttribute("applicationList", dto);
			req.setAttribute("resultCate", resultCate);
			return "merchant/brand/brand_applicationList";
		}
	}
	
	@RequestMapping(value="/brand_download.do")
	public void brandDownload(@RequestParam String inbrand_file, HttpServletResponse response) throws Exception {

        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);  // 牟 琯                   
        response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(inbrand_file,"UTF-8")+"\";");//        
	    //fileName =  牟 琯           見        
	    //URLEncoder.encode =  韜쨉     悶               求     쨌      
        response.setHeader("Content-Transfer-Encoding", "binary");

        //byte[] fileByte = FileUtils.readFileToByteArray(new File("C:\\  善�_ver.pdf"));      �� 
        byte[] fileByte = FileUtils.readFileToByteArray(new File(FILEPATH + "/" + inbrand_file));
        response.getOutputStream().write(fileByte);
        response.getOutputStream().flush();
        response.getOutputStream().close();
	}
	
	@RequestMapping(value="/brand_cancel.do")
	public String brandCancel(HttpServletRequest req, int inbrand_num, String inbrand_file) {
		int res = brandMapper.brandCancelUpdate(inbrand_num);
		String msg = null;
		String url = null;
		
		if(res>0) {
			File file = new File(FILEPATH, inbrand_file);
			if (file.exists()){
				file.delete();
			}
			msg = "입점신청이 취소되었습니다.";
			url = "merchant_main.do";
		}else {
			msg = "입점신청 취소가 완료되지 않았습니다.";
			url = "merchant_main.do";
		}
		req.setAttribute("msg", msg);
		req.setAttribute("url", url);
		return "forward:message.jsp";
	}

}
