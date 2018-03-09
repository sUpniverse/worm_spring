package com.javalec.springEx;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	@RequestMapping("/member/memberView")
	public String viewMember(HttpServletRequest request, Model model) {
		
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		
		model.addAttribute("id",id);
		model.addAttribute("pw", pw);
		
		
		return "/member/memberView";
		
	}
	
	@RequestMapping("/member/confirm")
	public String memberConfirm(@RequestParam("id") String id,@RequestParam("pw") String pw, Model model) {
		
		model.addAttribute("identify",id);
		model.addAttribute("password", pw);
		
		return "/member/confirm";
	}
	
//	@RequestMapping("/join/form")
//	public String  memberjoin(@RequestParam("name") String name,@RequestParam("id") String id,
//			@RequestParam("pw") String pw, @RequestParam("email") String email, Model model) {
//		
//		Member member = new Member();
//		member.setName(name);
//		member.setId(id);
//		member.setPw(pw);
//		member.setEmail(email);
//		
//		model.addAttribute("member",member);
//		
//		
//		return "/join/form";
//	}
	
	@RequestMapping("/join/form")
	public String  memberjoin(Member member) {		
		
		return "/join/form";
	}
	
	@RequestMapping("/member/{memberId}")
	public String getMember(@PathVariable String memberId, Model model ) {
		
		model.addAttribute("memberId",memberId);
		
		return "/member/memberOK";
	}
		
}
