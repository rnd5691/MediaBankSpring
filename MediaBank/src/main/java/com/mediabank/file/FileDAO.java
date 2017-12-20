package com.mediabank.file;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mediabank.util.DBConnector;
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
			
	//file_table or work_table의 작품명, 닉네임, 파일이름, 파일종류를 가져와
	/*public List<FileDTO> fnWInfo() throws Exception{
				Connection con = DBConnector.getConnect();
				String sql = "select DISTINCT w.*, f.file_name, f.file_kind, f.file_num from work_info w, file_table f where w.work_seq=f.work_seq ORDER BY f.FILE_NUM desc";
				PreparedStatement st = con.prepareStatement(sql);
				ResultSet rs = st.executeQuery();
				List<FileDTO> ar = new ArrayList<FileDTO>();
				MemberDAO memberDAO = new MemberDAO();
				FileDTO fileDTO = null;
				while(rs.next()){
					fileDTO = new FileDTO();
					fileDTO.setFile_name(rs.getString("file_name"));
					fileDTO.setFile_kind(rs.getString("file_kind"));
					fileDTO.setWork(rs.getString("work"));
					fileDTO.setNickname(memberDAO.searchKind(rs.getInt("user_num")));
					fileDTO.setFile_num(rs.getInt("file_num"));
					ar.add(fileDTO);
				}
				DBConnector.disConnect(rs, st, con);
				return ar;
	}*/
			
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
				Connection con = DBConnector.getConnect();
				String sql = "select * from file_table where work_seq=?";
				PreparedStatement st = con.prepareStatement(sql);
				
				st.setInt(1, work_seq);
				
				ResultSet rs = st.executeQuery();
				FileDTO fileDTO = null;
				if(rs.next()){
					fileDTO = new FileDTO();
					fileDTO.setFile_num(rs.getInt("file_num"));
					fileDTO.setFile_route(rs.getString("file_route"));
					fileDTO.setFile_name(rs.getString("file_name"));
					fileDTO.setWidth(rs.getString("width"));
					fileDTO.setHeight(rs.getString("height"));
					fileDTO.setFile_kind(rs.getString("file_kind"));
					fileDTO.setWork_seq(work_seq);
				}
				DBConnector.disConnect(rs, st, con);
				return fileDTO;
			}
			
			//fileUpload
				public int fileUpload(FileDTO fileDTO,Connection con) throws Exception {
					String sql = "INSERT INTO file_table VALUES(file_num.nextval,?,?,?,?,?,?,?)";
					PreparedStatement st = con.prepareStatement(sql);
					st.setInt(1, fileDTO.getWork_seq());
					st.setString(2, fileDTO.getFile_route());
					st.setString(3, fileDTO.getFile_name());
					st.setString(4, fileDTO.getWidth());
					st.setString(5, fileDTO.getHeight());
					st.setString(6, fileDTO.getFile_kind());
					st.setString(7, fileDTO.getOriginalName());
					int result = st.executeUpdate();
					st.close();
					return result;
				}
}
