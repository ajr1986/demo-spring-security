package com.springboot.billing.app.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "BILLS")
public class Bill implements Serializable {

	private static final long serialVersionUID = -3547750325845662878L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotEmpty
	private String description;

	private String comment;

	@Temporal(TemporalType.DATE)
	private Date date;

	@ManyToOne(fetch = FetchType.LAZY)
	private Client client;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "bill_id")
	private List<BillItem> billItemList;
	
	public Bill() {

		billItemList = new ArrayList<BillItem>();
	}

	@PrePersist
	public void prePersist() {
		date = new Date();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public List<BillItem> getBillItemList() {
		return billItemList;
	}

	public void setBillItemList(List<BillItem> billingItemList) {
		this.billItemList = billingItemList;
	}

	public void addBillItem(BillItem billItem) {
		billItemList.add(billItem);
	}
	
	public Double getTotalAmount() {
		
		Double totalAmount = Double.valueOf(0);
		
		for (BillItem billingItem : billItemList) {
			
			totalAmount += billingItem.getAmount();
		}
		
		return totalAmount;
	}
}
