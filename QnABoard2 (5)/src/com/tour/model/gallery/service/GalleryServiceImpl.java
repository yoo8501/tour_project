package com.tour.model.gallery.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tour.common.arrange.ListArrange;
import com.tour.common.file.FileManager;
import com.tour.exception.DeleteFailException;
import com.tour.exception.RegistFailException;
import com.tour.exception.SelectFailException;
import com.tour.exception.UpdateFailException;
import com.tour.model.bulletin.domain.BulletinMember;
import com.tour.model.bulletin.repository.BulletinMemberDAO;
import com.tour.model.gallery.domain.Gallery;
import com.tour.model.gallery.domain.Gallery_comment;
import com.tour.model.gallery.domain.Gallery_image;
import com.tour.model.gallery.repository.GalleryDAO;
import com.tour.model.gallery.repository.Gallery_commentDAO;
import com.tour.model.gallery.repository.Gallery_imageDAO;

@Service
public class GalleryServiceImpl implements GalleryService {
	@Autowired
	private GalleryDAO galleryDAO;
	@Autowired
	private Gallery_commentDAO gallery_commentDAO;
	@Autowired
	private Gallery_imageDAO gallery_imageDAO;

	@Autowired
	private BulletinMemberDAO bulletinMemberDAO;

	@Autowired
	FileManager fileManager;

	@Autowired
	ListArrange listArrange;

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Transactional(value = "transactionManager") // @Annotaion Ʈ����� ó��
	public void insert(Gallery gallery, Gallery_image gallery_image, String path) throws RegistFailException {
		System.out.println("Service insert");
		MultipartFile[] file = gallery_image.getMyFile();

//////////////////////////////////////// T E S T///////
		for (int i = 0; i < file.length; i++) {
			System.out.println("Service file" + file[i]);
		}
///////////////////////////////////////////////////////

		String[] filenameList = new String[file.length];
		File originFile = null;

////////////////////////////////////////////////////////////////////////
		int result = galleryDAO.insert(gallery);

		gallery_image.setGallery(gallery);
///////////////////////////////////////////////////////////////////////
		int result2 = 0; // gallery_imageDAO �� ������� ���� ����

		for (int i = 0; i < file.length; i++) {
			filenameList[i] = file[i].getOriginalFilename();
			String filename = filenameList[i];
			originFile = new File(path + "/" + filename);

			try {
				file[i].transferTo(originFile);
				filename = fileManager.getFilename(originFile, path);

				if (filename != null) {
					gallery_image.setFilename(filename);
//////////////////////////////////////////////////////////////////////////////////
					result2 = gallery_imageDAO.insert(gallery_image);
//////////////////////////////////////////////////////////////////////////////////

				}
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}

		if (result == 0) {
			throw new RegistFailException("������ ��Ͽ� �����Ͽ����ϴ�.");
		}
		if (result2 == 0) {
			throw new RegistFailException("���� ��Ͽ� �����Ͽ����ϴ�.");
		}
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Transactional(value = "transactionManager") // @Annotaion Ʈ����� ó��
	public void delete(int gallery_id, String path) throws DeleteFailException {

		List imageList = gallery_imageDAO.select(gallery_id);
		int result = 0;// ���ε� ���� ���� ����
		int result2 = 0;

		for (int i = 0; i < imageList.size(); i++) {
			Gallery_image image = (Gallery_image) imageList.get(i);
			String filename = image.getFilename();
			File file = new File(path + "/" + filename);

			if (file.delete()) {
				result = 1;
			} else {
				result = 0;
			}
		}
		if (result == 1) {
// �ش� �ۿ� ���� �̹��� ����
			result2 = gallery_imageDAO.deleteAll(gallery_id);
		}

		int result1 = 0;
		List list = gallery_commentDAO.selectAll(gallery_id);
// �ش� �ۿ� ���� ��ۻ���
		if (list.size() != 0) {
			result1 = gallery_commentDAO.deleteAll(gallery_id);
		} else {
			result1 = 1;
		}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// �ش� �� ����
		int result3 = galleryDAO.delete(gallery_id);

		if (result == 0) {
			System.out.println("GalleryServiceImpl------���ε����� ���� ����.");
			throw new DeleteFailException("�Խñ��� �����ϴµ� �����Ͽ����ϴ�.");
		}
		if (result1 == 0) {
			System.out.println("GalleryServiceImpl------��ۻ��� ����");
			throw new DeleteFailException("�Խñ��� �����ϴµ� �����Ͽ����ϴ�.");
		}
		if (result2 == 0) {
			System.out.println("GalleryServiceImpl------�̹��� ���� ����");
			throw new DeleteFailException("�Խñ��� �����ϴµ� �����Ͽ����ϴ�.");
		}
		if (result3 == 0) {
			System.out.println("GalleryServiceImpl------�Խñ� ���� ����");
			throw new DeleteFailException("�Խñ��� �����ϴµ� �����Ͽ����ϴ�.");
		}
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void updateHit(int gallery_id) {
		int result = galleryDAO.updateHit(gallery_id);
		if (result == 0) {
			throw new UpdateFailException("��ȸ�� ���� ����");
		}
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// ��ü �Խù� ��� ��������
	public List selectAll() throws SelectFailException {

		System.out.println("Service selectAll");
		List galleryList = galleryDAO.selectAll();
		System.out.println("Service DAO��� ");

		if (galleryList == null) {
			throw new SelectFailException("��ü �Խù� ����� �ҷ����µ� �����Ͽ����ϴ�.");
		}
		return galleryList;
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// �̹��� �����̸� ��������
	public List selectImage(int gallery_id) throws SelectFailException {

		List galleryImage = gallery_imageDAO.select(gallery_id);

		if (galleryImage == null) {
			throw new SelectFailException("�ش� �̹����� �ҷ����µ� �����Ͽ����ϴ�.");
		}

		return galleryImage;
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// �ش� �Խù� ��������
	public Gallery select(int gallery_id) throws SelectFailException {
		Gallery gallery = galleryDAO.select(gallery_id);
		if (gallery == null) {
			throw new SelectFailException("�ش� �Խù��� �������µ� �����Ͽ����ϴ�.");
		}
		return gallery;
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ��ü ����� �����´�
	public List selectComments(int gallery_id) {
		List gallery_comment = gallery_commentDAO.selectAll(gallery_id);
		System.out.println("Service selectComments" + gallery_id);
		if (gallery_comment == null) {
			throw new SelectFailException("�ش� �Խù��� ����� �������µ� �����Ͽ����ϴ�.");
		}
		return gallery_comment;
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// ��� ���
	public void insertConmments(Gallery_comment gallery_comment) {
		int result = gallery_commentDAO.insert(gallery_comment);
		if (result == 0) {
			throw new RegistFailException("��� ��� ����");
		}
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//��� ����
	public void updateComment(Gallery_comment gallery_comment) throws UpdateFailException {
		System.out.println("SERVICE / UPDATECOMMENT=" + gallery_comment.getContent());
		int result = gallery_commentDAO.update(gallery_comment);
		System.out.println("��� ���� update=" + result);

		if (result == 0) {
			throw new UpdateFailException("��� ���� �����Ͽ����ϴ�.");
		}
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//��� ����
	public void deleteComment(int comment_id) throws DeleteFailException {
		int result = gallery_commentDAO.delete(comment_id);

		if (result == 0) {
			throw new DeleteFailException("��� ������ �����Ͽ����ϴ�.");
		}
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// �Խù� ����
	@Transactional(value = "transactionManager") // @Annotaion Ʈ����� ó��
	public void update(Gallery gallery, Gallery_image gallery_image, String path) {

		System.out.println("Service//path = = =" + path);
		System.out.println("Service// myFile[]===" + gallery_image.getMyFile().length);
		System.out.println("//Service // gallery_id===" + gallery.getGallery_id());

		int deleteResult = 0; // ���� ���� ������ ��� ���ε�� ���� ����
		int result1 = 0;// ���� ���� ������ ��� DB���� ����
		int result2 = 0; // ���� �߰��� �̹��� �����
		int result3 = 0; // ���� �̹��� ��������� �����.

		if (gallery_image.getDeleteFile() != null) {
			for (int k = 0; k < gallery_image.getDeleteFile().length; k++) {
				String delFile = gallery_image.getDeleteFile()[k];
				System.out.println("====GalleryService==== ");
				System.out.println("Service ������ ���� �� =" + delFile);
				String delFile2 = fileManager.separateFile(gallery_image.getDeleteFile()[k]);
				File file = new File(path + "/" + delFile2);
				System.out.println("====GalleryService==== ");
				System.out.println("====File ��� !!!!!!!==== " + file);

				if (file.delete()) {
					deleteResult = 1;
					System.out.println("====GalleryService====  ");
					System.out.println("====delFile2==== " + delFile2);
					System.out.println("==================");
					gallery_image.setFilename(delFile2);
					result1 = gallery_imageDAO.deleteFile(gallery_image);
				} else {
					deleteResult = 0;
				}
			}
		} else {
			
			result1 = 1;
			System.out.println("������ �� ����.////////////////////////="+result1);
		}

// ���� �߰��� �̹��� ó��
		MultipartFile[] file = gallery_image.getMyFile();// ���� ������ ���� ���

		String[] filenameList = new String[file.length];// ������ �ִ� �����̸� ���� �迭

		File originFile = null;// ���� ���ε带 ���� ���� ����

		if (file.length>=1) {
			for (int i = 0; i < file.length; i++) {
				filenameList[i] = file[i].getOriginalFilename();

				originFile = new File(path + "/" + filenameList[i]);

				try {
					file[i].transferTo(originFile);
					String filename = fileManager.getFilename(originFile, path);

					if (filename != null) {
						gallery_image.setFilename(filename);
						gallery_image.setGallery(gallery);

						result2 = gallery_imageDAO.updateFile(gallery_image);
					}

				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else {
			result2=1;
			System.out.println("GalleryServiceImpl / result2 =="+result2);
			
		}

// �Խ��� ���� ���� ó��
		result3 = galleryDAO.update(gallery);

		System.out.println("Gallery-Service//�Խù� ������ ���� �������??" + result1);
		System.out.println("Gallery-Service//���ο� ���� �߰��� ���� �������??" + result2);
		System.out.println("Gallery-Service//���� ���� ���濡 ���� �������??" + result3);

		if (result1 == 0) {
			throw new UpdateFailException("���� �Խù� ������ �����Ͽ����ϴ�.");
		}
		if (result2 == 0) {
			throw new RegistFailException("���ο� ���� �߰��� �����Ͽ����ϴ�.");
		}
		if (result3 == 0) {
			throw new UpdateFailException("���� ���� ���濡 �����Ͽ����ϴ�.");
		}

	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////
// �Խ��� index.jsp �˻�ó�� 
	public List search(String searchType, String searchText) throws SelectFailException {
		List galleryList = new ArrayList();

		List memberList = new ArrayList();// �ۼ��� �˻�

		System.out.println("GalleryService / search / index =" + searchType + "/ data=" + searchText);

		if (searchType.equals("1")) {
			System.out.println("��ü �˻�");
			galleryList = galleryDAO.selectAll();

		} else if (searchType.equals("2")) {
			System.out.println("������ �˻�");
			galleryList = galleryDAO.searchTitle(searchText);

		} else if (searchType.equals("3")) {
			System.out.println("�ۼ��� �˻�");
			memberList = bulletinMemberDAO.selectMember(searchText);

			for (int i = 0; i < memberList.size(); i++) {
				BulletinMember one = (BulletinMember) memberList.get(i);
				Gallery gallery = new Gallery();
				gallery.setMember(one);

				List list = galleryDAO.searchWriter(gallery);

				galleryList = listArrange.ArrangeList(list, galleryList);
			}
			System.out.println("Gallery Service/ ���� /galleryList.size==" + galleryList.size());
		}

		if (galleryList == null) {
			throw new SelectFailException("�˻��� �ٽ� �õ����ּ���.");
		}
		return galleryList;
	}

//����������
	public List mainSelectAll() {

		List galleryList = galleryDAO.mainSelectAll();

		return galleryList;
	}

	public Gallery_image getGallery_id(int gallery_image_id) throws SelectFailException {

		Gallery_image gallery_image = gallery_imageDAO.getGallery_id(gallery_image_id);
		if (gallery_image == null) {
			throw new SelectFailException("���� ȭ�鿡 �̹����� �ҷ����µ� �����Ͽ����ϴ�.");
		}
		return gallery_image;
	}
}
