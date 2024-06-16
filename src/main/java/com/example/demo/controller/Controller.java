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

@org.springframework.stereotype.Controller
@RequestMapping
public class Controller {

	@Autowired
	private Service service;

	@GetMapping
	public String init(ModelMap model, @ModelAttribute(Service.FORM) Form form) {
		service.setCommonModel(model);
		return "search";
	}

	@PostMapping("/search")
	public String search(@Validated(Search.class) Form form, BindingResult error, RedirectAttributes redirect) {
		if (!service.existError(error, redirect)) {
			service.search(form, redirect);
		}
		
		return "redirect:";
	}

}
