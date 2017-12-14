package com.mediabank.file;

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
public class FileDAO {
	//전체 작품 번호 가져오기
		public List<Integer> work_seq(int user_num, String file_kind) throws Exception{
			Connection con = DBConnector.getConnect();
			String sql = "select w.work_seq from work_info w, file_table f where f.work_seq=w.work_seq and w.user_num=? and w.upload_check='승인' and f.file_kind=?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, user_num);
			st.setString(2, file_kind);
			ResultSet rs = st.executeQuery();
			List<Integer> ar = new ArrayList<Integer>();
			while(rs.next()){
				int work_seq = rs.getInt("work_seq");
				ar.add(work_seq);
			}
				
			DBConnector.disConnect(rs, st, con);
			
			return ar;
				
		}
		//구매목록 뷰페이지를 위한 work_seq 받아오기
				public List<FileDTO> selectWorkSeq (int file_num) throws Exception {
					Connection con = DBConnector.getConnect();
					String sql = "SELECT work_seq FROM file_table WHERE file_num=?";
					PreparedStatement st = con.prepareStatement(sql);
					st.setInt(1, file_num);
					ResultSet rs = st.executeQuery();
					FileDTO fileDTO = null;
					List<FileDTO> ar = new ArrayList<FileDTO>();
					while(rs.next()) {
						fileDTO = new FileDTO();
						fileDTO.setWork_seq(rs.getInt("work_seq"));
						ar.add(fileDTO);
					}
					DBConnector.disConnect(rs, st, con);
					return ar;
				}
			
			//file_table or work_table의 작품명, 닉네임, 파일이름, 파일종류를 가져와
			public List<FileDTO> fnWInfo() throws Exception{
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
			}
			
			//현재 판매 중인 내 작품 에 쓰이는 토탈메소드
			public int getTotalCount(int user_num, String file_kind) throws Exception	{
				Connection con = DBConnector.getConnect();
				String sql = "select nvl(count(f.work_seq), 0) from file_table f, work_info w where f.work_seq=w.work_seq and w.user_num=? and w.upload_check='승인' and f.file_kind=?";
				PreparedStatement st = con.prepareStatement(sql);
						
				st.setInt(1, user_num);
				st.setString(2, file_kind);
				
				ResultSet rs = st.executeQuery();
				rs.next();
						
				int result = rs.getInt(1);
				DBConnector.disConnect(rs, st, con);
						
				return result;
			}
				
			//현재 판매중인 내 작품 조회 메소드
			public List<FileDTO> selectNow(int user_num, String file_kind, MakeRow makeRow) throws Exception	{
				Connection con = DBConnector.getConnect();
				String sql = "select * from (select rownum R, I.* from (select f.file_num, f.work_seq, f.file_name, f.file_kind, w.user_num, w.sell, w.upload_check from work_info w, file_table f where f.work_seq=w.work_seq and user_num=? and w.upload_check='승인' and file_kind=?) I) where R between ? and ?";
				PreparedStatement st = con.prepareStatement(sql);
				st.setInt(1, user_num);
				st.setString(2, file_kind);
				st.setInt(3, makeRow.getStartRow());
				st.setInt(4, makeRow.getLastRow());
				ResultSet rs = st.executeQuery();
				
				List<FileDTO> ar = new ArrayList<FileDTO>();
				FileDTO fileDTO = null;
				while(rs.next())	{
					fileDTO = new FileDTO();
					fileDTO.setFile_num(rs.getInt("file_num"));
					fileDTO.setWork_seq(rs.getInt("work_seq"));
					fileDTO.setFile_name(rs.getString("file_name"));
					fileDTO.setUser_num(rs.getInt("user_num"));
					fileDTO.setFile_kind(rs.getString("file_kind"));
					fileDTO.setSell(rs.getString("sell"));
					ar.add(fileDTO);
				}
				DBConnector.disConnect(rs, st, con);
				return ar;
			}
				
			//salesReuqestViewDelete
			public void salesRequestViewDelete(int work_seq, Connection con) throws Exception {
				String sql = "DELETE file_table WHERE work_seq=?";
				PreparedStatement st = con.prepareStatement(sql);
				st.setInt(1, work_seq);
				st.executeUpdate();
				st.close();
			}
			
			//salesRequestViewUpdate
			public int salesRequestViewUpdate(FileDTO fileDTO, Connection con) throws Exception {
				String sql = "UPDATE file_table SET file_route=?, width=?, height=?, file_name=? WHERE work_seq=?";
				PreparedStatement st = con.prepareStatement(sql);
				st.setString(1, fileDTO.getFile_route());
				st.setString(2, fileDTO.getWidth());
				st.setString(3, fileDTO.getHeight());
				st.setString(4, fileDTO.getFile_name());
				st.setInt(5, fileDTO.getWork_seq());
				int result = st.executeUpdate();
				st.close();
				return result;
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
					String sql = "INSERT INTO file_table VALUES(file_num.nextval,?,?,?,?,?,?)";
					PreparedStatement st = con.prepareStatement(sql);
					st.setInt(1, fileDTO.getWork_seq());
					st.setString(2, fileDTO.getFile_route());
					st.setString(3, fileDTO.getFile_name());
					st.setString(4, fileDTO.getWidth());
					st.setString(5, fileDTO.getHeight());
					st.setString(6, fileDTO.getFile_kind());
					int result = st.executeUpdate();
					st.close();
					return result;
				}
}
