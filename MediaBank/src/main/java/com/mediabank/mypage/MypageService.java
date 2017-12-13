package com.mediabank.mypage;

import java.sql.Connection;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.mediabank.company.CompanyDAO;
import com.mediabank.company.CompanyDTO;
import com.mediabank.member.MemberDAO;
import com.mediabank.member.MemberDTO;
import com.mediabank.person.PersonDAO;
import com.mediabank.person.PersonDTO;
import com.mediabank.util.DBConnector;

@Service
public class MypageService {
	@Autowired
	private PersonDAO personDAO;
	@Autowired
	private CompanyDAO companyDAO;
	@Autowired
	private MemberDAO memberDAO;
	
	//---------<내 작품 판매승인 요청 현황>---
	public void salesRequestList() throws Exception{
		
	}
	//---------<내정보 메뉴 관련>---------
	public int update(MemberDTO memberDTO,PersonDTO personDTO,CompanyDTO companyDTO) throws Exception{
		
		Connection con = null;
		int result = 0;
		if(memberDTO.getToken()!=null){
			memberDTO.setPw("NULL");
		}
		
		try{
			con = DBConnector.getConnect();
			con.setAutoCommit(false);
			
			result = memberDAO.update(memberDTO, con);
			
			if(memberDTO.getKind().equals("company")){
				result = companyDAO.update(companyDTO, con);
			}else{
				result = personDAO.upload(personDTO, con);
			}
			
			con.commit();
		}catch(Exception e){
			e.printStackTrace();
			con.rollback();
		}finally{
			con.setAutoCommit(true);
		}
		
		return result;
	}
	
	public MemberDTO mypageAdd(Model model,HttpSession session) throws Exception{
		MemberDTO memberDTO = (MemberDTO)session.getAttribute("member");
		if(memberDTO != null){
			if(memberDTO.getKind().equals("company")){
				CompanyDTO companyDTO = companyDAO.selectOne(memberDTO.getUser_num());
				model.addAttribute("company", companyDTO);
			}else{
				PersonDTO personDTO = personDAO.selectOne(memberDTO.getUser_num());
				model.addAttribute("person", personDTO);
			}
		}
		
		return memberDTO;
	}
	public MemberDTO myinfo(HttpSession session) throws Exception{
		MemberDTO memberDTO = (MemberDTO)session.getAttribute("member");
		if(memberDTO!=null){
			String artist = null;
			if(memberDTO.getKind().equals("person")){
				artist = personDAO.checkArt(memberDTO.getUser_num());
			}
			session.setAttribute("artist", artist);
		}
		
		return memberDTO;
	}
}
