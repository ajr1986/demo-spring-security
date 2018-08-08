package com.springboot.billing.app.model.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.springboot.billing.app.model.entity.Product;

public interface ProductDAO extends CrudRepository<Product, Long> {

//	@Query(value = "select p from Product p where p.name like %?1%")
//	public List<Product> findByName(String term);

	// This method do the same that commented method above
	public List<Product> findByNameLikeIgnoreCase(String term);
}
