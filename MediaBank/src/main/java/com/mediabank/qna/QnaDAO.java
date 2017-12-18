package com.mediabank.qna;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mediabank.util.DBConnector;
import com.mediabank.util.RowNum;

@Repository
public class QnaDAO {
	@Autowired
	private SqlSession sqlSession;
	
	private static final String NAMESAPCE = "qnaMapper.";
	
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
		return sqlSession.delete(NAMESAPCE+"delet", qna_seq);
		/*Connection con = DBConnector.getConnect();
		String sql = "delete qna where qna_seq=?";
		PreparedStatement st = con.prepareStatement(sql);
		
		st.setInt(1, qna_seq);
		
		int result = st.executeUpdate();
		
		DBConnector.disConnect(st, con);
		
		return result;*/	
	}
	public int insert(QnaDTO qnaDTO) throws Exception	{
		return sqlSession.insert(NAMESAPCE+"insert", qnaDTO);
	}
	
	public QnaDTO selectOne(int qna_seq) throws Exception {
		return sqlSession.selectOne(NAMESAPCE+"selectOne", qna_seq);
	}
	public List<QnaDTO> adminSelectList(RowNum rowNum) throws Exception	{
		return sqlSession.selectList(NAMESAPCE+"adminSelectList", rowNum);	
	}
	
	public List<QnaDTO> selectList(RowNum rowNum) throws Exception	{
		return sqlSession.selectList(NAMESAPCE+"selectList", rowNum);
	}
	
	public int getTotalCount(RowNum rowNum) throws Exception{
		return sqlSession.selectOne(NAMESAPCE+"getTotalCount", rowNum);
	}
}
