package com.daycodeday.demo.mvc.action;

import com.daycodeday.demo.service.IDemoService;
import com.daycodeday.spring.annotation.Autowrited;
import com.daycodeday.spring.annotation.Controller;
import com.daycodeday.spring.annotation.RequestMapping;

@Controller
public class MyAction {

		@Autowrited
		IDemoService demoService;
	
		@RequestMapping("/index.html")
		public void query(){

		}
	
}
