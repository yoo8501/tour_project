package com.tour.model.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tour.model.bulletin.domain.BulletinBoard;
import com.tour.model.bulletin.repository.BulletinBoardDAO;
import com.tour.model.domain.QnABoard;
import com.tour.model.gallery.domain.Gallery;
import com.tour.model.gallery.repository.GalleryDAO;
import com.tour.model.repository.QnABoardDAO;
import com.tour.model.review.domain.Review;
import com.tour.model.review.repository.ReviewDAO;

/*
 * ���������� ���� �ۼ��� �Խñ� �������� ���� �� �Խ��� DAO �� ���� ��� ������ (����ȣ : 190503)
 */
@Service
public class MyPageServiceImpl implements MyPageService {
	
	@Autowired
	private QnABoardDAO qnaBoardDAO;					// ������ �Խ��� �Խñ� ��������
	@Autowired	
	private BulletinBoardDAO bulletinBoardDAO;		// �����Խ��� �Խñ� ��������
	@Autowired
	private GalleryDAO galleryDAO; //������ �Խñ� ��������
	@Autowired
	private ReviewDAO reviewDAO; //���� �Խñ� ��������
	
	// ***�Ʒ� ���� �Խ��� �׸��� DAO �� �߰� �ϼ���
	//==============================
	// ���� �ı� �Խ��� �Խñ� ��������
	// ������ �Խ��� �Խñ� ��������
	//==============================

	@Override
	public List selectAll(int member_id) {
		List myBoards = new ArrayList();
		
		List<QnABoard> qnaBoardList = qnaBoardDAO.getMyQnaBoards(member_id);
		List<BulletinBoard> bulletinBoardList = bulletinBoardDAO.getMyBulletinBoards(member_id);
		List<Gallery> galleryList=galleryDAO.getMyGallery(member_id);
		List<Review> reviewList=reviewDAO.getMyReview(member_id);
		
		// ***�Ʒ� ���� �Խ��� �׸��� DAO �۾��� �����ϼ���
		//==============================
		// ���� �ı� �Խ��� �Խñ� ��������
		// ������ �Խ��� �Խñ� ��������
		//==============================
		
		
		myBoards.add(qnaBoardList);
		myBoards.add(bulletinBoardList);
		myBoards.add(galleryList);
		myBoards.add(reviewList);
		
		return myBoards;
	}

}
