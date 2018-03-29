package com.javlec.springEx;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StudentController {
	
	@RequestMapping("/student/studentForm")
	public String studentForm() {
		return "createPage";
	}
	
	@RequestMapping("/student/create")
	public String studentCreate(@ModelAttribute("student") @Valid Student student, BindingResult result) {
		String page = "createDonePage";
		
//		StudentValidator validator = new StudentValidator();
//		validator.validate(student, result);
		if(result.hasErrors()) {
			page = "createPage";
		}
		
		return page;
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(new StudentValidator());
	}
}
