package com.oz.ozHouse.merchant;

import java.io.File;
import java.util.Date;
import java.util.Enumeration;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
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
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.oz.ozHouse.dto.ProductDTO;
import com.oz.ozHouse.dto.RequestProductDTO;
import com.oz.ozHouse.dto.CategoryDTO;
import com.oz.ozHouse.dto.InbrandDTO;
import com.oz.ozHouse.dto.NoticeDTO;
import com.oz.ozHouse.merchant.service.ProductManagementMapper;
import com.oz.ozHouse.merchant.service.StoreMainMapper;

@Controller
public class ProductManagementController {
	
	@Autowired
	private ProductManagementMapper productManagementMapper;
	
	private static final String PATH = "C:\\Users\\moonj\\Desktop\\ozHouse\\ozHouse\\src\\main\\webapp\\resources\\merchant\\imageFile";
	
	//�긽�뭹�벑濡� �뤌�럹�씠吏�濡� �씠�룞
	@RequestMapping(value = {"/productManagement_management.do", "/productManagement_input.do"}, method=RequestMethod.GET)
	public String productManagementInput(HttpServletRequest req, HttpServletResponse resp, @RequestParam Map<String, String> params) throws UnsupportedEncodingException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");

		InbrandDTO inbrand = productManagementMapper.merchant_getInbrandByMerNum(params.get("mer_num"));
	    List<CategoryDTO> selectedCategories = new ArrayList<>();

	    if (inbrand != null && inbrand.getInbrand_category() != null) {
	        String[] categoryNums = inbrand.getInbrand_category().split(",");
	        for (String numStr : categoryNums) {
	            try {
	                int num = Integer.parseInt(numStr.trim());
	                CategoryDTO category = productManagementMapper.merchant_getCategoryByNum(num);
	                if (category != null) {
	                    selectedCategories.add(category);
	                }
	            } catch (NumberFormatException e) {}
	        }
	    }
	    req.setAttribute("listCate", selectedCategories);
		return "merchant/store/productManagement/productManagement_input";
	}
	
	//�긽�뭹�벑濡�
	@RequestMapping(value="/productManagement_input.do", method=RequestMethod.POST)
	public String productManagementInput(@RequestParam("product_image") List<MultipartFile> product_image, @RequestParam("product_image_pro") List<MultipartFile> product_image_pro, MultipartHttpServletRequest multipartRequest, HttpServletRequest req, @ModelAttribute ProductDTO dto, BindingResult result, int mer_num, @RequestParam Map<String, String> params) throws Exception {
		String root = PATH + "\\" + "uploadFiles";
		
		File fileCheck = new File(root);
		if (!fileCheck.exists()) fileCheck.mkdir();
		
		String root1 = PATH + "\\" + "uploadProFiles";
		
		File fileCheck1 = new File(root1);
		if (!fileCheck1.exists()) fileCheck1.mkdir();
		
		if (result.hasErrors()) {
			dto.setProduct_image("");
			dto.setProduct_image_pro("");
		}
		MultipartHttpServletRequest mr = (MultipartHttpServletRequest)req;
		MultipartFile mf = mr.getFile("product_image");
		String filename = mf.getOriginalFilename();
		String ext1 = filename.substring(filename.lastIndexOf("."));
		String changeFile1 = UUID.randomUUID().toString() + ext1;
		
//		String path = req.getServletContext().getRealPath("/resources/files");
		
//		File file = new File(path, changeFile1);
		File file = new File(root, changeFile1);

		
		dto.setProduct_image(filename);
		dto.setProduct_image_change(changeFile1);
		
		System.out.println("root : " + root);
		System.out.println("filename : " + filename);
		System.out.println("changeFile1 : " + changeFile1);
		System.out.println("file : " + file);
		
		try {
			mf.transferTo(file);
		}catch(IOException e) {
			req.setAttribute("msg", "대표이미지 등록에 실패했습니다.");
			req.setAttribute("url", "productManagement_management.do?mer_num="+dto.getMer_num());
			return "forward:message.jsp";
		}
		
		System.out.println("product_image_pro : " + product_image_pro);
		
		List<Map<String, String>> fileList = new ArrayList<>();
		String fileproOri = "";
		for(int i = 0; i < product_image_pro.size(); i++) {
			String originFile = product_image_pro.get(i).getOriginalFilename();
			String ext = originFile.substring(originFile.lastIndexOf("."));
			String changeFile = UUID.randomUUID().toString() + ext;
			Map<String, String> map = new HashMap<>();
			map.put("originFile", originFile);
			map.put("changeFile", changeFile);
			fileproOri += originFile;
			System.out.println("originFIle : " + originFile);
			System.out.println("changeFile : " + changeFile);
			fileList.add(map);
		}
		System.out.println("root1 : " + root1);
		String filepro = "";
		
		try {
			for(int i = 0; i < product_image_pro.size(); i++) {
//				File uploadFile = new File(path + "\\" + fileList.get(i).get("changeFile"));
				File uploadFile = new File(root1 + "\\" + fileList.get(i).get("changeFile"));
				System.out.println("fileList : " + fileList.get(i).get("changeFile"));
				System.out.println("originFIle : " + fileList.get(i).get("originFIle"));

				product_image_pro.get(i).transferTo(uploadFile);
				System.out.println(uploadFile);
				filepro += fileList.get(i).get(("changeFile")) + ",";
				
				System.out.println(filepro);
			}
			
			System.out.println("다중 파일 업로드 성공!");
			
		} catch (IllegalStateException | IOException e) {
			System.out.println("다중 파일 업로드 실패");
			for(int i = 0; i < product_image_pro.size(); i++) {
//				new File(path + "\\" + fileList.get(i).get("changeFile")).delete();

				new File(root1 + "\\" + fileList.get(i).get("changeFile")).delete();
			}
			e.printStackTrace();
		}
		System.out.println("filepro : " + filepro);
		System.out.println("fileproOri : " + fileproOri);
		
		dto.setProduct_image_pro_change(filepro);
		dto.setProduct_image_pro(fileproOri);
		
		HttpSession session = req.getSession();
		session.setAttribute("product_image", root);
		session.setAttribute("product_image_pro", root1);
		System.out.println("root : " + session.getAttribute("product_image"));
		System.out.println("root1 : " + session.getAttribute("product_image_pro"));
		dto.setMer_num(mer_num);
		int res = productManagementMapper.merchant_insertProduct(dto);
		if (res>0) {
			req.setAttribute("msg", "상품 등록 요청 성공했습니다.");
			req.setAttribute("url", "productManagement_productList.do?mer_num="+dto.getMer_num());
		}else {
			req.setAttribute("msg", "상품 등록 요청 실패했습니다. 다시 시도하세요.");
			req.setAttribute("url", "productManagement_input.do?mer_num="+dto.getMer_num());
		}
		return "forward:message.jsp";
	}
	
	@RequestMapping(value="/product_update.do", method=RequestMethod.POST)
	public String productImageUpdate(@RequestParam("product_image") List<MultipartFile> product_image,
	        @RequestParam("product_image_pro") List<MultipartFile> product_image_pro,
	        MultipartHttpServletRequest multipartRequest, HttpServletRequest req, @ModelAttribute RequestProductDTO dto, 
	        BindingResult result, @RequestParam Map<String, String> params) throws Exception {

		
		String root = PATH + "\\" + "uploadFiles";
		
		File fileCheck = new File(root);
		if (!fileCheck.exists()) fileCheck.mkdir();
		
		String root1 = PATH + "\\" + "uploadProFiles";
		
		File fileCheck1 = new File(root1);
		if (!fileCheck1.exists()) fileCheck1.mkdir();
		
		if (result.hasErrors()) {
			dto.setProduct_image("");
			dto.setProduct_image_pro("");
		}
		
		MultipartHttpServletRequest mr = (MultipartHttpServletRequest)req;
		MultipartFile mf = mr.getFile("product_image");
		String filename = mf.getOriginalFilename();
		String ext1 = filename.substring(filename.lastIndexOf("."));
		String changeFile1 = UUID.randomUUID().toString() + ext1;

		File file = new File(root, changeFile1);
		
		dto.setProduct_image(filename);
		dto.setProduct_image_change(changeFile1);
		System.out.println("File path: " + file.getAbsolutePath());

		System.out.println("root : " + root);
		System.out.println("filename : " + filename);
		System.out.println("changeFile1 : " + changeFile1);
		System.out.println("file : " + file);
		
		try {
			mf.transferTo(file);
		}catch(IOException e) {
			req.setAttribute("msg", "대표이미지 등록에 실패했습니다.");
			req.setAttribute("url", "productManagement_management.do?mer_num="+dto.getMer_num());
			return "forward:message.jsp";
		}
		
		System.out.println("product_image : " + dto.getProduct_image());
		
		List<Map<String, String>> fileList = new ArrayList<>();
		String fileproOri = "";
		for(int i = 0; i < product_image_pro.size(); i++) {
			String originFile = product_image_pro.get(i).getOriginalFilename();
			String ext = originFile.substring(originFile.lastIndexOf("."));
			String changeFile = UUID.randomUUID().toString() + ext;
			Map<String, String> map = new HashMap<>();
			map.put("originFile", originFile);
			map.put("changeFile", changeFile);
			fileproOri += originFile;
			System.out.println("originFIle : " + originFile);
			System.out.println("changeFile : " + changeFile);
			fileList.add(map);
		}
		System.out.println("root1 : " + root1);
		String filepro = "";
		
		try {
			for(int i = 0; i < product_image_pro.size(); i++) {
				File uploadFile = new File(root1 + "\\" + fileList.get(i).get("changeFile"));
				System.out.println("fileList : " + fileList.get(i).get("changeFile"));
				System.out.println("originFIle : " + fileList.get(i).get("originFIle"));

				product_image_pro.get(i).transferTo(uploadFile);
				System.out.println(uploadFile);
				filepro += fileList.get(i).get(("changeFile")) + ",";
				
				System.out.println(filepro);
			}
			
			System.out.println("다중 파일 업로드 성공했습니다.");
			
		} catch (IllegalStateException | IOException e) {
			System.out.println("다중 파일 업로드 실패");
			for(int i = 0; i < product_image_pro.size(); i++) {
				new File(root1 + "\\" + fileList.get(i).get("changeFile")).delete();
			}
			e.printStackTrace();
		}
		System.out.println("filepro : " + filepro);
		System.out.println("fileproOri : " + fileproOri);
		
		dto.setProduct_image_pro_change(filepro);
		dto.setProduct_image_pro(fileproOri);
	
		HttpSession session = req.getSession();
		session.setAttribute("product_image", root);
		session.setAttribute("product_image_pro", root1);
		System.out.println("Set image name: " + dto.getProduct_image());
		System.out.println("Set image change name: " + dto.getProduct_image_change());
		productManagementMapper.merchant_deletereProduct(params.get("product_num"));
		int res = productManagementMapper.merchant_updateProductInfo(dto);
		if (res>0) {
			productManagementMapper.merchant_updateRe(params.get("product_num"));
			productManagementMapper.merchant_deletemsg(Integer.parseInt(params.get("product_num")));
			req.setAttribute("msg", "상품 수정 요청 성공했습니다.");
			req.setAttribute("url", "productManagement_productList.do?mer_num="+dto.getMer_num());
		}else {
			req.setAttribute("msg", "상품 수정 요청 실패했습니다. 다시 시도하세요.");
			req.setAttribute("url", "product_update.do?product_num="+params.get("product_num"));
		}
		return "forward:message.jsp";
	}
	
	//�긽�뭹�닔�젙 �뤌�럹�씠吏�
	@RequestMapping(value="/product_update.do", method=RequestMethod.GET)
	public String productUpdate(HttpServletRequest req, @RequestParam Map<String, String> params, @ModelAttribute RequestProductDTO redto) {
		req.setAttribute("product_num", params.get("product_num"));
		InbrandDTO inbrand = productManagementMapper.merchant_getInbrandByMerNum(params.get("mer_num"));
	    List<CategoryDTO> selectedCategories = new ArrayList<>();

	    if (inbrand != null && inbrand.getInbrand_category() != null) {
	        String[] categoryNums = inbrand.getInbrand_category().split(",");
	        for (String numStr : categoryNums) {
	            try {
	                int num = Integer.parseInt(numStr.trim());
	                CategoryDTO category = productManagementMapper.merchant_getCategoryByNum(num);
	                if (category != null) {
	                    selectedCategories.add(category);
	                }
	            } catch (NumberFormatException e) {}
	        }
	    }
	    req.setAttribute("listCate", selectedCategories);
		ProductDTO dto = productManagementMapper.merchant_getProduct(params.get("product_num"));
		req.setAttribute("getProduct", dto);
		return "merchant/store/productManagement/productManagement_update";
	}
	
	@RequestMapping(value = "/deleteSelectedProducts.do", method = RequestMethod.POST)
	public String deleteSelectedProducts(@RequestParam("selectedProducts") List<String> selectedProducts, HttpServletRequest req, int mer_num) {
	    
		if (selectedProducts == null || selectedProducts.isEmpty()) {
	        req.setAttribute("msg", "선택된 상품이 없습니다.");
	        req.setAttribute("url", "productManagement_productList.do?mer_num=" + mer_num);
	        return "forward:message.jsp";
	    }
		
		for(String productNum : selectedProducts) {
	        int parsedProductNum = Integer.parseInt(productNum);
	        System.out.println(parsedProductNum);
			int res = productManagementMapper.merchant_deleteProduct(parsedProductNum);
			 if (res > 0) {
				 productManagementMapper.merchant_deletereProduct(productNum);
				 productManagementMapper.merchant_deletemsg(parsedProductNum);
				 req.setAttribute("msg", "상품 삭제에 성공했습니다.");
			}else {
				req.setAttribute("msg", "상품 삭제에 실패했습니다. 다시 시도하세요.");
				}
	    }
		req.setAttribute("url", "productManagement_productList.do?mer_num="+mer_num);

		return "forward:message.jsp";
	}
	
	 @RequestMapping(value = "/getProductMessage.do", method = RequestMethod.GET, produces = "application/text; charset=UTF-8")
	    @ResponseBody
	    public String getProductMessage(HttpServletRequest req, HttpServletResponse resp,  @RequestParam("product_num") int productNum) throws UnsupportedEncodingException {
			req.setCharacterEncoding("UTF-8");
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("text/plain; charset=UTF-8");
			String msg = productManagementMapper.merchant_msgString(productNum);
			System.out.println(msg);
	        return msg;
	    }
	 
	   //�긽�뭹議고쉶 �럹�씠吏�濡� �씠�룞
	   @RequestMapping("/productManagement_productList.do")
	   public String productManagementList(HttpServletRequest req, HttpServletResponse resp, @RequestParam Map<String, String> params) throws IOException {
		   req.setCharacterEncoding("UTF-8");
			resp.setCharacterEncoding("UTF-8");
		   
		   String root = PATH + "\\" + "uploadFiles";
	      System.out.println("root는 : " + root);
	      List<ProductDTO> list = productManagementMapper.merchant_listProduct(params);
	      if (!list.isEmpty()) {
	           System.out.println(list.get(0).getProduct_image_change());
	       }
	      for (ProductDTO product : list) {
		        File imageFile = new File(root, product.getProduct_image_change());
		        if (imageFile.exists()) {
		            String encodedImage = encodeImageToBase64(imageFile);
		            product.setEncodedImage(encodedImage); // Assume ProductDTO has a field for the encoded image
		        }
		    }
	      
	      int listCount = productManagementMapper.merchant_listCount(params);
	      req.setAttribute("listProduct", list);
	      req.setAttribute("listCount", listCount);
	      req.setAttribute("product_image", root);
	      return "merchant/store/productManagement/productManagement_list";
	   }
	
	   @RequestMapping("/product_content.do")
	   public String productContent(HttpServletRequest req, @RequestParam Map<String, String> map) throws IOException {
		   String root = PATH + "\\" + "uploadFiles";
	       String root1 = PATH + "\\" + "uploadProFiles";
	       
	       System.out.println("product_num : " + map.get("product_num"));
	       ProductDTO dto = productManagementMapper.merchant_getProduct(map.get("product_num"));

	       req.setAttribute("getProduct", dto);

	       // ���몴�씠誘몄� �씤肄붾뵫
	       File imageFile = new File(root, dto.getProduct_image_change());
	       if (imageFile.exists()) {
	           String encodedImage = encodeImageToBase64(imageFile);
	           req.setAttribute("encodedImage", encodedImage);
	       }
	       
	       // �긽�꽭�씠誘몄� �씤肄붾뵫
	       List<String> encodedImagesPro = new ArrayList<>();
	       String[] imageProFiles = dto.getProduct_image_pro_change().split(",");
	       for (String imageFileName : imageProFiles) {
	           File imageProFile = new File(root1, imageFileName);
	           if (imageProFile.exists()) {
	               String encodedImagePro = encodeImageToBase64(imageProFile);
	               encodedImagesPro.add(encodedImagePro);
	           }
	       }
	       req.setAttribute("encodedImagesPro", encodedImagesPro);
	       
	       RequestProductDTO redto = productManagementMapper.merchant_getreProduct(map.get("product_num"));

	       if (redto != null) {
		       req.setAttribute("getreProduct", redto);

		       File imageFile2 = new File(root, redto.getProduct_image_change());
		       if (imageFile2.exists()) {
		           String encodedImage2 = encodeImageToBase64(imageFile2);
		           req.setAttribute("encodedImage2", encodedImage2);
		       	}
		       List<String> encodedImagesPro2 = new ArrayList<>();
		       String[] imageProFiles2 = redto.getProduct_image_pro_change().split(",");
		       for (String imageFileName2 : imageProFiles2) {
		           File imageProFile2 = new File(root1, imageFileName2);
		           if (imageProFile2.exists()) {
		               String encodedImagePro2 = encodeImageToBase64(imageProFile2);
		               encodedImagesPro2.add(encodedImagePro2);
		           }
		       }
		       req.setAttribute("encodedImagesPro2", encodedImagesPro2);
	       }
	       return "merchant/store/productManagement/productManagement_content";
	   }
	
	private String encodeImageToBase64(File file) throws IOException {
	    byte[] fileContent = FileUtils.readFileToByteArray(file);
	    return Base64.getEncoder().encodeToString(fileContent);
	}
	
	@RequestMapping("/product_delete.do")
	public String deleteRe (HttpServletRequest req, @RequestParam Map<String, String> params) {
	    int productNum = Integer.parseInt(params.get("product_num"));

	    // 상품에 해당하는 파일 정보 조회
	    ProductDTO product = productManagementMapper.merchant_getProduct(params.get("product_num"));
	    if (product != null) {
	        File mainImage = new File(PATH + "\\uploadFiles", product.getProduct_image_change());
	        if (mainImage.exists()) {
	            mainImage.delete();
	        }
	        
	        // 상세 이미지 삭제
	        String[] detailImages = product.getProduct_image_pro_change().split(",");
	        for (String imageName : detailImages) {
	            File detailImage = new File(PATH + "\\uploadProFiles", imageName);
	            if (detailImage.exists()) {
	                detailImage.delete();
	            }
	        }
	    }

	    // 상품 정보 삭제
	    int res = productManagementMapper.merchant_deleteProduct(productNum);
	    if (res > 0) {
	        productManagementMapper.merchant_deletereProduct(params.get("product_num"));
	        productManagementMapper.merchant_deletemsg(productNum);
	        req.setAttribute("msg", "상품 삭제에 성공했습니다.");
	        req.setAttribute("url", "productManagement_requestList.do?mer_num="+params.get("mer_num"));
	    } else {
	        req.setAttribute("msg", "상품 삭제에 실패했습니다. 다시 시도하세요.");
	        req.setAttribute("url", "productManagement_productList.do?mer_num="+params.get("mer_num"));
	    }
	    return "forward:message.jsp";
	}
	@RequestMapping("/productManagement_requestList.do")
	public String productManagementRequestList(HttpServletRequest req, @RequestParam Map<String, String> params) throws IOException {
		String root = PATH + "\\" + "uploadFiles";
		req.setAttribute("product_image", root);
		List<ProductDTO> list = productManagementMapper.merchant_requestListProduct(params);
		for (ProductDTO product : list) {
	        File imageFile = new File(root, product.getProduct_image_change());
	        if (imageFile.exists()) {
	            String encodedImage = encodeImageToBase64(imageFile);
	            product.setEncodedImage(encodedImage); // Assume ProductDTO has a field for the encoded image
	        }
	    }
		int requestListCount = productManagementMapper.merchant_requestListCount(params);
		req.setAttribute("requestListProduct", list);
		req.setAttribute("requestListCount", requestListCount);
		return "merchant/store/productManagement/productManagement_request_list";
	}
	
	//�벑濡� �떊泥� 泥좏쉶
	@RequestMapping("/product_fcancle.do")
	public String approvalfCancle(HttpServletRequest req, @RequestParam Map<String, String> params) {
		req.setAttribute("mer_num", params.get("mer_num"));
		int res = productManagementMapper.merchant_dcancle(params);
		String url = "productManagement_requestList.do?mer_num="+params.get("mer_num");
		if (res > 0) {
			req.setAttribute("msg", "등록 신청 철회 성공했습니다.");
			req.setAttribute("url", url);
		}else {
			req.setAttribute("msg", "등록 신청 철회 실패했습니다. 다시 시도하십시오.");
			req.setAttribute("url", url);
		}
		return "forward:message.jsp";
	}
	
	@RequestMapping("/product_cancle.do")
	public String requestRe(HttpServletRequest req, @RequestParam Map<String, String> params) {
		req.setAttribute("mer_num", params.get("mer_num"));
		int res = productManagementMapper.merchant_requestRe(params.get("product_num"));
		String url = "productManagement_requestList.do?mer_num="+params.get("mer_num");
		if (res > 0) {
			productManagementMapper.merchant_deletereProduct(params.get("product_num"));
	        productManagementMapper.merchant_deletemsg(Integer.parseInt(params.get("product_num")));
			req.setAttribute("msg", "삭제 신청 성공했습니다!");
			req.setAttribute("url", url);
		}else {
			req.setAttribute("msg", "삭제 신청 실패했습니다. 다시 시도하십시오.");
			req.setAttribute("url", url);
		}
		return "forward:message.jsp";
	}
	
	//�궘�젣 �떊泥� 泥좏쉶
	@RequestMapping("/product_dcancle.do")
	public String approvaldCancle(HttpServletRequest req, @RequestParam Map<String, String> params) {
		req.setAttribute("mer_num", params.get("mer_num"));
		int res = productManagementMapper.merchant_ucancle(params.get("_num"));
		String url = "productManagement_requestList.do?mer_num="+params.get("mer_num");
		if (res > 0) {
			productManagementMapper.merchant_deletereProduct(params.get("params.mer"));
			System.out.println(params.get("params.mer"));
			req.setAttribute("msg", "신청철회 성공했습니다.");
			req.setAttribute("url", url);
		}else {
			req.setAttribute("msg", "신청철회 실패했습니다. 다시 시도하십시오.");
			req.setAttribute("url", url);
		}
		return "forward:message.jsp";
	}
	
	//�닔�젙 �떊泥� 泥좏쉶
	@RequestMapping("/product_ucancle.do")
	public String approvaluCancle(HttpServletRequest req, @RequestParam Map<String, String> params) {
		req.setAttribute("mer_num", params.get("mer_num"));
		int res = productManagementMapper.merchant_ucancle(params.get("product_num"));
		String url = "productManagement_requestList.do?mer_num="+params.get("mer_num");
		if (res > 0) {
			productManagementMapper.merchant_deletereProduct(params.get("product_num"));
			req.setAttribute("msg", "신청철회 성공했습니다.");
			req.setAttribute("url", url);
		}else {
			req.setAttribute("msg", "신청철회 실패했습니다. 다시 시도하십시오.");
			req.setAttribute("url", url);
		}
		return "forward:message.jsp";
	}
	
	// �옱怨좉�由� 由ъ뒪�듃
	@RequestMapping("/productManagement_stock.do")
	public String productManagementStockList(HttpServletRequest req, @RequestParam Map<String, Object> params) throws IOException {
	    String root = PATH + "\\" + "uploadFiles";
	    req.setAttribute("product_image", root);
	    
	    String[] specArray = req.getParameterValues("spec");
	    List<String> spec = (specArray != null) ? Arrays.asList(specArray) : new ArrayList<String>();
	    params.put("spec", spec);

	    // 寃��깋 list
	    List<ProductDTO> list = productManagementMapper.merchant_stockListProduct(params);
	    for (ProductDTO product : list) {
	        File imageFile = new File(root, product.getProduct_image_change());
	        if (imageFile.exists()) {
	            String encodedImage = encodeImageToBase64(imageFile);
	            product.setEncodedImage(encodedImage); 
	        }
	    }
	    req.setAttribute("stockListProduct", list);
	    
	    int stockListCount = productManagementMapper.merchant_stockListCount(params);
	    req.setAttribute("stockListCount", stockListCount);

	    return "merchant/store/productManagement/productManagement_stock";
	}
	
	//�옱怨좎닔�젙
	@RequestMapping("/stock_update.do")
	public String stockUpdate(HttpServletRequest req, @RequestParam Map<String, String> params) {
		req.setAttribute("mer_num", params.get("mer_num"));
	    int res = productManagementMapper.merchant_updateStock(params);
	    if (res > 0) {
	        req.setAttribute("msg", "재고 수정에 성공했습니다.");
	    } else {
	        req.setAttribute("msg", "재고 수정에 실패했습니다.");
	    }
        req.setAttribute("url", "productManagement_stock.do?mer_num="+params.get("mer_num"));
	    return "forward:message.jsp";
	}
}
