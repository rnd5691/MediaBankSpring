package com.mediabank.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.mediabank.util.DBConnector;

@Repository
public class MemberDAO {
	@Autowired
	private SqlSession sqlSession;
	
	private static final String NAMESAPCE = "memberMapper.";
	//회원 계정 종류 구하기
		public String searchKind(int user_num) throws Exception{
			return sqlSession.selectOne(NAMESAPCE+"searchKind", user_num);
		}
		
		//닉네임 구하기
		public String searchNickName(int user_num, String member_kind) throws Exception{
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("user_num", user_num);
			map.put("member_kind", member_kind);
			System.out.println("닉네임구하기 시작!");
			System.out.println("보내고자 하는 user_num : "+user_num);
			System.out.println("보내고자 하는 member_kind : "+member_kind);
			return sqlSession.selectOne(NAMESAPCE+"searchNickName", map);
		}
		//탈퇴한 회원 정보가져옴 제외하는 작업
		public List<Integer> adminDropOutWork() throws Exception{
			return sqlSession.selectList(NAMESAPCE+"adminDropOutWork");
		}
		//탈퇴하기
		public int dropOut(int user_num) throws Exception{
			return sqlSession.update(NAMESAPCE+"dropOut", user_num);
		}
		//수정 정보 업로드
		public int update(MemberDTO memberDTO) throws Exception{
			return sqlSession.update(NAMESAPCE+"update", memberDTO);
		}
		//로그인을 위한 회원 정보 조회
		public MemberDTO selectOne(MemberDTO memberDTO) throws Exception{
			return sqlSession.selectOne(NAMESAPCE+"selectOne", memberDTO);
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
		public int searchUserNum() throws Exception{
			return sqlSession.selectOne(NAMESAPCE+"searchUserNum");
		}
		//회원가입 정보 넣기(공통정보)
		public int insert(MemberDTO memberDTO) throws Exception{
			return sqlSession.insert(NAMESAPCE+"insert", memberDTO);
		}
		
		//회원가입 ID 중복확인
		public boolean checkId(String id) throws Exception{
			boolean check = true;
			MemberDTO memberDTO = sqlSession.selectOne(NAMESAPCE+"checkId", id);
			if(memberDTO != null){
				check = false;
			}
			return check;
		}
}
