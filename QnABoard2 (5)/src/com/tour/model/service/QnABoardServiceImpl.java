package com.tour.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tour.exception.DataNotFoundException;
import com.tour.exception.DeleteException;
import com.tour.exception.RegistException;
import com.tour.exception.UpdateException;
import com.tour.model.domain.QnABoard;
import com.tour.model.repository.AnswerDAO;
import com.tour.model.repository.QnABoardDAO;

@Service
public class QnABoardServiceImpl implements QnABoardService {
	@Autowired
	@Qualifier("mybatisQnABoardDAO")
	private QnABoardDAO boardDAO;
	
	@Autowired
	private AnswerDAO answerDAO;
	
	
	
	
	@Override
	public List<QnABoard> selectAll() throws DataNotFoundException{
		List<QnABoard> qnaBoardList = boardDAO.selectAll();
		if(qnaBoardList == null) {
			throw new DataNotFoundException("�� ��ȸ�� ������ ������ϴ�. �����ڿ��� �����ϼ���.");
		}
		return qnaBoardList;
	}

	@Override
	public int insert(QnABoard qnaBoard) throws RegistException{
		int result=0;
		if(qnaBoard.getQnaBoard_content()!=null) {
			result = boardDAO.insert(qnaBoard);
		}
		if(result == 0) {
			throw new RegistException("�Խñ� ������ �����ϴ�!");
		}
		return result;
	}

	@Override
	public QnABoard select(int qnaBoard_id) throws DataNotFoundException{
		QnABoard qnaBoard = boardDAO.select(qnaBoard_id);
		if(qnaBoard == null) {
			throw new DataNotFoundException("�Խñ� ��ȸ ����!, �����ڿ��� �����ϼ���");
		}
		return qnaBoard;
	}

	
	@Transactional
	@Override
	public int delete(int qnaBoard_id) throws DeleteException{
		QnABoard qnaBoard = boardDAO.select(qnaBoard_id);
		int result = boardDAO.delete(qnaBoard_id);
		if(result == 0) {
			throw new DeleteException("�Խñ� ���� ����, �����ڿ��� �����ϼ���!");
		}
		if(qnaBoard.getQnaBoard_type_id().equals("2")) {
			if(qnaBoard.getAnswerState() != null) {
				int result2 = answerDAO.delete2(qnaBoard_id);
				if(result2==0) {
					throw new DeleteException("�亯�� ���� ����, �����ڿ��� �����ϼ���!");
				}else {
					result=result2;
				}
			}
		}
		return result;
	}

	@Override
	public int update(QnABoard qnaBoard) throws UpdateException{
		int result = boardDAO.update(qnaBoard);
		if(result == 0) {
			throw new UpdateException("�Խñ� ���� ���� ����!!, �����ڿ��� �����ϼ���!");
		}
		return result;
	}

	@Override
	public int updateHit(int qnaBoard_id) throws UpdateException{
		int result = boardDAO.updateHit(qnaBoard_id);
		//result = 0;
		if(result == 0) {
			throw new UpdateException("�Խñ� ��ȸ�� ���� ����!!, �����ڿ��� �����ϼ���!");
		}
		return result;
	}

	@Override
	public int updateAnswerState(QnABoard qnaBoard) throws UpdateException{
		int result = boardDAO.updateAnswerState(qnaBoard);
		//int result = 0;
		if(result == 0) {
			throw new UpdateException("�Խñ� �亯 ���� ���� ����!!, �����ڿ��� �����ϼ���!");
		}
		return result;
	}
	
	// ������ �Խñ� �˻�!!
	//=============================================================
	@Override
	public List<QnABoard> searchQnaBoards(int searchMode, String searchWord) {
		List<QnABoard> qnaBoardList = boardDAO.searchQnaBoards(searchMode, searchWord);
		return qnaBoardList;
	}
	//=============================================================

}
