package com.mediabank.qna;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mediabank.util.RowNum;

@Repository
public class QnaDAO {
	@Autowired
	private SqlSession sqlSession;
	
	private static final String NAMESAPCE = "qnaMapper.";
	
	public QnaDTO searchQna_seq(int qna_seq) throws Exception{
		return sqlSession.selectOne(NAMESAPCE+"searchQna_seq", qna_seq);
	}
	public int replyUpdate(QnaDTO qnaDTO) throws Exception{
		return sqlSession.update(NAMESAPCE+"replyUpdate", qnaDTO);
	}
	public int update(QnaDTO qnaDTO) throws Exception{
		return sqlSession.update(NAMESAPCE+"update", qnaDTO);
	}
	public int delete(int qna_seq) throws Exception{
		return sqlSession.delete(NAMESAPCE+"delet", qna_seq);	
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
