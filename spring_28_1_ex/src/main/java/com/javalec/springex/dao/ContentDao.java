package com.javalec.springex.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.javalec.springex.dto.ContentDto;

public class ContentDao implements IDao {

	JdbcTemplate template;
	
	@Autowired
	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}
	
	public ContentDao() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public ArrayList<ContentDto> listDao() {
		
		String query = "select * from board order by mId desc";
		ArrayList<ContentDto> dtos = (ArrayList<ContentDto>) this.template.query(query, new BeanPropertyRowMapper<ContentDto>(ContentDto.class));
		
		return dtos;		
	}
	
	@Override
	public void writeDao(final String mWriter, final String mContent) {
		
		this.template.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String query = "Insert into board (mWriter,mContent) values (?, ?)";
				PreparedStatement pstmt = con.prepareStatement(query);
				pstmt.setString(1, mWriter);
				pstmt.setString(2, mContent);
				return pstmt;					
				
			}
		});
		
	}
	
	@Override
	public ContentDto viewDao(String strId) {
		String query = "select * from board where mId = " + strId;
		
		return this.template.queryForObject(query, new BeanPropertyRowMapper<ContentDto>(ContentDto.class));
	}
	
	@Override
	public void deleteDao(final String bId) {
		String query = "delete from board where mId = ?";
		this.template.update(query,new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement pstmt) throws SQLException {
				pstmt.setInt(1, Integer.parseInt(bId));				
			}
		});
	}
}