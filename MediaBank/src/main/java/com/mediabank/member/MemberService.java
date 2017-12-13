package com.mediabank.member;

import java.sql.Connection;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mediabank.company.CompanyDAO;
import com.mediabank.company.CompanyDTO;
import com.mediabank.person.PersonDAO;
import com.mediabank.person.PersonDTO;
import com.mediabank.util.DBConnector;

@Service
public class MemberService {
	@Autowired
	private MemberDAO memberDAO;
	@Autowired
	private CompanyDAO companyDAO;
	@Autowired
	private PersonDAO personDAO;
	/*
	  -------------------------------------------------
	  [로그인]
	*/
	public String login(MemberDTO memberDTO,HttpSession session) throws Exception{
		memberDTO = memberDAO.selectOne(memberDTO);
		String message = "로그인 실패 하셨습니다.";
		if(memberDTO != null){
			session.setAttribute("member", memberDTO);
			message = memberDTO.getId()+"님 어서오세요.";
		}
		
		return message;
	}
	/*
	  -------------------------------------------------
	  [회원가입]
	*/
	public boolean idCheck(String id) throws Exception{
		return memberDAO.checkId(id);
	}
	public int insert(MemberDTO memberDTO, CompanyDTO companyDTO, PersonDTO personDTO) throws Exception{
		//company계정인지 person계정인지 비교
		int result = 0;
		Connection con = null;
		try{
			con = DBConnector.getConnect();
			con.setAutoCommit(false);
			int user_num = memberDAO.searchUserNum(con);
			memberDTO.setUser_num(user_num);
			result = memberDAO.insert(memberDTO, con);
			
			if(memberDTO.getKind().equals("company")){
				companyDTO.setUser_num(user_num);
				result = companyDAO.insert(companyDTO, con);
			}else{
				personDTO.setUser_num(user_num);
				result = personDAO.insert(personDTO, con);
			}
			
			con.commit();
		}catch(Exception e){
			con.rollback();
			e.printStackTrace();
		}finally{
			con.setAutoCommit(true);
			con.close();
		}
		
		return result;
	}
	
}