package com.springboot.billing.app.util.paginator;

public class PageItem {

	private int number;
	boolean isCurrent;

	public PageItem(int number, boolean isCurrent) {
		this.number = number;
		this.isCurrent = isCurrent;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public boolean isCurrent() {
		return isCurrent;
	}

	public void setCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}

}
