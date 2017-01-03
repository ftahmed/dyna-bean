package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pojo.PojoOne;
import pojo.PojoTwo;

@RestController
public class DynaBeanController {
	@Autowired
	PojoOne pojoOne;

	@Autowired
	PojoTwo pojoTwo;

	@RequestMapping("/one")
	public PojoOne getOne() {
		return pojoOne;
	}

	@RequestMapping("/two")
	public PojoTwo getTwo() {
		return pojoTwo;
	}
}
