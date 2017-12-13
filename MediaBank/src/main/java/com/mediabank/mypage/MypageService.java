package com.mediabank.mypage;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

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
import com.mediabank.util.PageMaker;
import com.mediabank.work.WorkDAO;
import com.mediabank.work.WorkDTO;

@Service
public class MypageService {
	@Autowired
	private PersonDAO personDAO;
	@Autowired
	private CompanyDAO companyDAO;
	@Autowired
	private MemberDAO memberDAO;
	@Autowired
	private WorkDAO workDAO;
	
	//---------<내 작품 판매승인 요청 현황>---
	public List<WorkDTO> salesRequestList(Model model,int curPage, MemberDTO memberDTO) throws Exception{
		int totalCount = 0;
		List<WorkDTO> ar = new ArrayList<WorkDTO>();
		PageMaker pageMaker = null;
		if(memberDTO.getKind().equals("admin")) {
			totalCount = workDAO.getTotalCount();
			pageMaker = new PageMaker(curPage, totalCount);
			//탈퇴회원을 제외한 유저 번호 가져오기
			List<Integer> user_ar = memberDAO.adminDropOutWork();
			List<WorkDTO> work_ar = workDAO.adminSelectList(pageMaker.getMakeRow());
			for(int user_num : user_ar){
				for(WorkDTO workDTO : work_ar){
					if(user_num==workDTO.getUser_num()){
						ar.add(workDTO);
					}
				}
			}
		}else {
			totalCount = workDAO.getTotalCount(memberDTO.getUser_num());
			pageMaker = new PageMaker(curPage, totalCount);
			ar = workDAO.selectList(memberDTO.getUser_num(), pageMaker.getMakeRow());
		}
		model.addAttribute("makePage", pageMaker.getMakePage());
		
		return ar;
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
