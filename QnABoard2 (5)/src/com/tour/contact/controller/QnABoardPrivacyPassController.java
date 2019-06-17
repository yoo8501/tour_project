package com.tour.contact.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.tour.exception.PrivacyRegistFailException;
import com.tour.exception.RegistException;
import com.tour.model.domain.QnABoard;
import com.tour.model.domain.QnABoardPrivacyPass;
import com.tour.model.service.QnABoardPrivacyPassService;
import com.tour.model.service.QnABoardService;

@Controller
public class QnABoardPrivacyPassController {
	@Autowired
	private QnABoardPrivacyPassService qnaBoard_privacy_pass_service;
	@Autowired
	private QnABoardService qnaBoardService;
	
	
	// ������� ��й�ȣ ��ȸ
	//======================================================================================================================
	@RequestMapping(value="/QnABoardPrivacyPass", method=RequestMethod.GET)
	public ModelAndView select(int qnaBoard_id, String privacy_pass) {
		System.out.println("QnABoardPrivacyPassController : select() : ���� qnaBoard_id : "+qnaBoard_id);
		System.out.println("QnABoardPrivacyPassController : select() : ���� privacy_pass : "+privacy_pass);
		String pass = qnaBoard_privacy_pass_service.select(qnaBoard_id);
		System.out.println("QnABoardPrivacyPassController : select() : ��ȸ�� �Խñ� ��ȣ : "+pass);
		QnABoard qnaBoard = qnaBoardService.select(qnaBoard_id);
		ModelAndView mav = new ModelAndView();
		if(pass==null || !pass.equals(privacy_pass)) {
			System.out.println("QnABoardPrivacyPassController : checkPass() : ��й�ȣ ����ġ!!");
			mav.setViewName("chimper/chimper/contact/passUncorrect");
		}else if(pass.equals(privacy_pass)) {
			System.out.println("QnABoardPrivacyPassController : checkPass() : ��й�ȣ ��ġ!!");
			mav.setViewName("chimper/chimper/contact/detail");
			mav.addObject("qnaBoard", qnaBoard);
		}
		return mav;
	}
	//======================================================================================================================
	
	
	
	
	
	// ������� ��й�ȣ ����
	//======================================================================================================================
	@RequestMapping(value="/QnABoardPrivacyPass", method=RequestMethod.POST)
	public String insert(QnABoardPrivacyPass qnaBoard_privacy_pass, HttpServletRequest request) {
		QnABoard qnaBoard = (QnABoard) request.getSession().getAttribute("qnaBoard");															// ���ǿ� ����ִ� qnaBoard ������ ����
		System.out.println("QnABoardPrivacyPassController : insert() : ���� qnaBoard_privacy_pass : "+qnaBoard_privacy_pass);		// qnaBoard ���� Ȯ��
		System.out.println("QnABoardPrivacyPassController : insert() : ���� qnaBoard_privacy_pass �� qnaBoard_id : "+qnaBoard_privacy_pass.getQnaBoard_id());
		if(qnaBoard_privacy_pass.getQnaBoard_id()!=0) {																										// board_id �� �ʱⰪ 0�� �ƴ϶�� ���� �Խñ�
			if(qnaBoard_privacy_pass_service.select(qnaBoard_privacy_pass.getQnaBoard_id())==null) {										// ��б� ��й�ȣ ������ �Խñ� board_id �� ��ȸ�� ����� ������ ó������ ��б��� �Ǵ±�
				System.out.println("QnABoardPrivacyPassController : insert() : ó�� ��б��� �Ǵ� ���Դϴ�. ��й�ȣ ������ ����մϴ�."); 			
				qnaBoard_privacy_pass_service.insert(qnaBoard_privacy_pass);																				// qnaBoard �� pass �� qnaBoard ������ DB �� insert
				System.out.println("QnABoardPrivacyPassController : insert() : ������� ��� ����!");
			}else {
				System.out.println("QnABoardPrivacyPassController : insert() : �̹� ��б��̾��� �Խñ��Դϴ�. ���� ��й�ȣ ������ �����ϰ� ���ο� ��й�ȣ�� �����մϴ�."); 
				qnaBoard_privacy_pass_service.delete(qnaBoard_privacy_pass.getQnaBoard_id());									
				qnaBoard_privacy_pass_service.insert(qnaBoard_privacy_pass);
				System.out.println("QnABoardPrivacyPassController : insert() : ��й�ȣ �缳�� �Ϸ�"); 
			}
		}else {	
			System.out.println("QnABoardPrivacyPassController : insert() : ���� �ۼ��Ǵ� ���̸鼭 ��б۷� �����ϴ� �Խù��Դϴ�.");
			qnaBoardService.insert(qnaBoard);				
			qnaBoard_privacy_pass.setQnaBoard_id(qnaBoard.getQnaBoard_id());
			qnaBoard_privacy_pass_service.insert(qnaBoard_privacy_pass);
			System.out.println("QnABoardPrivacyPassController : insert() : �� ��� �� ��й�ȣ ��� ����!");
		}
		
		return "redirect:/qnaBoards";
	}
	//======================================================================================================================
	
	
	
	
	
	
	// ����ó��
	//=================================================================
	// ��й�ȣ ���� ��� ���� ����ó��
	@ExceptionHandler(PrivacyRegistFailException.class)
	public ModelAndView privacyRegistFail(PrivacyRegistFailException e) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("error/errorPage");
		mav.addObject("error", e);
		return mav;
	}
	
	// �� ���� ��� ���� ����ó��
	@ExceptionHandler(RegistException.class)
	public ModelAndView qnaBoardRegistFail(RegistException e) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("error/errorPage");
		mav.addObject("error", e);
		return mav;
	}
	//=================================================================
}
