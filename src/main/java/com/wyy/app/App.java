package com.wyy.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {

	public static void main(String[] args) {
		
		ApplicationContext c = new ClassPathXmlApplicationContext("applicationContext.xml");
		c.getBean("user");
		System.out.println(c.getBean("user"));
	}

}
