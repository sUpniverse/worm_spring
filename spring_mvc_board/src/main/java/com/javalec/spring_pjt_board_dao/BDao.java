package com.javalec.spring_pjt_board_dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.javalec.spring_pjt_board.util.Constant;
import com.javalec.spring_pjt_board_dto.BDto;

public class BDao {
	
	DataSource dataSource;	
	JdbcTemplate template = null;
	
	
	public BDao() {
		// TODO Auto-generated constructor stub
		try {
			Context context = new InitialContext();
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/mysql");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		template = Constant.template;
		
	}
	
	public ArrayList<BDto> list() {
		
		
		String query = "select bId,bName,bTitle,bContent,bDate,bHit,bGroup,bStep,bIndent from mvc_board order by bGroup desc, bStep asc";		
		BeanPropertyRowMapper<BDto> mapper =  new BeanPropertyRowMapper<BDto>(BDto.class);
		ArrayList<BDto> dtos = (ArrayList<BDto>)template.query(query,mapper);
		return dtos;
		
		
//		ArrayList<BDto> dtos = new ArrayList<BDto>();
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String query = "select bId,bName,bTitle,bContent,bDate,bHit,bGroup,bStep,bIndent from mvc_board order by bGroup desc, bStep asc";
//		
//		try {
//			con = dataSource.getConnection();
//			pstmt  = con.prepareStatement(query);
//			rs = pstmt.executeQuery();
//			
//			while(rs.next()) {
//				int bId = rs.getInt("bId");
//				String bName = rs.getString("bName");
//				String bTitle = rs.getString("bTitle");
//				String bContent = rs.getString("bContent");
//				Timestamp bDate = rs.getTimestamp("bDate");
//				int bHit = rs.getInt("bHit");
//				int bGroup = rs.getInt("bGroup");
//				int bStep = rs.getInt("bStep");
//				int bIndent = rs.getInt("bIndent");
//				
//				BDto dto = new BDto(bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent);
//				dtos.add(dto);
//			}
//			
//		}catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}finally {
//			try {
//				if(rs != null) rs.close();
//				if(pstmt != null) pstmt.close();
//				if(con != null) con.close();
//			} catch (Exception e2) {
//				// TODO: handle exception
//			}			
//		}
//		
//		return dtos;
	}
	
	public void write(final String bName,final String bTitle,final String bContent) {
		
		
		template.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				String query = "insert into mvc_board (bName,bTitle,bContent,bHit,bGroup,bStep,bIndent) values (?,?,?,0,(select max(bId)+1 from mvc_board a),0,0)";
				PreparedStatement pstmt = con.prepareStatement(query);
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, bName);
				pstmt.setString(2, bTitle);
				pstmt.setString(3, bContent);
				return pstmt;
			}
		});
		
//		Connection con = null;
//		PreparedStatement pstmt = null;
//				
//		try {
//			con = dataSource.getConnection();
//			String query = "insert into mvc_board (bName,bTitle,bContent,bHit,bGroup,bStep,bIndent) values (?,?,?,0,(select max(bId)+1 from mvc_board a),0,0)";
//			pstmt = con.prepareStatement(query);
//			pstmt.setString(1, bName);
//			pstmt.setString(2, bTitle);
//			pstmt.setString(3, bContent);
//			int rn = pstmt.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if(pstmt != null) pstmt.close();
//				if(con != null) con.close();
//			} catch (Exception e) {
//				// TODO: handle exception
//			} 			
//		}
	}
	
	public BDto Contentview(String strId) {
		
		upHit(strId);
		
		String query = "select * from mvc_board where bId =" + strId;
		return template.queryForObject(query, new BeanPropertyRowMapper<BDto>(BDto.class));
		
//		BDto dto = null;
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		
//		try {
//			con = dataSource.getConnection();
//			String query = "select * from mvc_board where bId = ?";
//			pstmt = con.prepareStatement(query);
//			pstmt.setInt(1, Integer.parseInt(strId));
//			rs = pstmt.executeQuery();
//			
//			if(rs.next()) {
//				int bId = rs.getInt("bId");
//				String bName = rs.getString("bName");
//				String bTitle = rs.getString("bTitle");
//				String bContent = rs.getString("bContent");
//				Timestamp bDate = rs.getTimestamp("bDate");
//				int bHit = rs.getInt("bHit");
//				int bGroup = rs.getInt("bGroup");
//				int bStep = rs.getInt("bStep");
//				int bIndent = rs.getInt("bIndent");
//				
//				dto = new BDto(bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent);
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if(rs != null) rs.close();
//				if(pstmt != null) pstmt.close();
//				if(con != null) con.close();
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		
//		
//		return dto;
	}
	
	public void upHit(final String bid) {
		String query = "update mvc_board set bHit = bHit + 1 where bId =?";
		template.update(query,new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, Integer.parseInt(bid));
			}
		});
		
//		Connection con  = null;
//		PreparedStatement pstmt = null;
//		
//		try {
//			con = dataSource.getConnection();
//			String query = "update mvc_board set bHit = bHit + 1 where bId =?";
//			pstmt = con.prepareStatement(query);
//			pstmt.setInt(1, Integer.parseInt(bid));
//			
//			int rn = pstmt.executeUpdate();
//					
//		} catch (Exception e) {
//			// TODO: handle exception
//		} finally {
//			try {
//				if(pstmt != null) pstmt.close();
//				if(con != null) con.close();
//				
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}		
	}
	
	public void delete(final String bId) {
		
		String query = "delete from mvc_board where bId =?";
		template.update(query,new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setInt(1, Integer.parseInt(bId));
			}
		});
		
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {
//			con = dataSource.getConnection();
//			String query = "delete from mvc_board where bId =?";
//			pstmt = con.prepareStatement(query);
//			pstmt.setInt(1, Integer.parseInt(bId));	
//			int rn = pstmt.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if(pstmt != null) pstmt.close();
//				if(con != null) con.close();
//			} catch (Exception e2) {
//				// TODO: handle exception
//			}
//		}
//		
	}
	
	public void modify(final String bId, final String bName, final String bTitle, final String bContent) {
		
		String query = "update mvc_board set bName =? , bTitle =?, bContent = ? where bId = ? ";
		template.update(query,new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, bName);
				ps.setString(2, bTitle);
				ps.setString(3, bContent);
				ps.setInt(4, Integer.parseInt(bId));
			}
		});
		
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		try {
//			con = dataSource.getConnection();
//			String query = "update mvc_board set bName =? , bTitle =?, bContent = ? where bId = ? ";			
//			pstmt = con.prepareStatement(query);
//			pstmt.setString(1, bName);
//			pstmt.setString(2, bTitle);
//			pstmt.setString(3, bContent);
//			pstmt.setInt(4, Integer.parseInt(bId));
//			int rs = pstmt.executeUpdate();
//		} catch (Exception e) {			
//			e.printStackTrace();
//		} finally {
//			try {
//				if(pstmt != null) pstmt.close();
//				if(con != null) pstmt.close();
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
		
	}
	
	public BDto reply_view(String strId) {
		String query = "select * from mvc_board where bId = " + strId;
		return template.queryForObject(query, new BeanPropertyRowMapper<BDto>(BDto.class));
		
//		
//		BDto dto = null;
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//				
//		try {
//			con = dataSource.getConnection();
//			String query = "select * from mvc_board where bId = ?";
//			pstmt = con.prepareStatement(query);
//			pstmt.setInt(1, Integer.parseInt(strId));
//			rs = pstmt.executeQuery();
//			
//			if(rs.next()) {
//				int bId = rs.getInt("bId");
//				String bName = rs.getString("bName");
//				String bTitle = rs.getString("bTitle");
//				String bContent = rs.getString("bContent");
//				Timestamp bDate = rs.getTimestamp("bDate");
//				int bHit = rs.getInt("bHit");
//				int bGroup = rs.getInt("bGroup");
//				int bStep = rs.getInt("bStep");
//				int bIndent = rs.getInt("bIndent");
//				
//				dto = new BDto(bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent);
//				
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if(rs != null) rs.close();
//				if(pstmt != null) pstmt.close();
//				if(con != null) con.close();
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		
//		return dto;
	}
	
	public void reply(final String bId,final String bName,final String bTitle,final String bContent,final String bGroup,final String bStep,final String bIndent) {
		// TODO Auto-generated method stub
		
		replyShape(bGroup,bStep);
		String query = "insert into mvc_board (bName,bTitle,bContent,bGroup,bStep,bIndent) values (?,?,?,?,?,?)";
		template.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setString(1, bName);
				ps.setString(2, bTitle);
				ps.setString(3, bContent);
				ps.setInt(4,Integer.parseInt(bGroup));
				ps.setInt(5,Integer.parseInt(bStep) + 1);
				ps.setInt(6,Integer.parseInt(bIndent) + 1);
			}
		});		
				
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {
//			con = dataSource.getConnection();
//			String query = "insert into mvc_board (bName,bTitle,bContent,bGroup,bStep,bIndent) values (?,?,?,?,?,?)";
//			pstmt = con.prepareStatement(query);
//			pstmt.setString(1, bName);
//			pstmt.setString(2, bTitle);
//			pstmt.setString(3, bContent);
//			pstmt.setInt(4,Integer.parseInt(bGroup));
//			pstmt.setInt(5,Integer.parseInt(bStep) + 1);
//			pstmt.setInt(6,Integer.parseInt(bIndent) + 1);
//			
//			int rn = pstmt.executeUpdate();
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if(pstmt != null) pstmt.close();
//				if(con != null) con.close();
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}
//		
	}
	
	private void replyShape(final String strGroup,final String strStep) {
		
		String query = "update mvc_board set bStep = bStep + 1 where bGroup= ? and bStep > ?";
		template.update(query, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setInt(1,Integer.parseInt(strGroup));
				ps.setInt(2,Integer.parseInt(strStep));	
			}
		});
		
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		
//		try {
//			con = dataSource.getConnection();
//			String query = "update mvc_board set bStep = bStep + 1 where bGroup= ? and bStep > ?";
//			pstmt = con.prepareStatement(query);
//			pstmt.setInt(1,Integer.parseInt(strGroup));
//			pstmt.setInt(2,Integer.parseInt(strStep));			
//			int rn = pstmt.executeUpdate();
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if(pstmt != null) pstmt.close();
//				if(con != null) con.close();
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}

	}
}
