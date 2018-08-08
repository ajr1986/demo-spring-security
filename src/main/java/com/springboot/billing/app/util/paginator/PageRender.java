package com.springboot.billing.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {

	private String url;
	private Page<T> page;

	private int totalPages;
	private int pageSize;

	private int currentPage;

	private List<PageItem> pages;

	public PageRender(String url, Page<T> page) {

		this.url = url;
		this.page = page;
		pages = new ArrayList<PageItem>();

		pageSize = page.getSize();
		totalPages = page.getTotalPages();
		currentPage = page.getNumber() + 1;

		int from;
		int to;

		if (totalPages <= pageSize) {

			from = 1;
			to = totalPages;

		} else if (currentPage <= pageSize / 2) {

			from = 1;
			to = pageSize;

		} else if (currentPage >= totalPages - pageSize / 2) {

			from = totalPages - pageSize + 1;
			to = pageSize;

		} else {

			from = currentPage - pageSize / 2;
			to = pageSize;
		}

		for (int i = 0; i < to; i++) {

			pages.add(new PageItem(from + i, currentPage == from + i));
		}
	}

	public String getUrl() {
		return url;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public List<PageItem> getPages() {
		return pages;
	}

	public boolean isFirst() {
		return page.isFirst();
	}

	public boolean isLast() {
		return page.isLast();
	}

	public boolean isHasNext() {
		return page.hasNext();
	}
	
	public boolean isHasPrevious() {
		return page.hasPrevious();
	}
}
