package com.tour.review.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tour.common.board.ReviewPager;
import com.tour.exception.DeleteFailException;
import com.tour.exception.RegistFailException;
import com.tour.model.review.domain.Good;
import com.tour.model.review.domain.Review;
import com.tour.model.review.domain.Review_comment;
import com.tour.model.review.service.GoodService;
import com.tour.model.review.service.ReviewService;
import com.tour.model.review.service.Review_commentService;

@RestController
public class RestReviewController {
	
	@Autowired
	private Review_commentService review_commentService;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private GoodService goodService;
	

/////////////////////////////////////mainȭ�� ��ȸ/////////////////////////////////////	
	//���ƿ� ������ �Խñ� ��������(5��)
	
	@RequestMapping(value="/reviews/good", method=RequestMethod.GET)
	public JSONObject selectByGood() {
		System.out.println("���ƿ�� �Խ� �� �������� ��Ʈ�ѷ� ����");
		JSONObject jsonObject = new JSONObject();
		List reviewList = reviewService.selectByGood();
		jsonObject.put("reviewList", reviewList);
		jsonObject.put("resultCode", 1);
		return jsonObject;
	}
	 
	
	
/////////////////////////////////////review ����/////////////////////////////////////
	//���ƿ� insert
	@RequestMapping(value="/review/good", method=RequestMethod.POST)
	public JSONObject goodInsert(Good good) {
		System.out.println("���ƿ並 ���� member_id : "+good.getMember().getMember_id());
		System.out.println("���ƿ並 ���� review_id : "+good.getReview().getReview_id());
		JSONObject jsonObject = new JSONObject();
		goodService.insert(good);
		jsonObject.put("resultCode", 1);
		return jsonObject;
	}
	
	//�ش� �Խù��� ���ƿ� ��������
	@RequestMapping(value="/review/good/{review_id}", method = RequestMethod.GET)
	public List goodselect(@PathVariable("review_id") int review_id) {
		System.out.println(review_id+"�� ���� ���ƿ�� ��ü ��������");
		List<Good> list = goodService.selectByReviewId(review_id);
		return list;
	}
	
	//�ش� �Խù��� �ش� ������ ���ƿ� ������Ű��
	@RequestMapping(value="/review/good/{good_id}", method = RequestMethod.DELETE)
	public JSONObject goodDelete(@PathVariable("good_id") int good_id) {
		System.out.println(good_id+"���ƿ� �����ϱ�");
		JSONObject jsonObject = new JSONObject();
		goodService.delete(good_id);
		jsonObject.put("resultCode", 1);
		return jsonObject;
	}

///////////////////////////////////review_comment����////////////////////////////
	//��� ���
	@RequestMapping(value="/review_comments", method=RequestMethod.POST)
	public JSONObject insert(Review_comment review_comment) {
		System.out.println("��۵���� �� review id : "+review_comment.getReview().getReview_id());
		JSONObject jsonObject = new JSONObject();
		review_commentService.insert(review_comment);
		jsonObject.put("resultCode", 1);
		return jsonObject;
	}
	
	//�� ���� ��� ��� ��ü��������
	@RequestMapping(value="/review_comments/{review_id}", method = RequestMethod.GET)
	public JSONObject commentSelectAll(@PathVariable("review_id") int review_id, HttpServletRequest request) {
		System.out.println("����� ��û�� �� review_id : "+review_id);
		JSONObject jsonObject = new JSONObject();
		ReviewPager pager = new ReviewPager();
		List<Review_comment> review_commentList = review_commentService.selectByReviewId(review_id);
		pager.init(request, review_commentList, 10, 10);
		jsonObject.put("review_commentList", review_commentList);
		jsonObject.put("pager", pager);
		return jsonObject;
	}
	
	
	//��� �� �� ��������
	@RequestMapping(value="/review_comment/{review_comment_id}", method=RequestMethod.GET)
	public JSONObject commentSelect(@PathVariable("review_comment_id") int review_comment_id) {
		System.out.println("��� �� �� �������� ���� id : "+review_comment_id);
		JSONObject jsonObject = new JSONObject();
		Review_comment review_comment = review_commentService.select(review_comment_id);
		jsonObject.put("review_comment_id", review_comment.getReview_comment_id());
		jsonObject.put("member_id", review_comment.getMember().getMember_id());
		jsonObject.put("review_comment_content", review_comment.getReview_comment_content());
		jsonObject.put("review_id", review_comment.getReview().getReview_id());
		jsonObject.put("review_comment_regdate", review_comment.getReview_comment_regdate());
		return jsonObject;
	}
	
	//��� ����
	@RequestMapping(value="/review_comments/{review_comment_id}", method=RequestMethod.DELETE)
	public JSONObject commentDelete(@PathVariable("review_comment_id") int review_comment_id) {
		System.out.println("����� �����ϱ� ���� �Ѱ� ���� id : "+review_comment_id);
		JSONObject jsonObject = new JSONObject();
		review_commentService.delete(review_comment_id);
		jsonObject.put("resultCode", 1);
		return jsonObject;
	}
	
	//��� ����
	@RequestMapping(value="/review_comments", method=RequestMethod.PUT)
	public JSONObject commentEdit(Review_comment review_comment) {
		System.out.println("��� �����ϱ� ���� �Ѱܹ��� �ڸ�Ʈ id : "+review_comment.getReview_comment_id());
		System.out.println("��� �����ϱ� ���� �Ѱܹ��� �ڸ�Ʈ id : "+review_comment.getReview_comment_content());
		JSONObject jsonObject = new JSONObject();
		review_commentService.update(review_comment);
		jsonObject.put("resultCode", 1);
		return jsonObject;
	}
	
	///////////////////////////////////review����////////////////////////////
	

	
	
	///////////////////////CRUD ���� ó��////////////////////////////
	
	//��� ����
	@ExceptionHandler(RegistFailException.class)
	public JSONObject registFail(RegistFailException e) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("resultCode", e.getMessage());
		return jsonObject;
	}
	
	//���� ����
	@ExceptionHandler(DeleteFailException.class)
	public JSONObject deleteFail(DeleteFailException e) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("resultCode", e.getMessage());
		return jsonObject;
	}
	
}


