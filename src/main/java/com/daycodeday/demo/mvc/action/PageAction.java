package com.daycodeday.demo.mvc.action;

import com.daycodeday.demo.service.IQueryService;
import com.daycodeday.spring.annotation.Autowrited;
import com.daycodeday.spring.annotation.Controller;
import com.daycodeday.spring.annotation.RequestMapping;
import com.daycodeday.spring.annotation.RequestParam;
import com.daycodeday.spring.webmvc.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 公布接口url
 * @author Tom
 *
 */
@Controller
@RequestMapping("/")
public class PageAction {

	@Autowrited
	IQueryService queryService;
	
	@RequestMapping("/first.html")
	public ModelAndView query(@RequestParam("teacher") String teacher){
		String result = queryService.query(teacher);
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("teacher", teacher);
		model.put("data", result);
		model.put("token", "123456");
		return new ModelAndView("first.html",model);
	}
	
}
