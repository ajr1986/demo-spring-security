package com.springboot.billing.app.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.billing.app.model.entity.Client;
import com.springboot.billing.app.model.service.ClientService;
import com.springboot.billing.app.model.service.UploadFileService;
import com.springboot.billing.app.util.paginator.PageRender;

@Controller
@SessionAttributes("client")
public class ClientController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ClientService clientService;

	@Autowired
	private UploadFileService uploadFileService;

	@RequestMapping(value = { "/list", "/" }, method = RequestMethod.GET)
	public String getClients(Model model, @RequestParam(name = "p", defaultValue = "0") Integer page,
			Authentication authentication) {

		if (authentication != null) {
			logger.debug(authentication.getName());
		}

		// Authentication can be called like this to:
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// auth.getName();

		Pageable pageable = PageRequest.of(page, 5);

		Page<Client> clientPage = clientService.getClientsList(pageable);

		PageRender<Client> pageRender = new PageRender<Client>("/list", clientPage);

		model.addAttribute("title", "Clients");
		model.addAttribute("clients", clientPage);
		model.addAttribute("pageRender", pageRender);

		return "list";
	}

	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public String createClient(Map<String, Object> model) {

		Client client = new Client();

		model.put("title", "Add new client");
		model.put("client", client);

		return "form";
	}

	@RequestMapping(value = "/form/{id}", method = RequestMethod.GET)
	public String editClient(Map<String, Object> model, @PathVariable(value = "id") Long id, RedirectAttributes flash) {

		Client client = clientService.getClient(id);

		if (client == null) {
			flash.addFlashAttribute("error", "Client does not exist");
			return "redirect:/list";
		}

		model.put("title", "Edit client");
		model.put("client", client);

		return "form";
	}

	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String saveClient(@Valid Client client, BindingResult result, RedirectAttributes flash, Model model,
			SessionStatus status, @RequestParam(value = "file") MultipartFile photo) {

		model.addAttribute("title", "Client");

		if (result.hasErrors()) {
			flash.addFlashAttribute("error", "Error trying to save client data");
			return "form";
		}

		if (!photo.isEmpty()) {

			if (client.getId() != null && client.getPhoto() != null && !client.getPhoto().isEmpty()) {

				uploadFileService.delete(client.getPhoto());
			}

			try {

				String uniqueFileName = uploadFileService.copy(photo);
				client.setPhoto(uniqueFileName);

			} catch (IOException e) {

				logger.error("Error: was not possible upload file");
				flash.addFlashAttribute("error", "Error trying to upload photo");
			}

		}

		clientService.saveClient(client);
		status.setComplete();
		flash.addFlashAttribute("success", "Client saved successfully");
		return "redirect:list";
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String deleteClient(Model model, @PathVariable(value = "id") Long id, RedirectAttributes flash) {

		Client client = clientService.getClient(id);

		uploadFileService.delete(client.getPhoto());

		clientService.deleteClient(id);
		flash.addFlashAttribute("success", "Client removed successfully");
		return "redirect:/list";
	}

	@Secured( value = "ROLE_USER")
	@RequestMapping(value = "/details/{id}", method = RequestMethod.GET)
	public String detailsClient(Model model, @PathVariable(value = "id") Long id, RedirectAttributes flash) {

		Client client = clientService.getClient(id);

		if (client == null) {
			flash.addFlashAttribute("error", "Client does not exist");
			return "redirect:/list";
		}

		model.addAttribute("title", "Client Form");
		model.addAttribute("client", client);

		return "details";
	}

	@RequestMapping(value = "/uploads/{photo:.+}", method = RequestMethod.GET)
	public ResponseEntity<Resource> showPhoto(@PathVariable(name = "photo") String photo) {

		Resource resource = null;
		try {
			resource = uploadFileService.load(photo);

		} catch (MalformedURLException e) {

			logger.error("Error: was not possible load " + photo, e);
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

}
