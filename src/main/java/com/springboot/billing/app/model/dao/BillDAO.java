package com.springboot.billing.app.model.dao;

import org.springframework.data.repository.CrudRepository;

import com.springboot.billing.app.model.entity.Bill;

public interface BillDAO extends CrudRepository<Bill, Long> {

}
