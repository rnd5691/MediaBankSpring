package com.mediabank.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.mediabank.util.DBConnector;

@Repository
public class MemberDAO {
	//회원 계정 종류 구하기
		public String searchKind(int user_num) throws Exception{
			Connection con = DBConnector.getConnect();
			String sql = "select kind from user_info where user_num=?";
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setInt(1, user_num);
			
			ResultSet rs = st.executeQuery();
			String kind = null;
			if(rs.next()){
				kind = rs.getString("kind");
			}
			
			DBConnector.disConnect(rs, st, con);
			
			return kind;
		}
		/*//회사명 구하기
		public String searchCompanyName(int user_num) throws Exception{
			Connection con = DBConnector.getConnect();
			String sql = "select company_name from company where user_num=?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, user_num);
			
			ResultSet rs = st.executeQuery();
			String company_name = null;
			if(rs.next()){
				company_name = rs.getString("company_name");
			}
			
			DBConnector.disConnect(rs, st, con);
			
			return company_name;
		}*/
		//닉네임 구하기
		public String searchNickName(int user_num, String member_kind) throws Exception{
			
			Connection con = DBConnector.getConnect();
			String sql = null;
			if(member_kind.equals("company")) {
				sql = "select company_name from "+member_kind+" where user_num=?";
			}else {
				sql = "select nickname from "+member_kind+" where user_num=?";			
			}
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, user_num);
			
			ResultSet rs = st.executeQuery();
			String nickname = null;
			if(rs.next()){
				if(member_kind.equals("company")) {
					nickname = rs.getString("company_name");
				}else {				
					nickname = rs.getString("nickname");
				}
			}
			
			DBConnector.disConnect(rs, st, con);
			
			return nickname;
		}
		//탈퇴한 회원 정보가져옴 제외하는 작업
		public List<Integer> adminDropOutWork() throws Exception{
			Connection con = DBConnector.getConnect();
			String sql = "select user_num from user_info where session_check='N'";
			PreparedStatement st = con.prepareStatement(sql);
				
			ResultSet rs = st.executeQuery();
			List<Integer> ar = new ArrayList<Integer>();
				
			while(rs.next()){
				int user_num = rs.getInt("user_num");
				ar.add(user_num);
			}
				
			DBConnector.disConnect(rs, st, con);
				
			return ar;
		}
		//탈퇴하기
		public int dropOut(int user_num, Connection con) throws Exception{
			String sql = "update user_info set session_check='Y' where user_num=?";
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setInt(1, user_num);
			
			int result = st.executeUpdate();
			st.close();
			return result;
		}
		//수정 정보 업로드
		public int update(MemberDTO memberDTO, Connection con) throws Exception{
			String sql = "update user_info set pw=?, phone=?, email=? where user_num=?";
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setString(1, memberDTO.getPw());
			st.setString(2, memberDTO.getPhone());
			st.setString(3, memberDTO.getEmail());
			st.setInt(4, memberDTO.getUser_num());
			
			int result = st.executeUpdate();
			
			st.close();
			
			return result;
		}
		//로그인을 위한 회원 정보 조회
		public MemberDTO selectOne(MemberDTO memberDTO) throws Exception{
			Connection con = DBConnector.getConnect();
			String sql = "select * from user_info where id=? and pw=? and kind=? and session_check='N'";
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, memberDTO.getId());
			st.setString(2, memberDTO.getPw());
			st.setString(3, memberDTO.getKind());
			
			ResultSet rs = st.executeQuery();
				
			if(rs.next()) {
				memberDTO.setEmail(rs.getString("email"));
				memberDTO.setPhone(rs.getString("phone"));
				memberDTO.setToken(rs.getString("token"));
				memberDTO.setUser_num(rs.getInt("user_num"));
			}else {
				memberDTO = null;
			}
			DBConnector.disConnect(rs, st, con);
				
			return memberDTO;
				
		}
		//회원번호 가져오기(네아로 회원)
		public MemberDTO searchUser(MemberDTO memberDTO) throws Exception{
			Connection con = DBConnector.getConnect();
			String sql = "select * from user_info where id=? and token=? and kind=? and session_check='N'";

			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, memberDTO.getId());
			st.setString(2, memberDTO.getToken());
			st.setString(3, memberDTO.getKind());
			
			ResultSet rs = st.executeQuery();
			
			if(rs.next()){
				memberDTO.setEmail(rs.getString("email"));
				memberDTO.setPhone(rs.getString("phone"));
				memberDTO.setUser_num(rs.getInt("user_num"));
			}
			DBConnector.disConnect(rs, st, con);
			
			return memberDTO;
		}
		//회원 번호 가져오기(일반회원)
		public int searchUserNum(Connection con) throws Exception{
			String sql = "select user_num.nextval user_num from dual";

			PreparedStatement st = con.prepareStatement(sql);
			
			ResultSet rs = st.executeQuery();
			int user_num = 0;
			if(rs.next()){
				user_num = rs.getInt("user_num");
			}
			
			rs.close();
			st.close();
			
			return user_num;
		}
		//회원가입 정보 넣기(공통정보)
		public int insert(MemberDTO memberDTO, Connection con) throws Exception{
			String sql = "insert into user_info values(?,?,?,?,?,'N',?,?)";
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setInt(1, memberDTO.getUser_num());
			st.setString(2, memberDTO.getId());
			st.setString(3, memberDTO.getPw());
			st.setString(4, memberDTO.getPhone());
			st.setString(5, memberDTO.getEmail());
			st.setString(6, memberDTO.getToken());
			st.setString(7, memberDTO.getKind());
			
			int result = st.executeUpdate();
			
			st.close();
			
			return result;
		}
		
		//회원가입 ID 중복확인
		public boolean checkId(String id) throws Exception{
			boolean check = true;
			Connection con = DBConnector.getConnect();
			
			String sql = "select * from user_info where id=? and session_check='N'";
			
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, id);
			
			ResultSet rs = st.executeQuery();
			
			if(rs.next()){
				check = false;
			}
			
			DBConnector.disConnect(rs, st, con);
			return check;
		}
}
