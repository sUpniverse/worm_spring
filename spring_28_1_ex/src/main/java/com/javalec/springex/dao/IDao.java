package com.javalec.springex.dao;

import java.util.ArrayList;

import com.javalec.springex.dto.ContentDto;

public interface IDao {
	public ArrayList<ContentDto> listDao();
	public void writeDao(String mWriter, String mContent);
	public ContentDto viewDao(String strId);
	public void deleteDao(String bId);
	
}
