package com.sdsd.mvc.indiboard.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sdsd.mvc.common.util.PageInfo;
import com.sdsd.mvc.indiboard.model.vo.IndiBoard;
import com.sdsd.mvc.indiboard.model.vo.Reply;

import static com.sdsd.mvc.common.jdbc.JDBCTemplate.*;

import static com.sdsd.mvc.common.jdbc.JDBCTemplate.*;

public class BoardDao {

	public int getBoardCount(Connection connection) {
		int count = 0;
		PreparedStatement pstm = null;
		ResultSet rs = null;
//		String query = "SELECT COUNT(*) "
//						+ "FROM MY_ACT_BOARD M JOIN BOARD B ON (M.MA_WRITER_NUMBER = B.BOR_WRITER_NO) "
//						+ "WHERE B.BOR_STATUS = 'Y'";
		String query = "SELECT COUNT(*) "
					+ "FROM INDIBOARD ";
		
		try {
			pstm = connection.prepareStatement(query);
			rs = pstm.executeQuery();
			
			if(rs.next()) {
				count = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstm);
		}		
		return count;
	}

	public List<IndiBoard> findAll(Connection connection, PageInfo pageInfo) {
		List<IndiBoard> indiboardlist = new ArrayList<>();
		PreparedStatement pstm = null;;
		ResultSet rs = null;
		String query = "SELECT *  "
				+ "FROM ( "
				+ "    SELECT ROWNUM AS RNUM, "
				+ "           INDIBOR_NUMBER, "
				+ "           INDIBOR_TITLE, "
				+ "           MEM_EMAIL, "
				+ "           INDIBOR_WRITER_NAME, "
				+ "           INDI_CREATE_DATE, "
				+ "           INDI_UPDATE_DATE, "
				+ "           INDI_BOR_FILE, "
				+ "           INDI_READCOUNT, "
				+ "           INDI_BOR_STATUS "
				+ "    FROM ( "
				+ "            SELECT IB.INDIBOR_NUMBER, "
				+ "                   IB.INDIBOR_TITLE, "
				+ "                   M.MEM_EMAIL, "
				+ "                   IB.INDIBOR_WRITER_NAME, "
				+ "                   IB.INDI_CREATE_DATE, "
				+ "                   IB.INDI_UPDATE_DATE, "
				+ "                   IB.INDI_BOR_FILE, "
				+ "                   IB.INDI_READCOUNT, "
				+ "                   IB.INDI_BOR_STATUS "
				+ "            FROM INDIBOARD IB "
				+ "            JOIN MEMBER M ON (M.MEM_NUMBER = IB.INDIBOR_WRITER_NO) "
				+ "    ) "
				+ ") WHERE (RNUM BETWEEN ? AND ?) AND INDI_BOR_STATUS = 'Y'";
		
		
		try {
			pstm = connection.prepareStatement(query);
			pstm.setInt(1, pageInfo.getStartList());
			pstm.setInt(2, pageInfo.getEndList());
			
			rs = pstm.executeQuery();
			
			while(rs.next()) {
				IndiBoard indiBoard = new IndiBoard();
				
				indiBoard.setRowNum(rs.getInt("RNUM"));
				indiBoard.setMaBorNo(rs.getInt("INDIBOR_NUMBER"));
				indiBoard.setBorTitle(rs.getString("INDIBOR_TITLE"));
				indiBoard.setEmail(rs.getString("MEM_EMAIL"));
				indiBoard.setWriterName(rs.getString("INDIBOR_WRITER_NAME"));
				indiBoard.setCreateDate(rs.getString("INDI_CREATE_DATE"));
				indiBoard.setBorFile(rs.getString("INDI_BOR_FILE"));
				indiBoard.setReadCount(rs.getInt("INDI_READCOUNT"));
				indiBoard.setBorStatus(rs.getString("INDI_BOR_STATUS"));

				indiboardlist.add(indiBoard);
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstm);
		}
		
		return indiboardlist;
	}

	public int insertIndiBoard(Connection connection, IndiBoard indiBoard) {
		int result = 0;
		//int result2 = 0;
		PreparedStatement pstmt = null;
//		PreparedStatement pstmt2 = null;
		String query_board = "INSERT INTO INDIBOARD VALUES(SEQ_INDIBOR_NUMBER.NEXTVAL, ?, ?, '제목넣어야함', ?, DEFAULT, DEFAULT, DEFAULT, ?, DEFAULT)";
		//String query_indiBoard = "INSERT INTO MY_ACT_BOARD VALUES(SEQ_MY_BOR_NUM, 1, ?)";
		
		try {
			pstmt = connection.prepareStatement(query_board);
			//pstmt2 = connection.prepareStatement(query_indiBoard);
			
			pstmt.setInt(1, indiBoard.getWriterNo());
			pstmt.setString(2, indiBoard.getWriterName());
			pstmt.setString(3, indiBoard.getBorContent());
			pstmt.setString(4, indiBoard.getBorFile());

			result = pstmt.executeUpdate();
			//result2 = pstmt2.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
//			close(pstmt2);
			close(pstmt);
		}
		
		return result;
	}

//	public int insertIndiBoard2(Connection connection, IndiBoard indiBoard) {
//		int result = 0;
//		PreparedStatement pstm = null;
//		String query = "INSERT INTO MY_ACT_BOARD VALUES(SEQ_MA_BOARD_NUMBER.NEXTVAL, 1, ?, '제목넣는공간', ?, DEFAULT)";
//		// 제목은 jsp 파일 수정 후 넣어야합니다 
//		try {
//			pstm = connection.prepareStatement(query);
//			pstm.setInt(1, indiBoard.getWriterNo());
//			pstm.setString(2, indiBoard.getBorContent());
//
//			result = pstm.executeUpdate();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} finally {
//			close(pstm);
//		}
//		return result;
//		
//	}

	public int updateStatus(Connection connection, int maBorNo, String status) {
		int result = 0;
		PreparedStatement pstm = null;
		String qurey = "UPDATE INDIBOARD SET INDI_BOR_STATUS=? WHERE INDIBOR_NUMBER=?";
		try {
			pstm = connection.prepareStatement(qurey);
			
			pstm.setString(1, status);
			pstm.setInt(2, maBorNo);
			
			result = pstm.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstm);
		}
	
		return result;
	}


	public IndiBoard findBoardByNo(Connection connection, int maBorNo) {
		IndiBoard indiBoard = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "SELECT * "
					+ "FROM INDIBOARD IB "
					+ "JOIN MEMBER M ON(IB.INDIBOR_WRITER_NO = M.MEM_NUMBER) "
					+ "WHERE IB.INDI_BOR_STATUS = 'Y' AND INDIBOR_NUMBER = ?";
		
		try {
			pstmt = connection.prepareStatement(query);
			
			pstmt.setInt(1, maBorNo);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				indiBoard = new IndiBoard();
				
				indiBoard.setMaBorNo(rs.getInt("INDIBOR_NUMBER"));
				indiBoard.setBorTitle(rs.getString("INDIBOR_TITLE"));
				indiBoard.setBorContent(rs.getString("INDIBOR_CONTENT"));
				indiBoard.setWriterName(rs.getString("INDIBOR_WRITER_NAME"));
				indiBoard.setReadCount(rs.getInt("INDI_READCOUNT"));
				indiBoard.setBorFile(rs.getString("INDI_BOR_FILE"));
				indiBoard.setCreateDate(rs.getString("INDI_CREATE_DATE"));
				indiBoard.setUpdateDate(rs.getString("INDI_UPDATE_DATE"));				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		
		
		return indiBoard;
	}

	public int updateReadCount(Connection connection, IndiBoard indiBoard) {
		int result =0;
		PreparedStatement pstmt = null;
		String query = "UPDATE INDIBOARD SET INDI_READCOUNT=? WHERE INDIBOR_NUMBER=?";
		
		try {
			pstmt = connection.prepareStatement(query);
			
			indiBoard.setReadCount(indiBoard.getReadCount() + 1);
			
			pstmt.setInt(1, indiBoard.getReadCount());
			pstmt.setInt(2, indiBoard.getMaBorNo());
			
			result = pstmt.executeUpdate();
			System.out.println("몰라" + indiBoard.getReadCount());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public int insertReply(Connection connection, Reply reply) {
		int result = 0;
		PreparedStatement pstm = null;
		String query = "INSERT INTO REPLY VALUES(SEQ_IBREP_NUMBER.NEXTVAL, ?, ?, ?, DEFAULT, DEFAULT, DEFAULT)";
		try {
			pstm = connection.prepareStatement(query);
			
			pstm.setInt(1, reply.getRepboardNo());
			pstm.setInt(2, reply.getRepwriterNo());
			pstm.setString(3, reply.getRepcontent());
			result = pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(pstm);
		}
		return result;
	}

}
