package com.tour.contact.controller;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tour.exception.DataNotFoundException;
import com.tour.exception.DeleteException;
import com.tour.exception.RegistException;
import com.tour.exception.UpdateException;
import com.tour.model.domain.Answer;
import com.tour.model.service.AnswerService;
import com.tour.model.service.QnABoardService;

@Controller
public class AnswerController {
	@Autowired
	private AnswerService answerService;
	@Autowired
	private QnABoardService qnaBoardService;
	@Autowired
	private SqlSessionTemplate sqlsessiontemplate;

	
	// �亯�� ���� ����ϱ�!!
	// ========================================================================
	@RequestMapping(value = "/Answer", method = RequestMethod.POST)
	@ResponseBody
	public String registAnswer(Answer answer) {
		System.out.println("AnswerController : registAnswer() ȣ��!!");
		System.out.println("AnswerController : registAnswer() : qnaBoard_id : " + answer.getQnaBoard_id());
		System.out.println("AnswerController : registAnswer() : answer_content : " + answer.getAnswer_content());
		System.out.println("AnswerController : registAnswer() : answer_writer : " + answer.getAnswer_writer());

		if (answer.getAnswer_content().equals("")) {
			return "{\"result\":0}";
		}
		System.out.println("answerService : "+answerService.getClass().getCanonicalName());
		answerService.insert(answer);//�亯 ���
		
		return "{\"result\":1}";
	}
	// ========================================================================

	// �亯�� ���� ��������
	// ========================================================================
	@RequestMapping(value = "/Answer", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Answer getAnswer(int qnaBoard_id) {
		System.out.println("AnswerController : getAnswer() ȣ��!!");
		System.out.println("AnswerController : getAnswer() : qnaboard_id : " + qnaBoard_id);

		Answer answer = answerService.select(qnaBoard_id);

		return answer;
	}
	// ========================================================================

	// �亯�� ���� �����ϱ�
	// ========================================================================
	@RequestMapping(value = "/doUpdateAnswer", method = RequestMethod.POST)
	@ResponseBody
	public String updateAnswer(Answer answer) {
		System.out.println("AnswerController : updateAnswer() ȣ��!!");
		String allresult = "";
		int result = answerService.update(answer);

		return "{\"result\":1}";
	}
	// ========================================================================

	// �亯�� ���� �����ϱ�
	// ========================================================================
	@RequestMapping(value = "/doDeleteAnswer", method = RequestMethod.POST)
	@ResponseBody
	public String deleteAnswer(Answer answer) {
		System.out.println("AnswerController : deleteAnswer() ȣ��!!");
		answerService.delete(answer);
	

		return "{\"result\":1}";
	}
	// ========================================================================

	// ����ó��!!
	// ================================================
	// �亯�� ��� Exception ó��
	@ExceptionHandler(RegistException.class)
	@ResponseBody
	public String answerRegistFail(RegistException e) {
		
		return "{\"result\":0}";
	}

	
	 // �亯�� ����, �Խñ� �亯 ���� Exception ó��
	 @ExceptionHandler(UpdateException.class)
	 @ResponseBody
	 public String answerUpdateFail(UpdateException e) {
		
		 return "{\"result\":0}"; 
	 }
	 

	// �亯�� ���� Exception ó��
	@ExceptionHandler(DeleteException.class)
	@ResponseBody
	public String answerDeleteFail(DeleteException e) {
		
		return "{\"result\":0}";
	}

	// �亯�� ��ȸ Exception ó��
	@ExceptionHandler(DataNotFoundException.class)
	public ModelAndView answerSelectFail(DataNotFoundException e) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("error/errorPage");
		mav.addObject("error", e);

		return mav;
	}
	// ================================================
}
