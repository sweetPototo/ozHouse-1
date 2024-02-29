package com.oz.ozHouse.client;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.oz.ozHouse.client.bean.LoginOkBean;
import com.oz.ozHouse.client.service.MemberMapper;
import com.oz.ozHouse.client.service.MypageMapper;
import com.oz.ozHouse.client.service.ShoppingMapper;
import com.oz.ozHouse.dto.*;
import com.oz.ozHouse.merchant.service.ProductManagementMapper;

@Controller
public class ShoppingController {
	@Autowired
	private ShoppingMapper shoppingMapper;
	@Autowired
	private ProductManagementMapper productmanagementMapper;
	@Autowired
	private MypageMapper mypageMapper;
	@Autowired
	private MemberMapper memberMapper;

	private static final String PRODUCT_PATH = "C:\\Users\\moonj\\Desktop\\ozHouse\\ozHouse\\src\\main\\webapp\\resources\\merchant\\imageFile";
	private static final String BLOG_PATH = "C:\\Users\\moonj\\Desktop\\ozHouse\\ozHouse\\src\\main\\webapp\\resources\\client\\blog_image";
	private static final String REVIEW_PATH = "C:\\Users\\moonj\\Desktop\\ozHouse\\ozHouse\\src\\main\\webapp\\resources\\client\\review_image";
	
	// 쇼핑몰 메인페이지
	@RequestMapping(value = { "/", "main.do", "index.do" }, method = RequestMethod.GET)
	public String home(HttpServletRequest req) {

		List<ProductDTO> list_product = shoppingMapper.main_listProduct();
		List<BlogDTO> list_blog = shoppingMapper.main_listBlog();
		List<ProductDTO> list_today = shoppingMapper.selectTodayProduct();

		String product_path = PRODUCT_PATH + "\\" + "uploadFiles";
		
		List<String> blog_encodedImages = new ArrayList<>();
		List<String> member_encodedImages = new ArrayList<>();

		for (BlogDTO blog : list_blog) {
		    // 여러 이미지 파일 이름을 콤마로 분리하여 배열에 저장
		    String[] fileNames = blog.getBlog_image().split(",");
		    
		    // 대표 이미지 파일 이름
		    String representativeImageName = fileNames.length > 0 ? fileNames[0] : null;
		    // 대표 이미지 파일 경로
		    File blog_img = new File(BLOG_PATH, representativeImageName);
		    
		    MemberDTO memberDTO = shoppingMapper.shop_getMember(blog.getMember_id());
		    String memberFileName = memberDTO.getMember_image();

		    
		    try {
		        if (blog_img.exists()) {
		            String encodedImage = encodeImageToBase64(blog_img);
		            blog_encodedImages.add(encodedImage);
		        }
		        
		        if (memberFileName != null) {
	                File member_img = new File(req.getServletContext().getRealPath("/resources/image"), memberFileName);
	                if (member_img.exists()) {
	                    String memberEncodedImage = encodeImageToBase64(member_img);
	                    member_encodedImages.add(memberEncodedImage);
	                } else {
	                    // member_img가 존재하지 않는 경우
	                    member_encodedImages.add(null);
	                }
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}


		// 상품 이미지 파일 Base64 인코딩
		List<String> product_encodedImages = new ArrayList<>();

		for (ProductDTO product : list_product) {
			File product_img = new File(product_path, product.getProduct_image_change());
			try {
				if (product_img.exists()) {
					String encodedImage = encodeImageToBase64(product_img);
					product_encodedImages.add(encodedImage);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		List<String> todayProduct_encodedImages = new ArrayList<>();
		
		for (ProductDTO todays : list_today) {
			File product_img = new File(product_path, todays.getProduct_image_change());
			System.out.println("a : " + todays.getProduct_today());
			try {
				if (product_img.exists()) {
					String encodedImage = encodeImageToBase64(product_img);	
					todayProduct_encodedImages.add(encodedImage);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		

		req.setAttribute("blog_list", list_blog);
		req.setAttribute("listProduct", list_product);
		req.setAttribute("encodedImages", product_encodedImages);
		req.setAttribute("list_todays", list_today);
		req.setAttribute("todayProduct_encodedImages", todayProduct_encodedImages);
		req.setAttribute("blog_encodedImages", blog_encodedImages);
		req.setAttribute("member_encodedImages", member_encodedImages);

		return "client/main/Main";
	}

	private String encodeImageToBase64(File file) throws IOException {
		byte[] fileContent = FileUtils.readFileToByteArray(file);
		return Base64.getEncoder().encodeToString(fileContent);
	}

	// 상품상세 페이지
	@RequestMapping(value = "/prodView_main.do", method = RequestMethod.GET)
	public String prodView(HttpServletRequest req, @RequestParam(name = "num") int num,
			@ModelAttribute ReviewDTO dto) throws IOException {
		HttpSession session = req.getSession();
		LoginOkBean loginMember = (LoginOkBean) session.getAttribute("loginMember");
		System.out.println("로그인한 멤버 : " + loginMember);
		
		if(loginMember != null) {
			String member_id = loginMember.getMember_id();
	    	dto.setMember_id(member_id);
		}    	

		ProductDTO productDTO = shoppingMapper.getProduct(num);

		String root = PRODUCT_PATH + "\\" + "uploadFiles";
		String root1 = PRODUCT_PATH + "\\" + "uploadProFiles";

		// 대표 이미지 인코딩
		File product_img = new File(root, productDTO.getProduct_image_change());
		if (product_img.exists()) {
			String encodedImage = encodeImageToBase64(product_img);
			req.setAttribute("encodedImage", encodedImage);
		}

		// 상세 이미지 인코딩
		List<String> encodedImagesPro = new ArrayList<>();
		String[] imageProFiles = productDTO.getProduct_image_pro_change().split(",");

		for (String imageFileName : imageProFiles) {
			File imageProFile = new File(root1, imageFileName);

			if (imageProFile.exists()) {
				String encodedImagePro = encodeImageToBase64(imageProFile);
				encodedImagesPro.add(encodedImagePro);
			}
		}

		// 스크랩 확인 여부
		int member_num = getMemberNum(req);
		int product_num = num;
		String scarpResult = checkScarp(member_num, product_num);

		// 스크랩 수
		int scrap_count = shoppingMapper.main_scrap_count(product_num);
		
		// 리뷰
		List<ReviewDTO> main_listReview = shoppingMapper.main_listReview(product_num);
		List<String> review_encodedImages = new ArrayList<>();
		List<String> member_encodedImages = new ArrayList<>();
		
		System.out.println("리뷰 : " + main_listReview.size());
		
		if (main_listReview != null) {
		    for (ReviewDTO review : main_listReview) {
		        MemberDTO memberDTO = shoppingMapper.shop_getMember(review.getMember_id());
		        String memberFileName = memberDTO.getMember_image();
		        File review_img = new File(REVIEW_PATH, review.getReview_image());

		        try {
		            if (review_img.exists()) {
		                String encodedImage = encodeImageToBase64(review_img);
		                review_encodedImages.add(encodedImage);
		            } 

		            if (memberFileName != null) {
		                File member_img = new File(req.getServletContext().getRealPath("/resources/image"), memberFileName);
		                if (member_img.exists()) {
		                    String memberEncodedImage = encodeImageToBase64(member_img);
		                    member_encodedImages.add(memberEncodedImage);
		                } else {
		                    // member_img가 존재하지 않는 경우
		                    member_encodedImages.add(null);
		                }
		            } else {
		                // memberFileName이 null인 경우
		                member_encodedImages.add(null);
		            }
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
			req.setAttribute("main_listReview", main_listReview);
			req.setAttribute("review_encodedImages", review_encodedImages);
			req.setAttribute("member_encodedImages", member_encodedImages);
		}

		
		// 리뷰 수
		int review_count = shoppingMapper.review_count(num);
		
		req.setAttribute("productDTO", productDTO);
		req.setAttribute("encodedImagesPro", encodedImagesPro);
		req.setAttribute("scrapResult", scarpResult);
		req.setAttribute("loginMember", loginMember);
		req.setAttribute("scrap_count", scrap_count);


		return "client/main/Prodview";
	}

	// 스크랩 추가
	@RequestMapping(value = "scrap.do", method = RequestMethod.POST)
	@ResponseBody
	public String insertScrap(HttpServletRequest req, @RequestParam(name = "product_num") int product_num) {
		int member_num = getMemberNum(req);

		String result = checkScarp(member_num, product_num);
		System.out.println("체크 스크립 결과 : " + result);
		if (result == "N") {
			HashMap<String, Integer> hm = new HashMap<>();
			hm.put("member_num", member_num);
			hm.put("product_num", product_num);
			shoppingMapper.main_insertScrap(hm);

			result = "Y";
			return result;
		}

		return result;
	}

	// 스크랩 취소
	@RequestMapping(value = "unscrap.do", method = RequestMethod.POST)
	@ResponseBody
	public String deleteScrap(HttpServletRequest req, @RequestParam(name = "product_num") int product_num) {
		int member_num = getMemberNum(req);

		String result = checkScarp(member_num, product_num);

		if (result == "Y") {
			HashMap<String, Integer> hm = new HashMap<>();
			hm.put("member_num", member_num);
			hm.put("product_num", product_num);
			shoppingMapper.main_deleteScrap(hm);
			result = "N";
			return result;
		}

		return result;
	}

	public String checkScarp(int member_num, int product_num) {
		String result = "Y";

		HashMap<String, Integer> hm = new HashMap<>();
		hm.put("member_num", member_num);
		hm.put("product_num", product_num);
		if (shoppingMapper.main_checkScrap(hm) == null) {
			result = "N";
			return result;
		}
		return result;
	}

	// 리뷰 작성
	@RequestMapping(value = "review_write.do", method = RequestMethod.POST)
	public String reviewWrite(HttpServletRequest req, 
			@RequestParam(name = "product_num") int product_num,
			@RequestParam(name = "review_star") int review_star,
			@ModelAttribute ReviewDTO dto, BindingResult result) {
		
	    if (result.hasErrors()) {
	        dto.setReview_image("");
	    }

		String review_content = req.getParameter("review_content");
		
		MultipartHttpServletRequest mr = (MultipartHttpServletRequest) req;
		MultipartFile mf = mr.getFile("review_image");
		String fileName = mf.getOriginalFilename();
		File reviewImg = new File(REVIEW_PATH, fileName);
		
		System.out.println("글 : " + review_content);
		System.out.println("별점 : " + review_star);
		System.out.println("리뷰 이미지 : " + reviewImg);
		
		dto.setReview_image(fileName);
		
	    HttpSession session = req.getSession();
	    LoginOkBean loginMember = (LoginOkBean)session.getAttribute("loginMember");
    	String member_id = loginMember.getMember_id();
    	
    	dto.setMember_id(member_id);
		
		int res = shoppingMapper.main_review(dto);
		
		try {
			mf.transferTo(reviewImg);
		} catch(IOException e) {
			req.setAttribute("msg", "리뷰 이미지 첨부 실패");
			req.setAttribute("url", "");
			return "forward:message.jsp";
		}
		
		if(res > 0) {
			req.setAttribute("msg", "리뷰 등록 성공");
			req.setAttribute("url", "prodView_main.do?num=" + product_num);
		} else {
			req.setAttribute("msg", "리뷰 등록 실패");
			req.setAttribute("url", "prodView_main.do?num=" + product_num);
		}
		
		return "message";
	}

	private int getMemberNum(HttpServletRequest req) {
		HttpSession session = req.getSession();
		if ((LoginOkBean) session.getAttribute("loginMember") != null) {
			LoginOkBean loginMember = (LoginOkBean) session.getAttribute("loginMember");
			MemberDTO dto = memberMapper.getMember(loginMember.getMember_id());
			return dto.getMember_num();
		}
		return -1;
	}

	private MemberDTO getMember(HttpServletRequest req) {
		HttpSession session = req.getSession();
		if ((LoginOkBean) session.getAttribute("loginMember") != null) {
			LoginOkBean loginMember = (LoginOkBean) session.getAttribute("loginMember");
			MemberDTO dto = memberMapper.getMember(loginMember.getMember_id());
			return dto;
		}
		return null;
	}

	   @RequestMapping("/best_main.do")
	   public String Best(HttpServletRequest req, @RequestParam(value="spec", required=false) String spec){
	       int member_num = getMemberNum(req);
	       
	       if (member_num != -1) {
	           List<ProductDTO> list = mypageMapper.getMyScrap(member_num);
	          HashMap<Integer, String> hm = new HashMap<>();
	           for (int i = 0; i < list.size(); i++) {
	              String check = checkScarp(member_num, list.get(i).getProduct_num());
	              hm.put(list.get(i).getProduct_num(), check);
	           }
	           req.setAttribute("scrapCheck", hm);
	       }
	       
	       if (spec.equals("best")) {
	          // 기준 : 지난 7일 동안 5개 넘개 팔린 물건
	    	   List<ProductDTO> list_best = shoppingMapper.selectBestProduct();
	    	   List<String> product_encodedImages = new ArrayList<>();
	    	   String product_path = PRODUCT_PATH + "\\" + "uploadFiles";
	    	   
				for (ProductDTO product : list_best) {
					File product_img = new File(product_path, product.getProduct_image_change());
					
					try {
						if (product_img.exists()) {
							String encodedImage = encodeImageToBase64(product_img);
							product_encodedImages.add(encodedImage);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
	          req.setAttribute("product", list_best);
	          req.setAttribute("product_encodedImages", product_encodedImages);
	          
	       }if (spec.equals("today")) {
		    	  List<ProductDTO> list_today = shoppingMapper.selectTodayProduct();
		    	  List<String> product_encodedImages = new ArrayList<>();
		    	  String product_path = PRODUCT_PATH + "\\" + "uploadFiles";
		    	  
					for (ProductDTO product : list_today) {
						File product_img = new File(product_path, product.getProduct_image_change());
						
						try {
							if (product_img.exists()) {
								String encodedImage = encodeImageToBase64(product_img);
								product_encodedImages.add(encodedImage);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

		          req.setAttribute("product", list_today);
		          req.setAttribute("product_encodedImages", product_encodedImages);
	       }
	      
	       
	      req.setAttribute("upPath", req.getServletContext().getRealPath("/resources/files"));

	      return "client/main/Best";
	   }


	// 쇼핑 메인페이지
	@RequestMapping("/shop_main.do")
	public String shop_main(HttpServletRequest req) {
		List<ProductDTO> list_product = shoppingMapper.main_listProduct();
		List<CategoryDTO> list_cate = shoppingMapper.main_listCate();
		
		String product_path = PRODUCT_PATH + "\\" + "uploadFiles";
		List<String> product_encodedImages = new ArrayList<>();
		List<Integer> reviewCounts = new ArrayList<>();

		for (ProductDTO product : list_product) {
			File product_img = new File(product_path, product.getProduct_image_change());
			try {
				if (product_img.exists()) {
					String encodedImage = encodeImageToBase64(product_img);
					product_encodedImages.add(encodedImage);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
	        int review_count = shoppingMapper.review_count(product.getProduct_num());
	        reviewCounts.add(review_count);
		}
		
		req.setAttribute("product_encodedImages", product_encodedImages);
		req.setAttribute("list_product", list_product);
		req.setAttribute("list_cate", list_cate);
		req.setAttribute("review_count", reviewCounts);
		return "client/main/shop_main";
	}

	@RequestMapping(value = "/CartList_main.do", method = RequestMethod.GET)
	public String cartlist_main(HttpServletRequest req) {
		
		return "client/main/CartList_main";
	}

	@RequestMapping(value = "/CartAdd_main.do", method = RequestMethod.GET)
	public String cartAdd_main (HttpServletRequest req, @RequestParam int product_num, @RequestParam int order_count) throws IOException {
		HttpSession session = req.getSession();
		req.setAttribute("upPath", req.getServletContext().getRealPath("/files"));
		
		ProductDTO dto = shoppingMapper.getProduct(product_num);
		HashMap<ProductDTO, Integer> cart = (HashMap) session.getAttribute("cart");

		if (cart == null)
			cart = new HashMap<ProductDTO, Integer>();

		// 객체를 비교할 게 아니라 product num 을 비교
		for (ProductDTO pdto : cart.keySet()) {
			if (pdto.getProduct_num() == dto.getProduct_num()) {
				cart.put(pdto, cart.get(pdto) + order_count);
				session.setAttribute("cart", cart);
				System.out.println("aldald?");
				return "redirect:CartList_main.do";
			}
		}
		

		String root = PRODUCT_PATH + "\\" + "uploadFiles";
		List<String> encodedImages = new ArrayList<>();

		// 대표 이미지 인코딩
		File product_img = new File(root, dto.getProduct_image_change());
		if (product_img.exists()) {
			String encodedImage = encodeImageToBase64(product_img);
			encodedImages.add(encodedImage);
		}
		

		cart.put(dto, order_count);
		session.setAttribute("cart", cart);
		session.setAttribute("encodedImages", encodedImages);
		return "redirect:CartList_main.do";
	}

	@ResponseBody
	@RequestMapping(value = "/cart_update.do", method = RequestMethod.POST)
	public String cart_update(HttpServletRequest req, @RequestParam int product_num, @RequestParam int order_count) {
		System.out.println("여기까지 왓어요");
		HttpSession session = req.getSession();
		req.setAttribute("upPath", req.getServletContext().getRealPath("/files"));

		ProductDTO dto = shoppingMapper.getProduct(product_num);
		HashMap<ProductDTO, Integer> cart = (HashMap) session.getAttribute("cart");

		if (cart == null)
			cart = new HashMap<ProductDTO, Integer>();

		// 객체를 비교할 게 아니라 product num 을 비교
		for (ProductDTO pdto : cart.keySet()) {
			if (pdto.getProduct_num() == dto.getProduct_num()) {
				cart.put(pdto, order_count);
				session.setAttribute("cart", cart);
				return "T";
			}
		}

		cart.put(dto, order_count);
		session.setAttribute("cart", cart);
		return "T";
	}

	@ResponseBody
	@RequestMapping(value = "/cart_delete.do", method = RequestMethod.POST)
	public String cart_delete(HttpServletRequest req, @RequestBody Map<String, List<String>> data) {
		List<String> selectedProductNums = data.get("selectedProductNums");
		System.out.println("여기까지 왓어요2");
		HttpSession session = req.getSession();
		req.setAttribute("upPath", req.getServletContext().getRealPath("/files"));

		for (String num : selectedProductNums) {
			System.out.println(num);
		}
		HashMap<ProductDTO, Integer> cart = (HashMap) session.getAttribute("cart");

		if (cart == null)
			cart = new HashMap<ProductDTO, Integer>();

		// 객체를 비교할 게 아니라 product num 을 비교
		List<ProductDTO> toRemove = new ArrayList<>();
		for (String num : selectedProductNums) {
			for (ProductDTO pdto : cart.keySet()) {
				if (pdto.getProduct_num() == Integer.parseInt(num)) {
					toRemove.add(pdto);
				}
			}
		}
		for (ProductDTO pdto : toRemove) {
			cart.remove(pdto);
		}
		return "T";
	}

	protected boolean getCategoryList(HttpServletRequest req) {
		List<CategoryDTO> clist = productmanagementMapper.merchant_listCate();
		if (clist == null || clist.size() == 0) {
			req.setAttribute("msg", "쇼핑몰 준비중 입니다. 나중에 오세요");
			req.setAttribute("url", "main.do");
			return false;
		}
		req.setAttribute("listCate", clist);
		return true;
	}

	// 중복을 허용하지 않는 hashSet 이용
	private List<Mer_CouponDTO> can_user_list(MemberDTO memberDTO, HashMap<ProductDTO, Integer> orderProducts) {
		List<Mer_CouponDTO> userCoupons = mypageMapper.getUserCoupon(memberDTO.getMember_num());
		Map<Integer, List<Mer_CouponDTO>> couponMap = new HashMap<>();
		HashSet<Mer_CouponDTO> addedCoupons = new HashSet(); // 중복 체크용 Set

		// 사용자의 쿠폰을 판매자 번호별로 그룹화
		for (Mer_CouponDTO coupon : userCoupons) {
			couponMap.computeIfAbsent(coupon.getMer_num(), k -> new ArrayList<>()).add(coupon);
		}

		List<Mer_CouponDTO> canUseCoupons = new ArrayList<>();

		// 주문 상품별로 적용 가능한 쿠폰 찾기
		for (ProductDTO product : orderProducts.keySet()) {
			int merNum = product.getMer_num();
			List<Mer_CouponDTO> couponsForMer = couponMap.get(merNum);
			if (couponsForMer != null) {
				for (Mer_CouponDTO coupon : couponsForMer) {
					if (addedCoupons.add(coupon)) {
						canUseCoupons.add(coupon);
					}
				}
			}
		}

		return canUseCoupons;
	}

	@RequestMapping(value = "/Order_main.do", method = RequestMethod.GET)
	public String order_main(HttpServletRequest req, @RequestParam(required = false) String product_num,
			@RequestParam(required = false) String order_count) {
		HttpSession session = req.getSession();

		// 1. 멤버 설정
		MemberDTO memberDTO = getMember(req);
		if (memberDTO == null) {
			req.setAttribute("msg", "로그인 후 주문이 가능합니다");
			req.setAttribute("url", "member_login.do");
			return "message";
		}
		req.setAttribute("member", memberDTO);

		if (product_num != null) {
			int product_num1 = Integer.parseInt(product_num);
			int order_count1 = Integer.parseInt(order_count);

			// product HashMap 만들기
			ProductDTO dto = shoppingMapper.getProduct(product_num1);
			String mode = req.getParameter("mode");
			req.setAttribute("upPath", req.getServletContext().getRealPath("/files"));

			if (mode.equals("all")) {
				HashMap<ProductDTO, Integer> orderProducts = (HashMap) session.getAttribute("cart");
				if (orderProducts == null)
					orderProducts = new HashMap<ProductDTO, Integer>();
				for (ProductDTO pdto : orderProducts.keySet()) {
					if (pdto.getProduct_num() == dto.getProduct_num()) {
						orderProducts.put(pdto, orderProducts.get(pdto) + order_count1);
						List<Mer_CouponDTO> can_user_list = can_user_list(memberDTO, orderProducts);
						req.setAttribute("userCouponList", can_user_list);
						req.setAttribute("orderProducts", orderProducts);
						session.setAttribute("orderProducts", orderProducts);
						return "client/main/Order";
					}
				}
				orderProducts.put(dto, order_count1);
				List<Mer_CouponDTO> can_user_list = can_user_list(memberDTO, orderProducts);
				req.setAttribute("userCouponList", can_user_list);
				req.setAttribute("orderProducts", orderProducts);
				session.setAttribute("orderProducts", orderProducts);
				return "client/main/Order";
			} else {
				HashMap<ProductDTO, Integer> orderProducts = new HashMap<ProductDTO, Integer>();
				orderProducts.put(dto, order_count1);
				List<Mer_CouponDTO> can_user_list = can_user_list(memberDTO, orderProducts);
				req.setAttribute("userCouponList", can_user_list);
				req.setAttribute("orderProducts", orderProducts);
				session.setAttribute("orderProducts", orderProducts);
				return "client/main/Order";
			}
		} else {
			HashMap<ProductDTO, Integer> orderProducts = new HashMap<ProductDTO, Integer>();
			orderProducts = (HashMap) session.getAttribute("cart");
			List<Mer_CouponDTO> can_user_list = can_user_list(memberDTO, orderProducts);
			req.setAttribute("userCouponList", can_user_list);
			req.setAttribute("orderProducts", orderProducts);
			session.setAttribute("orderProducts", orderProducts);
			return "client/main/Order";
		}
	}

	   @RequestMapping(value = "/order_success.do", method = RequestMethod.POST)
	   public String order_success(HttpServletRequest req, OrderDTO dto,
	         @RequestParam(required = false) List<String> selectedCoupons) {
	      HttpSession session = req.getSession();
	      LocalDateTime localDateTime = LocalDateTime.now();
	      String memberNum = Integer.toString(getMemberNum(req));

	      String localDateTimeFormat1 = memberNum + localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

	      long order_code = Long.parseLong(localDateTimeFormat1);
	      System.out.println(order_code);
	      MemberDTO member = getMember(req);

	      String member_id = member.getMember_id();

	      String ad1 = req.getParameter("sample6_address");
	      String ad2 = req.getParameter("sample6_detailAddress");
	      String ad3 = req.getParameter("sample6_extraAddress");
	      System.out.println(ad1 + "/" + ad2 + "/" + ad3);

	      int user_point = member.getMember_point();
	      int final_discount_point = Integer.parseInt(req.getParameter("final_discount_point"));
	      int final_discount_coupon = Integer.parseInt(req.getParameter("final_discount_coupon"));
	      int member_use_service = final_discount_point + final_discount_coupon;

	      HashMap<ProductDTO, Integer> orderProducts = (HashMap) session.getAttribute("orderProducts");
	      int i = 0;
	      for (ProductDTO pdto : orderProducts.keySet()) {
	         dto.setMember_id(member_id);
	         dto.setOrder_name(req.getParameter("member_name"));
	         dto.setOrder_hp1(req.getParameter("member_hp1"));
	         dto.setOrder_hp2(req.getParameter("member_hp2"));
	         dto.setOrder_hp3(req.getParameter("member_hp3"));
	         dto.setOrder_code(order_code);
	         dto.setProduct_num(pdto.getProduct_num());
	         dto.setOrder_orderlike("ok");
	         dto.setOrder_refund("f");

	         // 만약 쿠폰과 포인트를 사용하지 않았다면?
	         if (member_use_service == 0) {
	            user_point += pdto.getProduct_point() * orderProducts.get(pdto);
	            System.out.println(user_point);
	         }

	         // 쿠폰과 포인트를 사용했을 시 첫 번째 행에만 쿠폰과 포인트 저장 / 멤버 포인트 업데이트
	         if (i == 0) {
	            dto.setOrder_dis_point(final_discount_point);
	            user_point -= final_discount_point;
	            dto.setOrder_dis_coupon(final_discount_coupon);
	         }
	         dto.setOrder_postcode(req.getParameter("member_postcode1"));
	         dto.setOrder_place(ad1 + "/" + ad2 + "/" + ad3);
	         dto.setOrder_count(orderProducts.get(pdto));
	         dto.setOrder_assembly_cost(pdto.getProduct_assembly_cost() * orderProducts.get(pdto));
	         dto.setOrder_price(pdto.getProduct_price());

	         int res = shoppingMapper.insertOrder(dto);

	         pdto.setProduct_quantity(pdto.getProduct_quantity() - orderProducts.get(pdto));
	         pdto.setProduct_purchases_count(pdto.getProduct_purchases_count() + orderProducts.get(pdto));
	         int res2 = shoppingMapper.updateOrderProduct(pdto);

	         System.out.println(res);
	         System.out.println(res2);
	         i++;
	      }

	      // 쿠폰한 사용 처리
	      if (selectedCoupons != null) {
	         for (String coupon : selectedCoupons) {
	            User_CouponDTO ucdto = new User_CouponDTO();
	            ucdto.setMember_num(member.getMember_num());
	            ucdto.setMer_coupon_num(Integer.parseInt(coupon));
	            ucdto.setOrder_code(order_code);
	            int res = shoppingMapper.selectedCoupons(ucdto);
	            System.out.println("쿠폰 처리 : " + res);
	         }
	      }

	      // 포인트 처리
	      member.setMember_point(user_point);
	      int res1 = shoppingMapper.updateMemberPoint(member);
	      System.out.println("포인트 처리 : " + res1);

	      // 첫 번째 확인이므로 mode=first
	      return "redirect:order_confirm.do?mode=first&order=" + order_code;
	   }

	@RequestMapping(value = "/order_confirm.do", method = RequestMethod.GET)
	public String order_confirm(HttpServletRequest req) {
		long order_code = Long.parseLong(req.getParameter("order"));
		List<OrderDTO> confirmOrderProducts = shoppingMapper.getOrder(order_code);
		HashMap<ProductDTO, Integer> orderhm = new HashMap<>();
		List<Mer_CouponDTO> selectOrderCoupon = shoppingMapper.selectOrderCoupon(order_code);

		for (OrderDTO dto : confirmOrderProducts) {
			orderhm.put(shoppingMapper.getProduct(dto.getProduct_num()), dto.getOrder_count());
		}

		req.setAttribute("userCouponList", selectOrderCoupon);
		req.setAttribute("member", getMember(req));
		req.setAttribute("orderinfo", confirmOrderProducts.get(0));
		System.out.println("뭐임?" + confirmOrderProducts.get(0).getOrder_dis_point());
		req.setAttribute("confirmOrderProducts", orderhm);

		return "client/main/order_confirm";
	}

	   @RequestMapping(value = "/order_cancel.do", method = RequestMethod.GET)
	   public String order_cancel(HttpServletRequest req) {
	      long order_code = Long.parseLong(req.getParameter("order"));
	      List<OrderDTO> confirmOrderProducts = shoppingMapper.getOrder(order_code);
	      MemberDTO member = getMember(req);
	      int user_point = member.getMember_point();

	      for (OrderDTO dto : confirmOrderProducts) {
	         // 포인트 되돌려주기
	         dto.setOrder_orderlike("return");
	         if (dto.getOrder_dis_point() >= 1) {
	            user_point += dto.getOrder_dis_point();
	         }

	         // 주문 수량 되돌리기
	         ProductDTO pdto = shoppingMapper.getProduct(dto.getProduct_num());
	         pdto.setProduct_quantity(pdto.getProduct_quantity() + dto.getOrder_count());
	         int res = shoppingMapper.setProductQuantity(pdto);
	      }

	      member.setMember_point(user_point);
	      int res = shoppingMapper.updateMemberPoint(member);
	      int res1 = shoppingMapper.orderCancel(order_code);
	      System.out.println(res);

	      req.setAttribute("msg", "주문 취소 완료");
	      req.setAttribute("url", "mypage_shopping.do");

	      return "message";
	   }

}