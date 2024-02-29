package com.oz.ozHouse.client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oz.ozHouse.client.bean.LoginOkBean;
import com.oz.ozHouse.client.service.MemberMapper;
import com.oz.ozHouse.client.service.MypageMapper;
import com.oz.ozHouse.client.service.ShoppingMapper;
import com.oz.ozHouse.dto.MemberDTO;
import com.oz.ozHouse.dto.Mer_CouponDTO;
import com.oz.ozHouse.dto.MypagePointDTO;
import com.oz.ozHouse.dto.OrderDTO;
import com.oz.ozHouse.dto.ProductDTO;
import com.oz.ozHouse.dto.ReviewDTO;
import com.oz.ozHouse.dto.ScrapDTO;
import com.oz.ozHouse.dto.SearchDTO;
import com.oz.ozHouse.dto.User_CouponDTO;

@Controller
public class MypageController {
	@Autowired
	private MemberMapper memberMapper;	
	@Autowired
	private MypageMapper mypageMapper;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private ShoppingMapper shoppingMapper;
	
	private static final String PRODUCT_PATH = "C:\\Users\\moonj\\Desktop\\ozHouse\\ozHouse\\src\\main\\webapp\\resources\\merchant\\imageFile";
	
    @RequestMapping(value = "/mypage_profile.do", method = RequestMethod.GET)
    public String index(HttpServletRequest req) {
    	MemberDTO dto = getMember(req);
    	req.setAttribute("member", dto);
        return "client/mypage/mypage_profile";
    }
    
    private MemberDTO getMember(HttpServletRequest req) {
    	HttpSession session = req.getSession();
    	LoginOkBean loginMember = (LoginOkBean)session.getAttribute("loginMember");
    	MemberDTO dto = memberMapper.getMember(loginMember.getMember_id());
    	return dto;
    }
    
    private int getMemberNum(HttpServletRequest req) {
    	HttpSession session = req.getSession();
    	LoginOkBean loginMember = (LoginOkBean)session.getAttribute("loginMember");
     	MemberDTO dto = memberMapper.getMember(loginMember.getMember_id());

    	return dto.getMember_num();
    }
    
    @RequestMapping(value = "/mypage_review.do", method = RequestMethod.GET)
    public String mypage_riew(HttpServletRequest req) {
    	MemberDTO dto = getMember(req);
    	List<ReviewDTO> list = mypageMapper.getMyReview(dto.getMember_num());
    	req.setAttribute("myReview", list);
        return "client/mypage/mypage_review";
    }
    
    @RequestMapping(value = "/mypage_writeReview.do", method = RequestMethod.GET)
    public String mypage_writeReview(HttpServletRequest req) {
    	
        return "client/mypage/mypage_review";
    }
    
    @RequestMapping(value = "/mypage_writeReview.do", method = RequestMethod.POST)
    public String mypage_writeReviewPro(HttpServletRequest req, @ModelAttribute ReviewDTO dto) {
    	// writePro
    	int res = mypageMapper.insertReview(dto);
		if (res>0) {
			req.setAttribute("msg", "리뷰 작성");
			req.setAttribute("url", "");
		}else if (res<0){
			req.setAttribute("msg", "리뷰 작성 실패");
			req.setAttribute("url", "");
		}
        return "client/mypage/mypage_review";
    }    
    
    @RequestMapping(value="/mypage_reviewContent.do", method=RequestMethod.GET)
    public String mypage_reviewContent(HttpServletRequest req, @RequestParam("review_num") int review_num) {
    	
    	ReviewDTO dto = mypageMapper.getReview(review_num);
    	req.setAttribute("getReview", dto);
    	return "client/mypage/mypage_reviewContent";
    }
    
    @RequestMapping(value = "mypage_setting.do", method = RequestMethod.GET)
    public String mypage_setting() {
    	
        return "member_update.do";
    }
    
    @RequestMapping(value = "mypage_coupon.do", method = RequestMethod.GET)
    public String mypage_coupon(HttpServletRequest req) {
    	int member_num = getMemberNum(req);
    	List<Mer_CouponDTO> user_list = mypageMapper.getUserCoupon(member_num);
    	List<Mer_CouponDTO> mer_list = mypageMapper.getMerCouponList(member_num);
    	req.setAttribute("merCouponList", mer_list);
    	req.setAttribute("userCouponList", user_list);
        return "client/mypage/mypage_coupon";
    }
    
    @RequestMapping(value = "mypage_usercoupon.do", method = RequestMethod.GET)
    public String mypage_usercoupon(HttpServletRequest req) {
    	int mer_couponnum = Integer.parseInt(req.getParameter("mer_couponnum"));
    	int mer_num = Integer.parseInt(req.getParameter("mer_num"));
    	String end = req.getParameter("enddate");
    	User_CouponDTO dto = new User_CouponDTO();
    	dto.setMember_num(getMemberNum(req));
    	dto.setMer_coupon_num(mer_couponnum);
    	dto.setMer_num(mer_num);
    	dto.setUser_coupon_active("f");
    	dto.setMer_couponenddate(end);
    	int res = mypageMapper.insertUserCoupon(dto);
		if (res>0) {
			req.setAttribute("msg", "쿠폰 받기 성공");
			req.setAttribute("url", "mypage_coupon.do");
		}else if (res<0){
			req.setAttribute("msg", "쿠폰 받기 실패");
			req.setAttribute("url", "mypage_coupon.do");
		}
    	return "message";
    }
    
    @RequestMapping(value = "mypage_upadatePasswd.do", method = RequestMethod.GET)
    public String mypage_updatePasswd(HttpServletRequest req) {
    	String mode = req.getParameter("mode");
    	
    	if (mode != null) {
    		req.setAttribute("mode", "find");
    		req.setAttribute("member_id", req.getParameter("id"));
    	}
        return "client/mypage/mypage_updatePasswd";
    }
    
    @RequestMapping(value = "mypage_upadatePasswd.do", method = RequestMethod.POST)
    public String mypage_updatePasswdPro(HttpServletRequest req) {
    	MemberDTO dto = new MemberDTO();
    	String mode = req.getParameter("mode");
    	String id = req.getParameter("member_id");
    	String new_pass = req.getParameter("new_member_passwd");
    	
        boolean passwd = false;
        
        if (!mode.equals("find")) {
        	dto = getMember(req);
            String old_pass = req.getParameter("member_passwd");
        	passwd = passwordEncoder.matches(old_pass, dto.getMember_passwd());
        }else if(mode.equals("find")) {
        	passwd = true;
        	dto.setMember_id(id);
        }
    	
    	if (passwd) {
    		dto.setMember_passwd(passwordEncoder.encode(new_pass));
        	int res = memberMapper.updatePasswd(dto);
    		if (res>0) {
    			req.setAttribute("msg", "비밀번호가 업데이트 되었습니다");
    			req.setAttribute("url", "index.do");
    		}else if (res<0){
    			req.setAttribute("msg", "비밀번호 업데이트 실패 : 수정하실 비밀번호를 확인해 주세요");
    			req.setAttribute("url", "index.do");
    		}
    	}else {
    		req.setAttribute("msg", "비밀번호 업데이트 실패 : 다시 확인해 주세요");
			req.setAttribute("url", "index.do");
    	}
    	
        return "message";
    }
    
    @RequestMapping("/scrap_product.do")
    @ResponseBody
    public String scrap_product(HttpServletRequest req, @RequestParam("product_num") String product_num) {
        String result="Y";
        int member_num = getMemberNum(req); 
        int product_num1 = Integer.parseInt(product_num);
        HashMap<String, Integer> hm = new HashMap<>();
        hm.put("member_num", member_num);
        hm.put("product_num", product_num1);
        if (mypageMapper.checkScrap(hm) != null) {
        	result = "N"; 	
        	return result;
        }
        mypageMapper.insertScrap(hm);
        return result;
    }
    
    public String checkScarp(int member_num, int product_num){
        String result="f";
        HashMap<String, Integer> hm = new HashMap<>();
        hm.put("member_num", member_num);
        hm.put("product_num", product_num);
        if (mypageMapper.checkScrap(hm) != null) {
        	result = "t"; 	
        	return result;
        }
        return result;
    }
    
    @RequestMapping(value = "mypage_scrapbook.do", method = RequestMethod.GET)
    public String mypage_scrapbook(HttpServletRequest req) {
    	int member_num = getMemberNum(req);
    	List<ProductDTO> list = mypageMapper.getMyScrap(member_num);
    	
    	if (member_num != -1) {
    		HashMap<Integer, String> hm = new HashMap<>();
        	for (int i = 0; i < list.size(); i++) {
        		String check = checkScarp(member_num, list.get(i).getProduct_num());
        		hm.put(list.get(i).getProduct_num(), check);
        	}
        	req.setAttribute("scrapCheck", hm);
    	}
    	
		List<String> encodedImagesPro = new ArrayList<>();
		
		String product_path = PRODUCT_PATH + "\\" + "uploadFiles";
		
		for (ProductDTO scrapProduct : list) {
			File product_img = new File(product_path, scrapProduct.getProduct_image_change());
			System.out.println("product_img" + product_img);
			try {
				if (product_img.exists()) {
					String encodedImage = encodeImageToBase64(product_img);
					encodedImagesPro.add(encodedImage);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    	
//		int review_count = shoppingMapper.review_count();
		
    	req.setAttribute("myScrap", list);
    	req.setAttribute("encodedImages", encodedImagesPro);
//    	req.setAttribute("review_count", review_count);
    	
        return "client/mypage/mypage_scarp";
    }
    
	private String encodeImageToBase64(File file) throws IOException {
		byte[] fileContent = FileUtils.readFileToByteArray(file);
		return Base64.getEncoder().encodeToString(fileContent);
	}
    
    @RequestMapping("/unscrap_product.do")
    @ResponseBody
    public String unscrap(HttpServletRequest req, @RequestParam("product_num") String product_num) {
        String result="Y";
        int member_num = getMemberNum(req); 
        int product_num1 = Integer.parseInt(product_num);
        HashMap<String, Integer> hm = new HashMap<>();
        hm.put("member_num", member_num);
        hm.put("product_num", product_num1);
        if (mypageMapper.checkScrap(hm) == null) {
        	result = "N"; 	
        	return result;
        }
        mypageMapper.deleteScrap(hm);
        return result;
    }
    
    @RequestMapping("/check_scrap.do")
    @ResponseBody
    public String checkScrap(HttpServletRequest req, @RequestParam("product_num") String product_num) {
        String result="Y";
        int member_num = getMemberNum(req); 
        int product_num1 = Integer.parseInt(product_num);
        HashMap<String, Integer> hm = new HashMap<>();
        hm.put("member_num", member_num);
        hm.put("product_num", product_num1);
        if (mypageMapper.checkScrap(hm) == null) {
        	result = "N"; 	
        	return result;
        }
        return result;
    }
    
    @RequestMapping(value = "/mypage_shopping.do", method = RequestMethod.GET)
    public String mypage_shopping(HttpServletRequest req, 
    							  @RequestParam(value="mode", required=false) String mode,
    							  @RequestParam(value="detail", required=false) String detail) {
    	MemberDTO member = getMember(req);
    	String member_id = member.getMember_id();
    	List<Long> order_code_list = null;
    	List<OrderDTO> user_order_list = null;
    	
    	// 오더 코드 리스트와 오더 리스트는 모드에 따라 바뀜
    	// if (mode == null) 멤버의 모든 쇼핑 내역
    	// if (mode != null) 동적 쿼리 작성으로 상태 및 이하 detail 별 쿼리 작동
    	
    	if (mode != null) {
    		SearchDTO search = new SearchDTO();
			search.setMember_id(member_id);
			search.setMode(mode);
			if (mode.equals("statement")) {
				search.setDetail_string(detail);
			}else {
				search.setDetail_int(Integer.parseInt(detail));
			}
			
        	order_code_list = mypageMapper.getOrderCodeSearch(search);
        	for (Long order_code : order_code_list) {
        		System.out.println(order_code);
        	}
        	user_order_list = mypageMapper.getOrderListSearch(order_code_list);

    	}else {
            order_code_list = shoppingMapper.getOrderCode(member_id);
            user_order_list = shoppingMapper.getUserOrderList(member_id);

    	}

		HashMap<Integer, ProductDTO> orderhm = new HashMap<>();
		
		int ready = 0;
		int delivery = 0;
		int complete = 0;
		
		for (OrderDTO dto : user_order_list) {
			// order_num 을 넣었을 때 ProductDTO 를 뽑아내는 해시 맵
			orderhm.put(dto.getOrder_num(), shoppingMapper.getOrderProduct(dto.getProduct_num()));
		}
    	
		for (Long order_code : order_code_list) {
			String order_statement = shoppingMapper.getOrderStatement(order_code);
			if (order_statement.contentEquals("ready")) {
				ready++;
			}else if (order_statement.contentEquals("delivery")) {
				delivery++;
			}else if (order_statement.contentEquals("complete")) {
				complete++;
			}
		}
		
		int coupon = mypageMapper.couponCount(member.getMember_num());
		int point = member.getMember_point();
		
		if (member.getMember_level().equals("normal")) {
			req.setAttribute("member_level", "WELCOME");
		}else if(member.getMember_level().equals("vip")) {
			req.setAttribute("member_level", "VIP");
		}
		
		req.setAttribute("coupon", coupon);
		req.setAttribute("point", point);
		req.setAttribute("ready", ready);
		req.setAttribute("delivery", delivery);
		req.setAttribute("complete", complete);
    	req.setAttribute("order_code_list", order_code_list);
    	req.setAttribute("user_order_list", user_order_list);
    	req.setAttribute("order_product", orderhm);
    	
        return "client/mypage/mypage_shopping";
    }
    
    @RequestMapping(value="mypage_point.do", method=RequestMethod.GET)
    public String mypage_point(HttpServletRequest req) {
    	MemberDTO member = getMember(req); 
    	
    	List<Long> code_list = shoppingMapper.getOrderCode(member.getMember_id());
    	List<OrderDTO> order_list = shoppingMapper.getUserOrderList(member.getMember_id());
    	List<MypagePointDTO> pdto = new ArrayList<MypagePointDTO>();
    	

    	
    	for (long code : code_list) {
        	int use_point = 0;
        	int add_point = 0;
    		MypagePointDTO dto = new MypagePointDTO();
    		dto.setOrder_code(code);
    		for (OrderDTO odto : order_list) {
    			if (code == odto.getOrder_code()) {
    				dto.setOrder_date(odto.getOrder_date().replace("/", "."));
    				use_point += odto.getOrder_dis_point();
    				add_point += mypageMapper.getProductPoint(odto.getProduct_num());
    			}
    		}
    		if(use_point > 0) {
    			System.out.println("포인트 사용");
    			dto.setStatement("사용");
    			dto.setPoint(use_point);
    		}else if (add_point > 0){
    			System.out.println("포인트 적립");
    			dto.setStatement("적립");
    			dto.setPoint(add_point);
    		}
    		pdto.add(dto);
    	}
    	
    	
    	req.setAttribute("member_p", pdto);
    	req.setAttribute("member", member);
    	return "client/mypage/mypage_point";
    }
    
    
}
