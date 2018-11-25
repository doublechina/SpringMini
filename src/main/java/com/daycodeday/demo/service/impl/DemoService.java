package com.daycodeday.demo.service.impl;


import com.daycodeday.demo.service.IDemoService;
import com.daycodeday.spring.annotation.Service;
@Service
public class DemoService implements IDemoService {

	public String get(String name) {
		return "My name is " + name;
	}

}
