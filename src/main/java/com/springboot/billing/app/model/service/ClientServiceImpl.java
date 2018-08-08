package com.springboot.billing.app.model.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.billing.app.model.dao.BillDAO;
import com.springboot.billing.app.model.dao.ClientDAO;
import com.springboot.billing.app.model.dao.ProductDAO;
import com.springboot.billing.app.model.entity.Bill;
import com.springboot.billing.app.model.entity.Client;
import com.springboot.billing.app.model.entity.Product;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientDAO clientDAO;

	@Autowired
	private ProductDAO productDAO;

	@Autowired
	private BillDAO billDAO;

	@Override
	@Transactional(readOnly = true)
	public List<Client> getClientsList() {

		return (List<Client>) clientDAO.findAll();
	}

	@Override
	@Transactional
	public void saveClient(Client client) {

		clientDAO.save(client);
	}

	@Override
	@Transactional(readOnly = true)
	public Client getClient(Long id) {

		Optional<Client> result = clientDAO.findById(id);

		Client client = null;
		if (result.isPresent()) {
			client = result.get();
		}

		return client;
	}

	@Override
	@Transactional
	public void deleteClient(Long id) {

		clientDAO.deleteById(id);
	}

	@Override
	public Page<Client> getClientsList(Pageable pageable) {

		return clientDAO.findAll(pageable);
	}

	@Override
	public List<Product> getProductsList(String term) {

		return productDAO.findByNameLikeIgnoreCase("%" + term + "%");
	}

	@Override
	@Transactional
	public void saveBill(Bill bill) {

		billDAO.save(bill);
	}

	@Override
	public Product getProduct(Long id) {

		Optional<Product> result = productDAO.findById(id);

		Product product = null;
		if (result.isPresent()) {
			product = result.get();
		}

		return product;
	}

	@Override
	public Bill getBill(Long id) {

		Optional<Bill> result = billDAO.findById(id);

		Bill bill = null;
		if (result.isPresent()) {
			bill = result.get();
		}

		return bill;
	}

	@Override
	@Transactional
	public void removeBill(Long id) {

		billDAO.deleteById(id);
	}

}
