package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.form.Form;
import com.example.demo.form.Form.Search;
import com.example.demo.service.extend.Service;
import com.example.demo.util.Constants;

@org.springframework.stereotype.Controller
@RequestMapping
public class Controller {

	@Autowired
	private Service service;
	
	@GetMapping
	public String init(ModelMap model, @ModelAttribute("form") Form form) {
		model.put(Constants.TITLE_KEY, Constants.SEARCH_TITLE);
		return "search";
	}
	
	@PostMapping("/search")
	public String search(@Validated(Search.class) Form form, BindingResult error, RedirectAttributes redirect) {
		if(error.hasFieldErrors()) {
			StringBuilder errorMessage = new StringBuilder();
			for(var errors : error.getAllErrors()) {
				errorMessage.append(errors.getDefaultMessage());
			}
			redirect.addFlashAttribute("errorMessage", errorMessage.toString());
		}
		service.findByUserName(form.getFull_name());
		redirect.addFlashAttribute("formList", service.getDataList());
		redirect.addFlashAttribute("form", form);
		return "redirect:";
	}
}
