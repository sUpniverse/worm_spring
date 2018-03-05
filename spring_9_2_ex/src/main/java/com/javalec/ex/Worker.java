package com.javalec.ex;

public class Worker {
	private String name;
	private String age;
	private String job;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	
	public void getWorkerInfo() {
		System.out.println("이름 :" + getName());
		System.out.println("나이 :" + getAge());
		System.out.println("직업 :" + getJob());
	}

	
}
