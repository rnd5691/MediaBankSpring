package com.mediabank.work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mediabank.file.FileDTO;
import com.mediabank.member.MemberDAO;
import com.mediabank.util.DBConnector;
import com.mediabank.util.RowNum;

@Repository
public class WorkDAO {
	@Autowired
	private SqlSession sqlSession;
	@Autowired
	private MemberDAO memberDAO;
	private static final String NAMESPACE = "workMapper.";
	//다운로드히트 업데이트 
	public void downloadHitUpdate(int work_seq) throws Exception {
		sqlSession.update(NAMESPACE+"downloadHitUpdate", work_seq);
	}
			
	//관리자 승인,거부 난 작품들 리스트 가져오기
	public boolean adminCheck(int work_seq) throws Exception{
		String upload_check = sqlSession.selectOne(NAMESPACE+"adminCheck", work_seq);
		boolean check = true;
		if(upload_check.equals("대기중")||upload_check.equals("거부")) {
			check = false;
		}
		return check;
	}
	//무명작가용 토탈카운트 이미지용
	public int artistGetTotalCountImgString (String select, String search) throws Exception	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("select", select);
		map.put("search", search);
		return sqlSession.selectOne(NAMESPACE+"artistGetTotalCountImgString", map);		
	}
	//무명작가용 토탈카운트 비디오용
	public int artistGetTotalCountVideo(String select, String search) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("select", select);
		map.put("search", search);
		
		return sqlSession.selectOne(NAMESPACE+"artistGetTotalCountVideo", map);
	}
			
	//태그에 해당하는 값만 가져와! 무명작가게시판용
	public List<FileDTO> seachWorkSEQ(String tag, String kind, RowNum rowNum) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("tag", tag);
		map.put("kind", kind);
		map.put("rowNum", rowNum);
		return sqlSession.selectList(NAMESPACE+"searchWorkSEQ", map);
	}
			//무명작가 검색창
			public List<FileDTO> artistSearch(String kind, String select, String search,RowNum rowNum) throws Exception {
				Connection con = DBConnector.getConnect();
				String sql = "SELECT * FROM (SELECT rownum R, Q.* FROM "
						+ "(SELECT w.*, f.file_name, f.file_kind FROM work_info w, file_table f "
						+ "WHERE w."+select+" LIKE '%"+search+"%' and w.WORK_SEQ = f.WORK_SEQ and upload_check='승인' and sell='Y' and f.file_kind='"+kind+"'"
								+ " ORDER BY f.WORK_SEQ desc) Q) WHERE R between ? and ?";
				PreparedStatement st = con.prepareStatement(sql);
				st.setInt(1, rowNum.getStartRow());
				st.setInt(2, rowNum.getLastRow());
				ResultSet rs = st.executeQuery();
				FileDTO fileDTO = null;
				List<FileDTO> image = new ArrayList<FileDTO>();
				/*HashMap<String, List<FileDTO>> map = new HashMap<>();*/
				while(rs.next()) {
					fileDTO = new FileDTO();
					fileDTO.setWork(rs.getString("work"));
					fileDTO.setNickname(memberDAO.searchNickName(rs.getInt("user_num"), "person"));
					fileDTO.setWork_seq(rs.getInt("work_seq"));
					fileDTO.setFile_name(rs.getString("file_name"));
					fileDTO.setFile_kind(rs.getString("file_kind"));
					
					image.add(fileDTO);
				}
				DBConnector.disConnect(rs, st, con);
				return image;
			}
			
			//판매여부 업데이트
				public int sellUpdate(int work_seq, String sell) throws Exception	{
					Connection con = DBConnector.getConnect();
					String sql = "update work_info set sell=? where work_seq=?";
					PreparedStatement st = con.prepareStatement(sql);
					
					st.setString(1, sell);
					st.setInt(2, work_seq);
					
					int result = st.executeUpdate();
					
					DBConnector.disConnect(st, con);
					
					return result;
				}
		//체크 된 작품들 판매여부 업데이트
		public int sellUpdate(Connection con, int work_seq, String sell) throws Exception	{
			String sql = "update work_info set sell=? where work_seq=?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, sell);
			st.setInt(2, work_seq);
			int result = st.executeUpdate();
			
			st.close();
				
			return result;
		}
		//회원탈퇴시 판매유무 변경
		public int dropOut(int user_num) throws Exception{
			return sqlSession.update(NAMESPACE+"dropOut", user_num);
		}
		
		//관리자 승인시 업데이트
		public int approvalUpdate(int work_seq) throws Exception{
			Connection con = DBConnector.getConnect();
			String sql = "update work_info set upload_check='승인', sell='Y' where work_seq=?";
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setInt(1, work_seq);
			
			int result = st.executeUpdate();
			
			DBConnector.disConnect(st, con);
			
			return result;
		}
		//관리자 거부시 업데이트
		public int replyUpdate(WorkDTO workDTO) throws Exception{
			Connection con = DBConnector.getConnect();
			String sql = "update work_info set reply=?, upload_check='거부' where work_seq=?";
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setString(1, workDTO.getReply());
			st.setInt(2, workDTO.getWork_seq());
			
			int result = st.executeUpdate();
			
			DBConnector.disConnect(st, con);
			
			return result;
		}
		//salesReuqestViewDelete
	public void salesRequestViewDelete(int work_seq) throws Exception {
		sqlSession.delete(NAMESPACE+"salesRequestViewDelete", work_seq);
	}
		
	//viewUpdate
	public int salesViewUpdate(WorkDTO workDTO) throws Exception {
		return sqlSession.update(NAMESPACE+"salesViewUpdate", workDTO);
	}
		
		public String searchWork(int work_seq) throws Exception{
			Connection con = DBConnector.getConnect();
			String sql = "select work from work_info where work_seq=?";
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setInt(1, work_seq);
			ResultSet rs = st.executeQuery();
			
			String work = null;
			if(rs.next()){
				work = rs.getString("work");
			}
			
			DBConnector.disConnect(rs, st, con);
			
			return work;
			
		}
		//총 수익금액 계산 메소드
		public int totalMoney(int user_num) throws Exception	{
			Connection con = DBConnector.getConnect();
			
			String sql = "select sum(download_hit*price) from work_info where user_num=?";
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setInt(1, user_num);
			ResultSet rs = st.executeQuery();
			rs.next();
			
			int result = rs.getInt(1);
			
			DBConnector.disConnect(rs, st, con);
			
			return result;
			
		}
		
		//판매 작품 총 갯수 구하는 메소드
		public int workTotalCount(int user_num) throws Exception	{
			Connection con = DBConnector.getConnect();
			
			String sql = "select count(*) from work_info where user_num=? and upload_check='승인'";
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setInt(1, user_num);
			ResultSet rs = st.executeQuery();
			
			rs.next();
			
			int result = rs.getInt(1);
			
			DBConnector.disConnect(rs, st, con);
			
			return result;
			
		}
		
		
			//모든정보 가져와!
			public List<WorkDTO> selectAll(Connection con) throws Exception {
				String sql = "SELECT * FROM work_info where upload_check='승인' and sell='Y'";
				PreparedStatement st = con.prepareStatement(sql);
				ResultSet rs = st.executeQuery();
				List<WorkDTO> ar = new ArrayList<WorkDTO>();
				WorkDTO workDTO = null;
				
				while(rs.next()) {
					workDTO = new WorkDTO();
					workDTO.setWork(rs.getString("work"));
					workDTO.setUser_num(rs.getInt("user_num"));
					workDTO.setNickname(memberDAO.searchNickName(rs.getInt("user_num"), "person"));
					workDTO.setWork_seq(rs.getInt("work_seq"));
					workDTO.setWork_date(rs.getDate("work_date"));
					workDTO.setUpload_check(rs.getString("upload_check"));
					workDTO.setTag(rs.getString("tag"));
					workDTO.setPrice(rs.getInt("price"));
					workDTO.setContents(rs.getString("contents"));
					workDTO.setReply(rs.getString("reply"));
					ar.add(workDTO);
				}
				rs.close();
				st.close();
				return ar;
			}
		
		public int update(WorkDTO workDTO) throws Exception	{
			Connection con = DBConnector.getConnect();
			
			String sql = "update work_info set work=?, work_date=?, price=?, contents=?, tag=? where work_seq=?";
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setString(1, workDTO.getWork());
			st.setDate(2, workDTO.getWork_date());
			st.setInt(3, workDTO.getPrice());
			st.setString(4, workDTO.getContents());
			st.setString(5, workDTO.getTag());
			st.setInt(6, workDTO.getWork_seq());
			
			int result = st.executeUpdate();
			
			DBConnector.disConnect(st, con);
			
			return result;
			
		}
		
		public WorkDTO selectOne(int work_seq, Connection con) throws Exception{
			String sql = "select * from work_info where work_seq=?";
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setInt(1, work_seq);
			
			ResultSet rs = st.executeQuery();
			WorkDTO workDTO = null;
			
			if(rs.next()){
				workDTO = new WorkDTO();
				workDTO.setWork(rs.getString("work"));
				workDTO.setUser_num(rs.getInt("user_num"));
				workDTO.setNickname(memberDAO.searchNickName(rs.getInt("user_num"), "person"));
				workDTO.setWork_seq(rs.getInt("work_seq"));
				workDTO.setWork_date(rs.getDate("work_date"));
				workDTO.setUpload_check(rs.getString("upload_check"));
				workDTO.setTag(rs.getString("tag"));
				workDTO.setPrice(rs.getInt("price"));
				workDTO.setContents(rs.getString("contents"));
				workDTO.setReply(rs.getString("reply"));
			}
			
			rs.close();
			st.close();
			
			return workDTO;
		}
		
		public List<WorkDTO> adminSelectList(RowNum rowNum) throws Exception{
			Connection con = DBConnector.getConnect();
			String sql = "select * from "
					+	"(select rownum R, Q.* from "
					+   "(select * from work_info where upload_check='대기중' order by work_seq desc) Q) "
					+   "where R between ? and ?";
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setInt(1, rowNum.getStartRow());
			st.setInt(2, rowNum.getLastRow());
			
			ResultSet rs = st.executeQuery();
			List<WorkDTO> ar = new ArrayList<WorkDTO>();
			
			while(rs.next()){
				WorkDTO workDTO = new WorkDTO();
				workDTO.setWork_seq(rs.getInt("work_seq"));
				workDTO.setWork(rs.getString("work"));
				workDTO.setNickname(memberDAO.searchNickName(rs.getInt("user_num"), "person"));
				workDTO.setWork_date(rs.getDate("work_date"));
				workDTO.setUpload_check(rs.getString("upload_check"));
				workDTO.setDownload_hit(rs.getInt("download_hit"));
				workDTO.setPrice(rs.getInt("price"));
				workDTO.setUser_num(rs.getInt("user_num"));
				
				ar.add(workDTO);
			}
			
			DBConnector.disConnect(rs, st, con);
			
			return ar;
		}
		
		public List<WorkDTO> MoneySelectList(int user_num, RowNum rowNum) throws Exception{
			Connection con = DBConnector.getConnect();
			String sql = "select * from "
					+	"(select rownum R, Q.* from "
					+   "(select * from work_info where user_num=? and upload_check='승인' order by work_seq desc) Q) "
					+   "where R between ? and ?";
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setInt(1, user_num);
			st.setInt(2, rowNum.getStartRow());
			st.setInt(3, rowNum.getLastRow());
			
			ResultSet rs = st.executeQuery();
			List<WorkDTO> ar = new ArrayList<WorkDTO>();
			
			while(rs.next()){
				WorkDTO workDTO = new WorkDTO();
				workDTO.setWork_seq(rs.getInt("work_seq"));
				workDTO.setWork(rs.getString("work"));
				workDTO.setNickname(memberDAO.searchNickName(rs.getInt("user_num"), "person"));
				workDTO.setWork_date(rs.getDate("work_date"));
				workDTO.setUpload_check(rs.getString("upload_check"));
				workDTO.setDownload_hit(rs.getInt("download_hit"));
				workDTO.setPrice(rs.getInt("price"));
				
				ar.add(workDTO);
			}
			
			DBConnector.disConnect(rs, st, con);
			
			return ar;
		}
		
		public List<WorkDTO> selectList(int user_num, RowNum rowNum) throws Exception{
			Connection con = DBConnector.getConnect();
			String sql = "select * from "
					+	"(select rownum R, Q.* from "
					+   "(select * from work_info where user_num=? order by work_seq desc) Q) "
					+   "where R between ? and ?";
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setInt(1, user_num);
			st.setInt(2, rowNum.getStartRow());
			st.setInt(3, rowNum.getLastRow());
			
			ResultSet rs = st.executeQuery();
			List<WorkDTO> ar = new ArrayList<WorkDTO>();
			
			String nickName = memberDAO.searchNickName(user_num, "person");
			while(rs.next()){
				WorkDTO workDTO = new WorkDTO();
				workDTO.setWork_seq(rs.getInt("work_seq"));
				workDTO.setWork(rs.getString("work"));
				workDTO.setNickname(nickName);
				workDTO.setWork_date(rs.getDate("work_date"));
				workDTO.setUpload_check(rs.getString("upload_check"));
				workDTO.setDownload_hit(rs.getInt("download_hit"));
				workDTO.setPrice(rs.getInt("price"));
				
				ar.add(workDTO);
			}
			
			DBConnector.disConnect(rs, st, con);
			
			return ar;
		}
		//관리자 작품승인요청에 사용될 totalCount
		public int getTotalCount() throws Exception	{
			Connection con = DBConnector.getConnect();
			String sql = "select count(nvl(work_seq, 0)) from work_info where upload_check='대기중'";
			PreparedStatement st = con.prepareStatement(sql);
			
			ResultSet rs = st.executeQuery();
			rs.next();
			int result = rs.getInt(1);
			DBConnector.disConnect(rs, st, con);
			
			return result;
		}
		//수익현황에 사용될 totalCount
		public int getTotalCount(int user_num, String check) throws Exception	{
			Connection con = DBConnector.getConnect();
			String sql = "select count(nvl(work_seq, 0)) from work_info where user_num=? and upload_check=?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, user_num);
			st.setString(2, check);
			ResultSet rs = st.executeQuery();
			rs.next();
			int result = rs.getInt(1);
			DBConnector.disConnect(rs, st, con);
			
			return result;
		}
		//총 내 작품에 쓰이는 토탈카운트 메소드
		public int getTotalCount(int user_num) throws Exception	{
			Connection con = DBConnector.getConnect();
			String sql = "select count(nvl(work_seq, 0)) from work_info where user_num=?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, user_num);
			ResultSet rs = st.executeQuery();
			rs.next();
			int result = rs.getInt(1);
			DBConnector.disConnect(rs, st, con);
			
			return result;
		}
		
		public int insert(WorkDTO workDTO) throws Exception {
			return sqlSession.insert(NAMESPACE+"insert",workDTO);
			/*String sql = "INSERT INTO work_info VALUES(?,?,?,sysdate,'대기중',?,?,?,?,'N',0)";
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, workDTO.getWork_seq());
			st.setString(2, workDTO.getWork());
			st.setInt(3, workDTO.getUser_num());
			st.setString(4, workDTO.getTag());
			st.setInt(5, workDTO.getPrice());
			st.setString(6, workDTO.getContents());
			st.setString(7, workDTO.getReply());
			int result = st.executeUpdate();
			st.close();
			return result;*/
		}
		
		//work_seq 찾기
		public int fileNumSelect() throws Exception {
			return sqlSession.selectOne(NAMESPACE+"fileNumSelect");
		}
}
