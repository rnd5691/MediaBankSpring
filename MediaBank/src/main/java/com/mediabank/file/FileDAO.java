package com.mediabank.file;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mediabank.util.RowNum;

@Repository
public class FileDAO {
	@Autowired
	private SqlSession sqlSession;
	
	private static final String NAMESPACE = "fileMapper.";
	//전체 작품 번호 가져오기
	public List<Integer> work_seq(int user_num, String file_kind, RowNum rowNum) throws Exception{
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user_num", user_num);
		map.put("file_kind", file_kind);
		map.put("rowNum", rowNum);
		
		return sqlSession.selectList(NAMESPACE+"work_seq", map);		
	}
	//구매목록 뷰페이지를 위한 work_seq 받아오기
	public List<FileDTO> selectWorkSeq (int file_num) throws Exception {
		return sqlSession.selectList(NAMESPACE+"selectWorkSeq", file_num);
	}
			
	//현재 판매 중인 내 작품 에 쓰이는 토탈메소드
	public int getTotalCount(int user_num, String file_kind) throws Exception	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user_num", user_num);
		map.put("file_kind", file_kind);
		
		return sqlSession.selectOne(NAMESPACE+"getTotalCount", map);
	}
	
	//현재 판매중인 내 작품 조회 메소드
	public List<FileDTO> selectNow(int user_num, String file_kind, RowNum rowNum) throws Exception	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("user_num", user_num);
		map.put("file_kind", file_kind);
		map.put("rowNum", rowNum);
		return sqlSession.selectList(NAMESPACE+"selectNow", map);
	}
				
	//salesReuqestViewDelete
	public void salesRequestViewDelete(int work_seq) throws Exception {
		sqlSession.delete(NAMESPACE+"salesRequestViewDelete", work_seq);
	}
	
	//salesRequestViewUpdate
	public int salesRequestViewUpdate(FileDTO fileDTO) throws Exception {
		return sqlSession.update(NAMESPACE+"salesRequestViewUpdate", fileDTO);
	}
			
	//부모의 work_seq로 file_table 모든 정보 가져오기
	public FileDTO selectOne(int work_seq) throws Exception{
		return sqlSession.selectOne(NAMESPACE+"selectOne", work_seq);
	}
			
	//fileUpload
	public int fileUpload(FileDTO fileDTO) throws Exception {
		return sqlSession.insert(NAMESPACE+"fileUpload", fileDTO);
	}
}
