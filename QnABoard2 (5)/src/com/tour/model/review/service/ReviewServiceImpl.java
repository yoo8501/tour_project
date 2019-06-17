package com.tour.model.review.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sun.javafx.scene.control.SelectedCellsMap;
import com.tour.exception.EditFailException;
import com.tour.exception.RegistFailException;
import com.tour.common.file.FileManager;
import com.tour.model.review.domain.Image;
import com.tour.model.review.domain.Path;
import com.tour.model.review.domain.Review;
import com.tour.model.review.repository.GoodDAO;
import com.tour.model.review.repository.ImageDAO;
import com.tour.model.review.repository.PathDAO;
import com.tour.model.review.repository.ReviewDAO;
import com.tour.model.review.repository.Review_commentDAO;

@Service
public class ReviewServiceImpl implements ReviewService {

	@Autowired
	public FileManager fileManager;

	@Autowired
	@Qualifier("mybatisReviewDAO")
	private ReviewDAO reviewDAO;

	@Autowired
	@Qualifier("mybatisImageDAO")
	private ImageDAO imageDAO;

	@Autowired
	@Qualifier("mybatisPathDAO")
	private PathDAO pathDAO;

	@Autowired
	@Qualifier("mybatisGoodDAO")
	private GoodDAO goodDAO;

	@Autowired
	@Qualifier("mybatisReview_commentDAO")
	private Review_commentDAO review_commentDAO;

	@Override
	public List selectAll() {
		// TODO Auto-generated method stub
		return reviewDAO.selectAll();
	}

	@Override
	public Review select(int review_id) {
		Review review = reviewDAO.select(review_id);
		return reviewDAO.select(review_id);
	}

	@Override
	public void update(Review review, Image image, Path path,String filePath) {
		System.out.println("���ƿ��� ��ε�" + path.getSelectedPath());
		System.out.println("���ƿ��� filePath"+filePath);
		System.out.println("���ƿ� �� ������ : "+review.getReview_id());
		
		int deleteResult = 0; //���� ������ �����ϴ� ��� ���ε�� ���� ����
		int result1 = 0; //���� ������ ������ ��� DB���� ����
		int result2 = 0; //���� �߰��� �̹��� �����
		int result3 = 0; //path���� ���
		int result4 = 0; //path���� ���
		int result5 = 0; //�� ���� ���
		
		System.out.println("������ ���� : "+image.getDeleteFile());
		
		if(image.getDeleteFile() != null) {
			for(int i=0; i<image.getDeleteFile().size(); i++) {
				String oriFile = image.getDeleteFile().get(i);
				System.out.println("������ ���� :"+oriFile);
				String newFile = fileManager.separateFile(image.getDeleteFile().get(i));
				System.out.println("�̸� �ٲ� ���� :"+newFile);
				
				File file = new File(filePath+"/"+newFile);
				System.out.println("file : "+file);
				if(file.delete()) {
					deleteResult = 1;
					System.out.println("file.delete()���");
					image.setFile_name(newFile);
					result1 = imageDAO.deleteFile(image);
					System.out.println("imageDAO.deleteFile()���");
				}else {
					deleteResult = 0;
				}
			}
		}else {
			result1 = 1;
			result2 = 1;
		}
		
		//���� �߰��� �̹��� ó��
		List<MultipartFile> myfile = image.getMyfile(); //���� ������ ���� ���
		File originFile = null; //���� ���ε带 ���� ���� ����
		System.out.println("myfile : "+myfile);
		System.out.println("myfile ������ : "+myfile.size());

		if(myfile.size() != 0) {
			for (int i = 0; i < myfile.size(); i++) {
				String filename = myfile.get(i).getOriginalFilename();
				originFile = new File(filePath + "/" + filename);
				System.out.println("originFile : "+originFile);
				try {
					myfile.get(i).transferTo(originFile);
					filename = fileManager.getFilename(originFile, filePath);
					if (filename != null) {
						image.setFile_name(filename);
						image.setReview(review);
						result2 = imageDAO.insert(image);
						if (result2 == 0) {
							throw new RegistFailException("�̹�����Ͽ� �����Ͽ����ϴ�.");
						}else {
							System.out.println("�̹��� ��� : "+result2);
							System.out.println("���� �̸�" + image.getFile_name());
						}
					}
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else {
			result2 = 1;
			result1 = 1;
		}
		
		//path ����
		JSONParser jsonParser = new JSONParser();
		JSONArray jsonArray = null;
		try {
			jsonArray = (JSONArray) jsonParser.parse(path.getSelectedPath());
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("review_id : "+review.getReview_id());
		result3 = pathDAO.deleteByReviewId(review.getReview_id());
		System.out.println("path ���� ��� : "+result3);
		
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			path.setLati((double) jsonObject.get("lati"));
			path.setLongi((double) jsonObject.get("longi"));
			path.setPath_name((String)jsonObject.get("path_name"));
			path.setReview(review);
			result4 = pathDAO.insert(path);
			System.out.println("path ��� ��� : "+result4);
			if (result4 == 0) {
				throw new RegistFailException("�н���Ͽ� �����Ͽ����ϴ�.");
			}
		}
		
		//�� ����
		System.out.println("������ ���� ���� : "+review.getReview_content());
		System.out.println("������ ������ ���� : "+review.getReview_title());
		
		result5 = reviewDAO.update(review);
		System.out.println("�� ���� ��� : "+result5);
		
	}

	// delete
	@Transactional(value = "transactionManager")
	@Override
	public void delete(int review_id, String path) {
		// �����ȿ��ִ� �̹������� ���� �����ϰ��� DB���� �����
		System.out.println("realpath : " + path);
		Review review = reviewDAO.select(review_id);
		List<Image> imageList = review.getImage();
		for (int i = 0; i < imageList.size(); i++) {
			String filename = imageList.get(i).getFile_name();
			File file = new File(path + "/" + filename);
			if (file.delete()) {
				int result1 = imageDAO.deleteByReviewId(review_id);
				System.out.println("�̹��� ���� ��� : " + result1);
			}
		}
		int result2 = pathDAO.deleteByReviewId(review_id);
		System.out.println("path ���� ��� : " + result2);
		int result3 = review_commentDAO.deleteByReviewId(review_id);
		System.out.println("��� ���� ��� : " + result3);
		int result4 = goodDAO.deleteByReviewId(review_id);
		System.out.println("���ƿ� ���� ��� : " + result4);
		int result5 = reviewDAO.delete(review_id);
		System.out.println(review_id + "�� �Խñ� ���� ��� : " + result5);
	}

	// insert
	@Transactional(value="transactionManager")
	public void insert(Review review, Image image, Path path, String filePath) {
		System.out.println("���ƿ��� ��ε�" + path.getSelectedPath());
		System.out.println("���ƿ��� filePath"+filePath);
		fileManager = new FileManager();
		List<MultipartFile> file = image.getMyfile();

		File originFile = null;

		JSONParser jsonParser = new JSONParser();
		JSONArray jsonArray = null;
		try {
			jsonArray = (JSONArray) jsonParser.parse(path.getSelectedPath());
			System.out.println(jsonArray.toString());
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		int result = reviewDAO.insert(review);
		if (result == 0) {
			throw new RegistFailException("�۵�Ͽ� �����Ͽ����ϴ�.");
		}
		int result2 = 0;
		int result3 = 0;

		for (int i = 0; i < file.size(); i++) {
			String filename = file.get(i).getOriginalFilename();
			originFile = new File(filePath + "/" + filename);
			try {
				file.get(i).transferTo(originFile);
				filename = fileManager.getFilename(originFile, filePath);
				if (filename != null) {
					image.setFile_name(filename);
					image.setReview(review);
					result2 = imageDAO.insert(image);
					if (result2 == 0) {
						throw new RegistFailException("�̹�����Ͽ� �����Ͽ����ϴ�.");
					}

					System.out.println("���� �̸�" + image.getFile_name());
				}
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			path.setLati((double) jsonObject.get("lati"));
			path.setLongi((double) jsonObject.get("longi"));
			path.setPath_name((String)jsonObject.get("path_name"));
			path.setReview(review);
			result3 = pathDAO.insert(path);
			if (result3 == 0) {
				throw new RegistFailException("�н���Ͽ� �����Ͽ����ϴ�.");
			}
		}
	}

	@Override
	public void updateHit(int review_id) throws EditFailException {
		int result = reviewDAO.updateHit(review_id);
		if (result == 0) {
			throw new EditFailException("��ȸ�� ������Ʈ ����");
		}
	}

	@Override
	public List selectByGood() {
		System.out.println("���ƿ�� �� ���� �������� ���� ����");
		return reviewDAO.selectByGood();
	}
}
