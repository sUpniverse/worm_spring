package com.javalec.ex;

public class Student {
	private String name;
	private String age;
	private String gradeNum;
	private String classNum;
	
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
	public String getGradeNum() {
		return gradeNum;
	}
	public void setGradeNum(String gradeNum) {
		this.gradeNum = gradeNum;
	}
	public String getClassNum() {
		return classNum;
	}
	public void setClassNum(String classNum) {
		this.classNum = classNum;
	}
	
	public void getStudentInfo() {
		System.out.println("이름 :" + getName());
		System.out.println("나이 :" + getAge());
		System.out.println("학년 :" + getGradeNum());
		System.out.println("반 :" + getClassNum());
	}

	
}
