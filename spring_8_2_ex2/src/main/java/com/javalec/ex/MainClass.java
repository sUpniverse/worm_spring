package com.javalec.ex;


import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class MainClass {
	public static void main(String[] args) {
				
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class);
		AdminConnection connection = ctx.getBean("adminConfig",AdminConnection.class);
		
		System.out.println("adminID :" + connection.getAdminId());
		System.out.println("adminPW :" + connection.getAdminPw());
		System.out.println("adminID :" + connection.getSub_adminId());
		System.out.println("adminPW :" + connection.getSub_adminPw());
		
		ctx.close(); 
		
	}

}
