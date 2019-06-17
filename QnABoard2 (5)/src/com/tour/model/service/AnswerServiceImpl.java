package com.tour.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tour.exception.DataNotFoundException;
import com.tour.exception.DeleteException;
import com.tour.exception.RegistException;
import com.tour.exception.UpdateException;
import com.tour.model.domain.Answer;
import com.tour.model.domain.QnABoard;
import com.tour.model.repository.AnswerDAO;
import com.tour.model.repository.QnABoardDAO;

@Service
public class AnswerServiceImpl implements AnswerService {
	@Autowired
	private AnswerDAO answerDAO;
	@Autowired	
	private QnABoardDAO qnaBoardDAO;
	
	@Transactional
	@Override
	public void insert(Answer answer) throws RegistException, UpdateException{ // Transaction ���� �Ϸ�!
		System.out.println("AnswerServiceImpl : insert() ȣ��!!");
		int result = answerDAO.insert(answer);		// �亯���
		if(result == 0) {
			System.out.println("AnswerServiceImpl : insert() : ��Ͻ���!!");
			throw new RegistException("�亯�� ��� ����!!");
		}
		QnABoard qnaBoard = qnaBoardDAO.select(answer.getQnaBoard_id());
		qnaBoard.setAnswerState("���");
		int result2= qnaBoardDAO.updateAnswerState(qnaBoard);	// �亯 ���� ����
		//result2=0;
		if(result2 ==0) {
			System.out.println("AnswerServiceImpl : qnaBoardDAO.updateAnswerState() : ���� ���� ����!!");
			throw new UpdateException("���� ���� ����!");
		}
	}
	
	@Transactional
	@Override
	public void delete(Answer answer) throws DeleteException{			// Transaction ���� �Ϸ�!
		System.out.println("delete() : answer_id : "+answer.getAnswer_id());
		int result = answerDAO.delete(answer.getAnswer_id());
		if(result == 0) {
			throw new DeleteException("�亯�� ���� ����");
		}
		Answer getAnswer = answerDAO.select(answer.getQnaBoard_id());
		System.out.println("delete() : answer : "+getAnswer);
		QnABoard qnaBoard = qnaBoardDAO.select(answer.getQnaBoard_id());
		qnaBoard.setAnswerState(null);
		int result2 = qnaBoardDAO.updateAnswerState(qnaBoard);
		//result2=0;
		if(result2 == 0) {
			throw new UpdateException("�亯 ���� ���� ����!");
		}
	}

	@Override
	public Answer select(int qnaBoard_id) throws DataNotFoundException{
		Answer answer = answerDAO.select(qnaBoard_id);
		if(answer == null) {
			throw new DataNotFoundException("�亯�� ��ȸ ����");
		}
		return answer;
	}

	@Override
	public int update(Answer answer) throws UpdateException{
		int result = answerDAO.update(answer);
		if(result != 1) {
			throw new UpdateException("�亯�� ���� ����");
		}
		
		return result;
	}



}
