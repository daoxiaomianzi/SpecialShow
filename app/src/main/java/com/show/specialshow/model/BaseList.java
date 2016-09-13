package com.show.specialshow.model;

import java.io.Serializable;

public class BaseList implements Serializable {
	private static final long serialVersionUID = 537815780285619073L;
	private int pageNum;
	private int pageNow;
	private int pageSize;
	private int total;

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageNow() {
		return pageNow;
	}

	public void setPageNow(int pageNow) {
		this.pageNow = pageNow;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}
