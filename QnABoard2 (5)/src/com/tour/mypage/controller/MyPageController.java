package com.tour.mypage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.tour.common.board.Pager;
import com.tour.model.domain.QnABoard;
import com.tour.model.service.MyPageService;

/*
 * ���������� ���� �ۼ��� �Խñ� �������� ���� Ŭ���� (����ȣ : 190503)
 */

@Controller
public class MyPageController {
	@Autowired
	private MyPageService myPageService;
	
	@RequestMapping(value="/myBoards", method=RequestMethod.GET)
	public ModelAndView selectAll(int member_id) {
		System.out.println("MyPageController : selectAll() ȣ��!!");
		List myBoards = myPageService.selectAll(member_id);
		
		Pager pager = new Pager();
		
		Object[] object = {myBoards, pager};
		ModelAndView mav = new ModelAndView();
		mav.setViewName("chimper/chimper/myPage/myPageForm");
		mav.addObject("objects", object);
		
		return mav;
	}
}
