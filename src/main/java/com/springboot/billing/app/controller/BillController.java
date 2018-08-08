package com.springboot.billing.app.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.billing.app.model.entity.Bill;
import com.springboot.billing.app.model.entity.BillItem;
import com.springboot.billing.app.model.entity.Client;
import com.springboot.billing.app.model.entity.Product;
import com.springboot.billing.app.model.service.ClientService;

@Controller
@RequestMapping(value = "/bill")
@SessionAttributes(value = "bill")
public class BillController {

	@Autowired
	private ClientService clientService;

	@GetMapping("/form/{clientId}")
	public String create(@PathVariable(value = "clientId") Long clientId, Model model, RedirectAttributes flash) {

		Client client = clientService.getClient(clientId);

		if (client == null) {

			flash.addFlashAttribute("error", "Client does not exist");
			return "redirect:/list";
		}

		Bill bill = new Bill();
		bill.setClient(client);

		model.addAttribute("bill", bill);
		model.addAttribute("title", "New Bill");

		return "bill/form";
	}

	@GetMapping(value = "/load-products/{term}", produces = { "application/json" })
	public @ResponseBody List<Product> loadProducts(@PathVariable(value = "term") String term) {
		return clientService.getProductsList(term);
	}

	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String saveBill(@Valid Bill bill, BindingResult result, RedirectAttributes flash, SessionStatus status,
			Model model, @RequestParam(name = "item_id[]", required = false) Long[] productIdList,
			@RequestParam(name = "quantity[]", required = false) Integer[] quantityList) {

		if (result.hasErrors()) {
			model.addAttribute("error", "Error trying to save bill");
			model.addAttribute("title", "New Bill");
			return "bill/form";
		}

		if (productIdList == null || productIdList.length == 0) {
			model.addAttribute("error", "Bill has not related products");
			model.addAttribute("title", "New Bill");
			return "bill/form";
		}

		for (int i = 0; i < productIdList.length; i++) {

			Product product = clientService.getProduct(productIdList[i]);

			BillItem billItem = new BillItem();
			billItem.setProduct(product);
			billItem.setQuantity(quantityList[i]);

			bill.addBillItem(billItem);
		}

		clientService.saveBill(bill);
		status.setComplete();

		flash.addFlashAttribute("success", "Bill saved successfully");

		return "redirect:/details/" + bill.getClient().getId();
	}

	@RequestMapping(value = "/details/{id}")
	public String showDetails(@PathVariable(name = "id") Long id, Model model, RedirectAttributes flash) {

		Bill bill = clientService.getBill(id);

		if (bill == null) {

			flash.addFlashAttribute("error", "Bill does not exist");
			return "redirect:/list";
		}

		model.addAttribute("bill", bill);
		model.addAttribute("title", "Bill: " + bill.getDescription());

		return "bill/details";
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String removeBill(@PathVariable(name = "id") Long id, Model model, RedirectAttributes flash) {
		
		Bill bill = clientService.getBill(id);
		
		if(bill == null) {
			flash.addFlashAttribute("error", "Bill does not exist");
			return "redirect:/list";
		}
		
		clientService.removeBill(id);
		flash.addFlashAttribute("success", "Bill removed successfully");
		
		return "redirect:/details/" + bill.getClient().getId();
	}
}
