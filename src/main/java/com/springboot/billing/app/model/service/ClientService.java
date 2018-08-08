package com.springboot.billing.app.model.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springboot.billing.app.model.entity.Bill;
import com.springboot.billing.app.model.entity.Client;
import com.springboot.billing.app.model.entity.Product;

public interface ClientService {

	public List<Client> getClientsList();

	public void saveClient(Client client);

	public Client getClient(Long id);

	public void deleteClient(Long id);
	
	public Page<Client> getClientsList(Pageable pageable);
	
	public List<Product> getProductsList(String term);
	
	public void saveBill(Bill bill);
	
	public Product getProduct(Long id);
	
	public Bill getBill(Long id);
	
	public void removeBill(Long id);
}
