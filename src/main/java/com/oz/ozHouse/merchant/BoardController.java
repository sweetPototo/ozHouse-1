package com.oz.ozHouse.merchant;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.oz.ozHouse.merchant.service.BoardMapper;
import com.oz.ozHouse.merchant.service.ProductManagementMapper;
import com.oz.ozHouse.dto.Product_QA_DTO;
import com.oz.ozHouse.dto.Product_reQA_DTO;
import com.oz.ozHouse.dto.Order_QADTO;
import com.oz.ozHouse.dto.Order_reQADTO;
import com.oz.ozHouse.dto.ProductDTO;
import com.oz.ozHouse.dto.ReviewDTO;

@Controller
public class BoardController {
	
	@Autowired
	private BoardMapper boardMapper;
	
	//상품 문의
	@RequestMapping(value={"/board_productBoard.do","/board_board.do"})
	public String productBoard(HttpServletRequest req, @RequestParam Map<String, String> params) {
		List<Product_QA_DTO> relist = boardMapper.productQAList(params);
		int productqaCount = boardMapper.productqaCount(params);
		req.setAttribute("productQAList", relist);
		req.setAttribute("productqaCount", productqaCount);
		return "merchant/store/board/product_board";
	}

	//상품 문의 상세보기
	@RequestMapping("/product_qa_content.do")
	public String productContent(HttpServletRequest req, int product_QA_num) {
		List<Product_QA_DTO> list = boardMapper.productContent(product_QA_num);
		List<Product_reQA_DTO> list2 = boardMapper.productReContent(product_QA_num);
	    req.setAttribute("productContent", list);
	    req.setAttribute("productReContent", list2);
	    req.setAttribute("product_QA_num", product_QA_num);
		return "merchant/store/board/product_content";
	}
	
	//상품 문의 답변
	@RequestMapping(value="/product_reqa.do", method=RequestMethod.GET)
	public String productReQA(HttpServletRequest req, @RequestParam("product_QA_num") int productQANum) {
		List<Product_QA_DTO> list = boardMapper.productContent(productQANum);
	    req.setAttribute("productContent", list);
		req.setAttribute("product_QA_num", productQANum);
		return "merchant/store/board/product_write";
	}
	
	//상품 문의 답변
	@RequestMapping(value="/product_reqa.do", method=RequestMethod.POST)
	public String productReQA(HttpServletRequest req, HttpServletResponse resp,@RequestParam Map<String, Object> params) throws UnsupportedEncodingException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		req.setAttribute("member_id", params.get("member_id"));
		String product_reQA_content = req.getParameter("product_reQA_content");
		product_reQA_content = product_reQA_content.replace("\r\n", "<br>").replace("\n", "<br>");
		int res = boardMapper.productReQa(params);
		int productQANum = Integer.parseInt(req.getParameter("product_QA_num"));
		if (res>0) {
			int updateRes = boardMapper.productQAstate(productQANum);
			req.setAttribute("msg", "답변 등록 성공했습니다.");
			req.setAttribute("url", "product_qa_content.do?product_QA_num="+productQANum);
		}else {
			req.setAttribute("msg", "답변 등록 실패했습니다.");
			req.setAttribute("url", "product_reqa.do?product_QA_num="+productQANum);
		}
		return "forward:message.jsp";
	}
	
	//상품 문의 답변 수정
	@RequestMapping(value="/product_reqa_update.do", method=RequestMethod.GET)
	public String productReQAModify(HttpServletRequest req, int product_reQA_num) {
		System.out.println(product_reQA_num);
		List<Product_QA_DTO> list = boardMapper.productContent(product_reQA_num);
	    req.setAttribute("productContent", list);
		List<Product_reQA_DTO> dto = boardMapper.productReContent(product_reQA_num);
		req.setAttribute("productReContent", dto);
		return "merchant/store/board/product_update";
	}
	
	//상품 문의 답변 수정
	@RequestMapping(value="/product_reqa_update.do", method=RequestMethod.POST)
	public String productReQAModify(HttpServletRequest req, HttpServletResponse resp, @ModelAttribute Product_reQA_DTO dto) throws UnsupportedEncodingException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		String product_reQA_content = req.getParameter("product_reQA_content");
		product_reQA_content = product_reQA_content.replace("\r\n", "<br>").replace("\n", "<br>");
		int res = boardMapper.productReQaUpdate(dto);
		if (res>0) {
			req.setAttribute("msg", "답변 수정 성공했습니다.");
			req.setAttribute("url", "product_qa_content.do?product_QA_num="+dto.getProduct_reQA_num());
		}else {
			req.setAttribute("msg", "답변 수정 실패했습니다.");
			req.setAttribute("url", "product_reqa.do?product_QA_num="+dto.getProduct_reQA_num());
		}
		return "forward:message.jsp";
	}
	
	//주문 문의
	@RequestMapping("/board_orderBoard.do")
	public String orderBoard(HttpServletRequest req, @RequestParam Map<String, String> params) {
		List<Order_QADTO> list = boardMapper.orderQAList(params);
	    int orderqaCount = boardMapper.orderqaCount(params);
	    req.setAttribute("orderQAList", list);
	    req.setAttribute("orderqaCount", orderqaCount);
	    return "merchant/store/board/order_board";
	}	
	
	//주문 문의 상세보기
	@RequestMapping("/order_qa_content.do")
	public String orderContent(HttpServletRequest req, int order_QA_num) {
		List<Order_QADTO> list = boardMapper.orderContent(order_QA_num);
		List<Order_reQADTO> list2 = boardMapper.orderReContent(order_QA_num);
	    req.setAttribute("orderContent", list);
	    req.setAttribute("orderReContent", list2);
		return "merchant/store/board/order_content";
	}
		
	//주문 문의 답변
	@RequestMapping(value="/order_reqa.do", method=RequestMethod.GET)
	public String orderReQA(HttpServletRequest req, @RequestParam("order_QA_num") int orderQANum) {
		List<Order_QADTO> list = boardMapper.orderContent(orderQANum);
	    req.setAttribute("orderContent", list);
		req.setAttribute("order_QA_num", orderQANum);
		return "merchant/store/board/order_write";
	}
	
	//주문 문의 답변
	@RequestMapping(value="/order_reqa.do", method=RequestMethod.POST)
	public String orderReQA(HttpServletRequest req, HttpServletResponse resp,@RequestParam Map<String, Object> params) throws UnsupportedEncodingException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		req.setAttribute("member_id", params.get("member_id"));
		int res = boardMapper.orderReQa(params);
		int orderQANum = Integer.parseInt(req.getParameter("order_QA_num"));
		if (res>0) {
			int updateRes = boardMapper.orderQAstate(orderQANum);
			req.setAttribute("msg", "답변 등록 성공했습니다.");
			req.setAttribute("url", "order_qa_content.do?order_QA_num="+orderQANum);
		}else {
			req.setAttribute("msg", "답변 등록 실패했습니다.");
			req.setAttribute("url", "order_reqa.do?order_QA_num="+orderQANum);
		}
		return "forward:message.jsp";
	}
	
	//주문 문의 답변 수정
	@RequestMapping(value="/order_reqa_update.do", method=RequestMethod.GET)
	public String orderReQAModify(HttpServletRequest req, int order_reQA_num) {
		List<Order_QADTO> list = boardMapper.orderContent(order_reQA_num);
		List<Order_reQADTO> dto = boardMapper.orderReContent(order_reQA_num);
	    req.setAttribute("orderContent", list);
		req.setAttribute("orderReContent", dto);
		return "merchant/store/board/order_update";
	}
	
	//주문 문의 답변 수정
	@RequestMapping(value="/order_reqa_update.do", method=RequestMethod.POST)
	public String orderReQAModify(HttpServletRequest req, HttpServletResponse resp, @ModelAttribute Order_reQADTO dto) throws UnsupportedEncodingException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		int res = boardMapper.orderReQaUpdate(dto);
		if (res>0) {
			req.setAttribute("msg", "답변 수정 성공했습니다.");
			req.setAttribute("url", "order_qa_content.do?order_QA_num="+dto.getOrder_reQA_num());
		}else {
			req.setAttribute("msg", "답변 수정 실패했습니다.");
			req.setAttribute("url", "order_reqa.do?order_QA_num="+dto.getOrder_reQA_num());
		}
		return "forward:message.jsp";
	}
	
	//구매 후기
	@RequestMapping("/board_orderReview.do")
	public String orderReview(HttpServletRequest req, HttpServletResponse resp, @RequestParam Map<String, String> params) throws UnsupportedEncodingException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
	    List<ReviewDTO> list = boardMapper.reviewList(params);
	    int reviewqaCount = boardMapper.reviewqaCount(params);
	    req.setAttribute("reviewList", list);
	    req.setAttribute("reviewqaCount", reviewqaCount);
	    return "merchant/store/board/order_review";
	}
	
	//구매 후기 상세보기
	@RequestMapping("/review_content.do")
	public String reviewContent(HttpServletRequest req, int review_num) {
		List<ReviewDTO> list = boardMapper.reviewContent(review_num);
	    req.setAttribute("reviewContent", list);
		return "merchant/store/board/review_content";
	}
}
