package com.javalec.ex;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class MainClass {
	public static void main(String[] args) {
		AbstractApplicationContext ctx = new GenericXmlApplicationContext("classpath:applicationCTX.xml");
		Student student1 = ctx.getBean("student",Student.class);
		System.out.println(student1.getName());
		System.out.println(student1.getAge());
		
		Student student2 = ctx.getBean("student",Student.class);
		student2.setName("홍길자");
		student2.setAge(20);
		System.out.println(student1.getName());
		System.out.println(student1.getAge());
		
		if(student1.equals(student2)) {
			System.out.println("같습니다");
		} else {
			System.out.println("다릅니다");
		}
			
	}
}
