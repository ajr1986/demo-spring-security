package com.springboot.billing.app.model.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.springboot.billing.app.model.entity.Client;

public interface ClientDAO extends PagingAndSortingRepository<Client, Long> {

}
