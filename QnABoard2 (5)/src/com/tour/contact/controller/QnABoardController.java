package com.tour.contact.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tour.common.board.Pager;
import com.tour.exception.DataNotFoundException;
import com.tour.exception.DeleteException;
import com.tour.exception.RegistException;
import com.tour.exception.UpdateException;
import com.tour.model.bulletin.domain.BulletinMember;

import com.tour.model.domain.QnABoard;
import com.tour.model.service.QnABoardPrivacyPassService;
import com.tour.model.service.QnABoardService;

@Controller
public class QnABoardController {
	@Autowired
	private QnABoardService boardService;
	@Autowired
	private QnABoardPrivacyPassService qnaBoard_privacy_pass_service;
	
	int count;
	
	
	// DB �� ��ϵǾ��ִ� ��� �Խñ� ��������
	//===========================================================
	@RequestMapping(value="/qnaBoards", method=RequestMethod.GET)
	public ModelAndView selectAll() {
		System.out.println("QnABoardController : selectAll() ȣ��!!");
		List<QnABoard> qnaBoardList = boardService.selectAll();
		System.out.println("������ �Խñ� ��ü ���� : "+qnaBoardList.size());
		
		Pager pager = new Pager();
		
		Object[] object = {qnaBoardList, pager};
		ModelAndView mav = new ModelAndView();
		mav.setViewName("chimper/chimper/contact/contact");
		mav.addObject("objects", object);
		
		return mav;
	}
	//===========================================================
	
	
	// �� 1�� ����ϱ�!!!
	//===========================================================
	@RequestMapping(value="/qnaBoards", method=RequestMethod.POST)
	public ModelAndView insert(QnABoard qnaBoard) {
		System.out.println("QnABoardController : insert() ȣ��!!");
		System.out.println("QnABoardController : insert() : qnaboard_privacy_id : "+qnaBoard.getQnaBoard_privacy_id());
		System.out.println("QnABoardController : insert() : member : "+qnaBoard.getMember_id());
		
		ModelAndView mav = new ModelAndView();
		if(qnaBoard.getQnaBoard_privacy_id().equals("2")) {
			int result = boardService.insert(qnaBoard);
			mav.setViewName("redirect:/qnaBoards");
		}else {
			System.out.println("QnABoardController : insert() : ����� ���� ���Դϴ�. ��й�ȣ ���� Page �� �̵��մϴ�.");
			mav.setViewName("chimper/chimper/contact/setBoardPass");
			mav.addObject("qnaBoard", qnaBoard);
		}
		
		return mav;
	}
	//===========================================================
	
	
	
	
	
	// �� 1�ǿ� ���� ���� ��������
	//===========================================================
	@RequestMapping(value="/qnaBoard", method=RequestMethod.GET)
	public ModelAndView select(int qnaBoard_id, HttpServletRequest request) {
		System.out.println("QnABoardControlle`r : select() ȣ��!!");
		QnABoard qnaBoard = boardService.select(qnaBoard_id);
		ModelAndView mav = new ModelAndView();
		System.out.println("QnABoardController : select() : qnaBoard_privacy_id : "+qnaBoard.getQnaBoard_privacy_id());
		
		// ȸ���� ��� �Խñ� �󼼺��� ó��
		//===================================================
		if(request.getSession().getAttribute("member")!=null) {
			BulletinMember member = (BulletinMember)request.getSession().getAttribute("member");
			
			if(qnaBoard.getQnaBoard_privacy_id().equals("1")) {
				if(member.getMemberLevel().getMember_level_id()==0) {
					if(!qnaBoard.getQnaBoard_writer().equals(member.getId())) {
						mav.setViewName("chimper/chimper/contact/confirmBoardPass");
						mav.addObject("qnaBoard", qnaBoard);
					}else {
						mav.setViewName("chimper/chimper/contact/detail");
						mav.addObject("qnaBoard", qnaBoard);
					}
				}else {
					mav.setViewName("chimper/chimper/contact/detail");
					mav.addObject("qnaBoard", qnaBoard);
				}
			}else {
				mav.setViewName("chimper/chimper/contact/detail");
				mav.addObject("qnaBoard", qnaBoard);
			}
		//===================================================	
			
		// ȸ���� �ƴҰ�� �� �󼼺��� ó��
		//===================================================
		}else {
			if(qnaBoard.getQnaBoard_privacy_id().equals("1")) {
				mav.setViewName("chimper/chimper/contact/confirmBoardPass");
				mav.addObject("qnaBoard", qnaBoard);
			}else {
				mav.setViewName("chimper/chimper/contact/detail");
				mav.addObject("qnaBoard", qnaBoard);
			}
		}
		//===================================================
		return mav;
	}
	//===========================================================
		
	
	
	
	
	// �� 1�� ��ȸ�� �ø���!!
	//===========================================================
	@RequestMapping(value="/upHit", method=RequestMethod.GET)
	public ModelAndView updateHit(int qnaBoard_id, HttpServletRequest request) {
		System.out.println("QnABoardController : updateHit() ȣ��!!");
		int result = boardService.updateHit(qnaBoard_id);
		//Member member = (Member)request.getSession().getAttribute("member");
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/qnaBoard?qnaBoard_id="+qnaBoard_id);
		
		return mav;
	}
	//===========================================================
	
	
	
	
	// �� 1�� �����ϱ�!!
	//===========================================================
	@RequestMapping(value="/qnaBoard/doDelete")
	public String delete(int qnaBoard_id) {
		System.out.println("QnABoardController : delete() ȣ��!!");
		QnABoard qnaBoard = boardService.select(qnaBoard_id);
		boardService.delete(qnaBoard_id);
		if(qnaBoard.getQnaBoard_privacy_id().equals("1")) {
			qnaBoard_privacy_pass_service.delete(qnaBoard_id);
		}
		
		return "redirect:/qnaBoards";
	}
	//===========================================================
	
	
	
	
	// �� 1�� �����ϱ�!!
	//===========================================================
	@RequestMapping(value="/qnaBoard/doUpdate")
	public ModelAndView update(QnABoard qnaBoard) {
		System.out.println("QnABoardController : update() ȣ��!!");
		System.out.println("���� qnaBoard �� qnaBoard_privacy_id : "+qnaBoard.getQnaBoard_privacy_id());
		boardService.update(qnaBoard);																									// �� ����!!
		ModelAndView mav = new ModelAndView();
		if(qnaBoard.getQnaBoard_privacy_id().equals("1")) {																		// ������ ���� privacy_id �� 1:����� ���
			System.out.println("QnABoardController : update() : ����� ��ȯ �� �Դϴ�.. ��й�ȣ ��� ������ �����ϴ�.. ��й�ȣ�� ����մϴ�.");
			mav.setViewName("chimper/chimper/contact/setBoardPass");														// ��ȣ ���� â���� �̵�
			mav.addObject("qnaBoard", qnaBoard);																						// �̵��ϸ鼭 qnaBoard ������ �ѱ�
		}else {
			if(qnaBoard_privacy_pass_service.select(qnaBoard.getQnaBoard_id())!=null) {
				qnaBoard_privacy_pass_service.delete(qnaBoard.getQnaBoard_id());
			}
			mav.setViewName("redirect:/qnaBoard?qnaBoard_id="+qnaBoard.getQnaBoard_id());
		}

		
		return mav;
	}
	//===========================================================
	
	
	
	
	
	
	// �Խñ� �˻�
	//====================================================================
	@RequestMapping(value="/searchQnaBoards", method=RequestMethod.GET)
	public ModelAndView searchQnaBoards(int searchMode, String searchWord) {
		System.out.println("QnABoardController : searchQnaBoards() ȣ��!!");
		List<QnABoard> qnaBoardList = boardService.searchQnaBoards(searchMode, searchWord);
		
		Pager pager = new Pager();
		
		Object[] object = {qnaBoardList, pager};
		ModelAndView mav = new ModelAndView();
		mav.setViewName("chimper/chimper/contact/contact");
		mav.addObject("objects", object);
		
		return mav;
	}
	//====================================================================
	
	
	
	
	
	
//	// �������������� ���� �� ������ �Խù� ��������!!
//	//====================================================================
//	@RequestMapping(value="/myQnaBoards", method=RequestMethod.GET)
//	public ModelAndView getMyQnaBoards(int member_id) {
//		System.out.println("QnABoardController : getMyQnaBoards() ȣ��!!");
//		List<QnABoard> qnaBoardList = boardService.getMyQnaBoards(member_id);
//		
//		Pager pager = new Pager();
//		
//		Object[] object = {qnaBoardList, pager};
//		ModelAndView mav = new ModelAndView();
//		mav.setViewName("chimper/chimper/myPage/myPageForm");
//		mav.addObject("objects", object);
//		
//		return mav;
//	}
//	//====================================================================
	
	
	
	
	
	
	
	
	
	// ���� ó��!!
	//============================================
	// �� ��ü �ҷ����� ���� ����ó��
	//===========================================================
	@ExceptionHandler(DataNotFoundException.class)
	@ResponseBody
	public ModelAndView loadBoardsException(DataNotFoundException e) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("error/errorPage");
		mav.addObject("error",e);
		
		return mav;
	}
	//===========================================================
	
	
	// �� 1�� ��Ͽ� ���� ����ó��
	//===========================================================
	@ExceptionHandler(RegistException.class)
	@ResponseBody
	public ModelAndView registFailException(RegistException e) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("error/errorPage");
		mav.addObject("error",e);
		
		return mav;
	} 
	//===========================================================
	
	
	// �� 1�� ������ ���� ���ó��
	//===========================================================
	@ExceptionHandler(DeleteException.class)
	@ResponseBody
	public ModelAndView deleteFailException(DeleteException e) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("error/errorPage");
		mav.addObject("error",e);
		
		return mav;
	} 
	//===========================================================

	
	// �� 1�� ������ ���� ���ó��
	//===========================================================
	@ExceptionHandler(UpdateException.class)
	@ResponseBody
	public ModelAndView updateFailException(UpdateException e) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("error/errorPage");
		mav.addObject("error",e);
		
		return mav;
	} 
	//===========================================================
	
	
	//============================================
}
