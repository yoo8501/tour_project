package com.tour.common.board;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
@Component
public class Pager {
	private int currentPage=1;																	// ���� Page
	private int totalRecord;																		// �� Record ��
	private int pageSize=10;																		// Page �� ������ Record ��
	private int totalPage;																			// �� Page ��
	private int blockSize=10;
	private int firstPage;																			// PageBlock ���� ù ������
	private int lastPage;																			// PageBlock ���� ������ ������
	private int curPos;
	private int num;
	
	// ȣ��� ����, Paging ó�� �������� ����� �Ѵ�.
	public void init(HttpServletRequest request, int totalRecord) {
		// ����ڰ� Page ��ȣ�� ������ �� ���� ��ȣ�� ������ ��ü!!!
		// ����ڰ� Page ��ȣ�� ������ ��� get ������� Data �� �Ѿ���� ������ request �� ���´�.
		if(request.getParameter("currentPage") != null) {
			currentPage = Integer.parseInt(request.getParameter("currentPage"));
		}
		this.totalRecord = totalRecord;
		totalPage = (int)Math.ceil((float)totalRecord/pageSize);		
		firstPage=currentPage-(currentPage-1)%blockSize;
		lastPage=firstPage+(blockSize-1);
		curPos = (currentPage-1)*pageSize;
		num = totalRecord-curPos;
	}

	
	
	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public int getFirstPage() {
		return firstPage;
	}

	public void setFirstPage(int firstPage) {
		this.firstPage = firstPage;
	}

	public int getLastPage() {
		return lastPage;
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

	public int getCurPos() {
		return curPos;
	}

	public void setCurPos(int curPos) {
		this.curPos = curPos;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
	
	
}
