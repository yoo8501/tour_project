package com.tour.model.gallery.repository;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tour.model.gallery.domain.Gallery;

@Repository
public class MybatisGalleryDAO implements GalleryDAO {
	@Autowired
	private SqlSessionTemplate sessionTemplate;

	public int insert(Gallery gallery) {
		return sessionTemplate.insert("Gallery.insert", gallery);
	}

	public int delete(int gallery_id) {
		return sessionTemplate.delete("Gallery.delete", gallery_id);
	}

	public int update(Gallery gallery) {
		return sessionTemplate.update("Gallery.update", gallery);
	}

	public int updateHit(int gallery_id) {
		return sessionTemplate.update("Gallery.updateHit", gallery_id);
	}

	public List selectAll() {
		System.out.println("DAO��� ");
		return sessionTemplate.selectList("Gallery.selectAll");
	}

	public Gallery select(int gallery_id) {
		return sessionTemplate.selectOne("Gallery.select", gallery_id);
	}

	// ------------------------- �˻� -------------------------
	// ��ü �˻�
	public List searchAll() {
		return sessionTemplate.selectList("Gallery.searchAll");
	}

	// �������� �˻�
	public List searchTitle(String searchText) {
		return sessionTemplate.selectList("Gallery.searchTitle", "%" + searchText + "%");
	}

	// �ۼ��ڷ� �˻�
	public List searchWriter(Gallery gallery) {
		return sessionTemplate.selectList("Gallery.searchWriter", gallery);
	}

	// ���� ������
	public List mainSelectAll() {
		return sessionTemplate.selectList("Gallery.mainSelectAll");
	}

	@Override
	public List<Gallery> getMyGallery(int member_id) {
		List<Gallery> galleryList = sessionTemplate.selectList("Gallery.selectMyGallery", member_id);
		return galleryList;
	}

}
