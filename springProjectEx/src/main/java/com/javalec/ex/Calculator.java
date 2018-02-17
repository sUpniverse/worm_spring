package com.javalec.ex;

public class Calculator {
	public void add(int firstNum, int secondNum) {
		System.out.println("add");
		System.out.println("firstNum + secondNum =" + (firstNum + secondNum));
	}
	
	public void substract(int firstNum, int secondNum) {
		System.out.println("substract");
		System.out.println("firstNum - secondNum =" + (firstNum - secondNum));
	}
	
	public void multi(int firstNum, int secondNum) {
		System.out.println("multi");
		System.out.println("firstNum * secondNum =" + (firstNum * secondNum));
	}
	
	public void div(int firstNum, int secondNum) {
		System.out.println("div");
		System.out.println("firstNum / secondNum =" + (firstNum / secondNum));
	}
}
