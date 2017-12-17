package com.mediabank.qna;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.mediabank.member.MemberDAO;
import com.mediabank.util.DBConnector;
import com.mediabank.util.MakeRow;

@Repository
public class QnaDAO {
	
	public QnaDTO searchQna_seq(int qna_seq) throws Exception{
		Connection con = DBConnector.getConnect();
		String sql = "select * from qna where qna_seq=?";
		PreparedStatement st = con.prepareStatement(sql);
		
		st.setInt(1, qna_seq);
		
		ResultSet rs = st.executeQuery();
		QnaDTO qnaDTO = null;
		while(rs.next()) {
			qnaDTO = new QnaDTO();
			qnaDTO.setContents(rs.getString("contents"));
			qnaDTO.setQna_seq(rs.getInt("qna_seq"));
			qnaDTO.setReg_date(rs.getDate("reg_date"));
			qnaDTO.setReply(rs.getString("reply"));
			qnaDTO.setReply_check(rs.getString("reply_check"));
			qnaDTO.setTitle(rs.getString("title"));
		}
		
		DBConnector.disConnect(rs, st, con);
		
		return qnaDTO;
	}
	public int replyUpdate(QnaDTO qnaDTO) throws Exception{
		Connection con = DBConnector.getConnect();
		String sql = "update qna set reply_check='답변 완료', reply=? where qna_seq=?";
		PreparedStatement st = con.prepareStatement(sql);
		
		st.setString(1, qnaDTO.getReply());
		st.setInt(2, qnaDTO.getQna_seq());
		
		int result = st.executeUpdate();
		
		return result;
	}
	public int update(QnaDTO qnaDTO) throws Exception{
		Connection con = DBConnector.getConnect();
		String sql = "update qna set title=?, contents=? where qna_seq=?";
		PreparedStatement st = con.prepareStatement(sql);
		
		st.setString(1, qnaDTO.getTitle());
		st.setString(2, qnaDTO.getContents());
		st.setInt(3, qnaDTO.getQna_seq());
		
		int result = st.executeUpdate();
		
		DBConnector.disConnect(st, con);
		
		 return result;
	}
	public int delete(int qna_seq) throws Exception{
		Connection con = DBConnector.getConnect();
		String sql = "delete qna where qna_seq=?";
		PreparedStatement st = con.prepareStatement(sql);
		
		st.setInt(1, qna_seq);
		
		int result = st.executeUpdate();
		
		DBConnector.disConnect(st, con);
		
		return result;
		
	}
	public int insert(QnaDTO qnaDTO,int user_num) throws Exception	{
		Connection con = DBConnector.getConnect();
		
		String sql = "insert into qna values(qna_seq.nextval,?,?,?,sysdate,'답변 미완료',NULL)";
		PreparedStatement st = con.prepareStatement(sql);
		st.setInt(1, user_num);
		st.setString(2, qnaDTO.getTitle());
		st.setString(3, qnaDTO.getContents());
		
		int result = st.executeUpdate();
		
		DBConnector.disConnect(st, con);
		
		return result;
	}
	
	public QnaDTO selectOne(int qna_seq) throws Exception {
		Connection con = DBConnector.getConnect();
		
		String sql = "select * from qna where qna_seq=?";
		PreparedStatement st = con.prepareStatement(sql);
		st.setInt(1, qna_seq);
		ResultSet rs = st.executeQuery();
		QnaDTO qnaDTO = null;
		MemberDAO memberDAO = new MemberDAO();
		if(rs.next())	{
			int user_num = rs.getInt("user_num");
			String kind = memberDAO.searchKind(user_num);
			qnaDTO = new QnaDTO();
			qnaDTO.setQna_seq(rs.getInt("qna_seq"));
			qnaDTO.setTitle(rs.getString("title"));
			qnaDTO.setWriter(memberDAO.searchNickName(user_num, kind));
			qnaDTO.setContents(rs.getString("contents"));
			qnaDTO.setReg_date(rs.getDate("reg_date"));
			qnaDTO.setReply_check(rs.getString("reply_check"));
			qnaDTO.setReply(rs.getString("reply"));
		}
		
		DBConnector.disConnect(rs, st, con);
		
		return qnaDTO;
	}
	public List<QnaDTO> adminSelectList(MakeRow makeRow, String kind, String search) throws Exception	{
		Connection con = DBConnector.getConnect();
		String sql = "select * from "
			+	"(select rownum R, Q.* from "
			+   "(select * from qna where reply_check='답변 미완료' and "+kind+" like ? order by qna_seq desc) Q) "
			+   "where R between ? and ?";
		PreparedStatement st = con.prepareStatement(sql);
		
		st.setString(1, "%"+search+"%");
		st.setInt(2, makeRow.getStartRow());
		st.setInt(3, makeRow.getLastRow());
		
		ResultSet rs = st.executeQuery();
		List<QnaDTO> ar = new ArrayList<QnaDTO>();
		MemberDAO memberDAO = new MemberDAO();
		while(rs.next())	{
			int user_num = rs.getInt("user_num");
			String member_kind = memberDAO.searchKind(user_num);
			QnaDTO qnaDTO = new QnaDTO();
			qnaDTO.setQna_seq(rs.getInt("qna_seq"));
			qnaDTO.setTitle(rs.getString("title"));
			qnaDTO.setWriter(memberDAO.searchNickName(user_num, member_kind));
			qnaDTO.setContents(rs.getString("contents"));
			qnaDTO.setReg_date(rs.getDate("reg_date"));
			qnaDTO.setReply_check(rs.getString("reply_check"));
			qnaDTO.setReply(rs.getString("reply"));
			ar.add(qnaDTO);
		}
		DBConnector.disConnect(rs, st, con);
		return ar;
		
	}
	
	public List<QnaDTO> selectList(MakeRow makeRow, String kind, String search) throws Exception	{
		Connection con = DBConnector.getConnect();
		String sql = "select * from "
			+	"(select rownum R, Q.* from "
			+   "(select * from qna where "+kind+" like ? order by qna_seq desc) Q) "
			+   "where R between ? and ?";
		PreparedStatement st = con.prepareStatement(sql);
		
		st.setString(1, "%"+search+"%");
		st.setInt(2, makeRow.getStartRow());
		st.setInt(3, makeRow.getLastRow());
		
		ResultSet rs = st.executeQuery();
		List<QnaDTO> ar = new ArrayList<QnaDTO>();
		
		MemberDAO memberDAO = new MemberDAO();
		while(rs.next())	{
			int user_num = rs.getInt("user_num");
			String member_kind = memberDAO.searchKind(user_num);
			
			QnaDTO qnaDTO = new QnaDTO();
			qnaDTO.setQna_seq(rs.getInt("qna_seq"));
			qnaDTO.setTitle(rs.getString("title"));
			qnaDTO.setWriter(memberDAO.searchNickName(user_num, member_kind));
			qnaDTO.setContents(rs.getString("contents"));
			qnaDTO.setReg_date(rs.getDate("reg_date"));
			qnaDTO.setReply_check(rs.getString("reply_check"));
			qnaDTO.setReply(rs.getString("reply"));
			ar.add(qnaDTO);
		}
		DBConnector.disConnect(rs, st, con);
		return ar;
		
	}
	
	public int getTotalCount(String kind, String search) throws Exception{
		Connection con = DBConnector.getConnect();
		String sql = "select nvl(count(qna_seq), 0) from qna where "+kind+" like ?";
		PreparedStatement st = con.prepareStatement(sql);
		
		st.setString(1, "%"+search+"%");
		ResultSet rs = st.executeQuery();
		rs.next();
		int result = rs.getInt(1);
		
		DBConnector.disConnect(rs, st, con);
		
		return result;
	}
}
