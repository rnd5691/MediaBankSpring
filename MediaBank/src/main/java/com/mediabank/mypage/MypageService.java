package com.mediabank.mypage;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mediabank.company.CompanyDAO;
import com.mediabank.company.CompanyDTO;
import com.mediabank.file.FileDAO;
import com.mediabank.file.FileDTO;
import com.mediabank.member.MemberDAO;
import com.mediabank.member.MemberDTO;
import com.mediabank.person.PersonDAO;
import com.mediabank.person.PersonDTO;
import com.mediabank.util.DBConnector;
import com.mediabank.util.ListData;
import com.mediabank.util.Pager;
import com.mediabank.util.RowNum;
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
	@Autowired
	private FileDAO fileDAO;
	//--------------<탈퇴하기>-----------------
	@Transactional
	public int dropOut(MemberDTO memberDTO) throws Exception{
		int result = 0;
		//회원 탈퇴 정보 추가
		result = memberDAO.dropOut(memberDTO.getUser_num());
		//탈퇴 회원 작품이 있는지 확인
		int work = workDAO.workTotalCount(memberDTO.getUser_num());
		//작품이 있을 경우 판매유무를 모두 N으로 변경
		if(work>0) {
			result = workDAO.dropOut(memberDTO.getUser_num());
		}
		return result;
	}
	//--------------<작품 별 수익 현황>-------------
	public void salesRequestMoney(ListData listData,Model model,int curPage, MemberDTO memberDTO) throws Exception{
		int user_num = memberDTO.getUser_num();
		int totalCount = workDAO.getTotalCount(user_num, "승인");
		int workTotalCount = workDAO.getTotalCount(user_num);
		RowNum rowNum = listData.makeRow(10);
		Pager pager = listData.makePage(totalCount);
		
		
		List<WorkDTO> ar = workDAO.MoneySelectList(user_num, rowNum);
		int totalMoney = workDAO.totalMoney(user_num);
		
		model.addAttribute("totalMoney", totalMoney);
		model.addAttribute("workTotal", workTotalCount);
		model.addAttribute("list", ar);
		model.addAttribute("makePage", pager);
	}
	//---------<현재 판매중인 내 작품>-----
	public int saelsRequestNowUpdate(HttpSession session,MemberDTO memberDTO,String [] view,String file_kind) throws Exception{
		List<Integer> checkWork_seq = new ArrayList<Integer>();//체크가 된 작품들
		List<Integer> ncheckWork_seq = new ArrayList<Integer>(); //체크가 안된 작품들
		List<Integer> totalWork_seq = null; //전체 작품
		RowNum rowNum = (RowNum)session.getAttribute("makeRow");
		
		Connection con = null;
		int result = 0;
		try {
			//문자열로 되어있는 체크된 작품 번호를 숫자로 변환
			for(int i=0; i<view.length; i++) {
				int work_seq = Integer.parseInt(view[i]);
				checkWork_seq.add(work_seq);
			}
			
			totalWork_seq = fileDAO.work_seq(memberDTO.getUser_num(), file_kind, rowNum);
			System.out.println("[전체 work_seq]");
			for(int i=0; i<totalWork_seq.size(); i++) {
				System.out.print(totalWork_seq.get(i)+", ");
			}
			System.out.println();
			System.out.println("[체크 된 work_seq]");
			for(int i=0; i<checkWork_seq.size(); i++) {
				System.out.print(checkWork_seq.get(i)+", ");
			}
			System.out.println();
			
			int ch=0;
			for(int i=0; i<totalWork_seq.size(); i++) {
				boolean check = true;
				for(int j=ch; j<checkWork_seq.size(); j++) {
					ch++;
					if(checkWork_seq.get(j).equals(totalWork_seq.get(i))) {
						check = false;
						break;
					}else {
						check = true;
					}
					if(ch==checkWork_seq.size()) {
						ch=0;
					}
				}
				if(check) {
					ncheckWork_seq.add(totalWork_seq.get(i));
				}
			}
			System.out.println("[not check]");
			for(int i=0; i<ncheckWork_seq.size(); i++) {
				System.out.print(ncheckWork_seq.get(i)+", ");
			}
			
		}catch(Exception e) {
			//체크된 값이 없을 경우 진행
			ncheckWork_seq = fileDAO.work_seq(memberDTO.getUser_num(), file_kind, rowNum);
			e.printStackTrace();
		}
		
		try {
			con = DBConnector.getConnect();
			con.setAutoCommit(false);
			//체크된 작품들 판매 상태 바꾸기
			if(checkWork_seq != null) {
				for(int work : checkWork_seq) {
					result = workDAO.sellUpdate(con, work, "Y");
				}
			}
			System.out.println();
			//미체크된 작품들 판매 상태 바꾸기
			if(ncheckWork_seq != null) {
				for(int work : ncheckWork_seq) {
					result = workDAO.sellUpdate(con, work, "N");
				}
			}
			con.commit();
		}catch(Exception e) {
			con.rollback();
			e.printStackTrace();
		}finally {
			con.setAutoCommit(true);
			con.close();
		}
		
		return result;
	}
	public void saelsRequestNowAdd(HttpSession session,Model model,int curPage,String file_kind,int user_num) throws Exception{
		Pager pager = (Pager)session.getAttribute("makePage");
		RowNum rowNum = (RowNum)session.getAttribute("makeRow");
		
		List<FileDTO> imageAr = fileDAO.selectNow(user_num, file_kind, rowNum);
		List<FileDTO> videoAr = fileDAO.selectNow(user_num, file_kind, rowNum);
		
		model.addAttribute("file", imageAr);
		model.addAttribute("video", videoAr);
		model.addAttribute("file_kind", file_kind);
		model.addAttribute("user_num", user_num);
		model.addAttribute("makePage", pager);
		
		
	}
	public void salesRequestNowForm(ListData listData,HttpSession session,Model model,int curPage,String file_kind, MemberDTO memberDTO) throws Exception{
		int totalCount = fileDAO.getTotalCount(memberDTO.getUser_num(), file_kind);
		RowNum rowNum = listData.makeRow(12);
		Pager pager = listData.makePage(totalCount);
		
		session.setAttribute("makePage", pager);
		session.setAttribute("makeRow", rowNum);
		
		model.addAttribute("file_kind", file_kind);
	}
	//---------<내 작품 판매승인 요청 현황>---
	public int adminReplyUpdate(WorkDTO workDTO) throws Exception{
		return workDAO.replyUpdate(workDTO);
	}
	public int adminApprovalUpdate(int work_seq) throws Exception{
		return workDAO.approvalUpdate(work_seq);
	}
	
	@Transactional
	public void viewDelete(HttpSession session,int work_seq) throws Exception{
		FileDTO deleteDTO = fileDAO.selectOne(work_seq);
		String path = session.getServletContext().getRealPath("resources/upload/");
		this.removeFile(path, deleteDTO.getFile_name());
		fileDAO.salesRequestViewDelete(work_seq);
		workDAO.salesRequestViewDelete(work_seq);
	}
	public int viewUpdate(RedirectAttributes ra,MultipartHttpServletRequest request,HttpSession session,FileDTO fileDTO, WorkDTO workDTO) throws Exception{
		
		FileDTO deleteDTO = fileDAO.selectOne(workDTO.getWork_seq());
		MultipartFile file = request.getFile("file");
		String originalFileName = file.getOriginalFilename();//원본 파일 명
		fileDTO.setOriginalName(originalFileName);
		if(originalFileName==null) {
			originalFileName="";
		}
		String path = session.getServletContext().getRealPath("resources/upload/");
		int result=0;
		//동일한 파일일경우
		if(originalFileName=="") {
			result = workDAO.salesViewUpdate(workDTO);			
		}else {
			//파일 삭제
			this.removeFile(path, deleteDTO.getFile_name());
			File dir = new File(path);
			
			if(!dir.isDirectory()){
				dir.mkdir();
			}
			String id = UUID.randomUUID().toString();
			
			//파일 확장자 구분
			String extension = FilenameUtils.getExtension(originalFileName);
			String saveFileName = id+"."+extension;
			
			System.out.println("orginalFileName : "+originalFileName);
			System.out.println("saveFileName : "+saveFileName);
			
			if(!originalFileName.equals("")){
				String savePath = path + saveFileName;
				System.out.println("savePath : "+savePath);
				
				file.transferTo(new File(savePath));
				
				fileDTO.setFile_route(savePath);
				fileDTO.setFile_name(saveFileName);
				fileDTO.setOriginalName(originalFileName);
				System.out.println("work_seq : "+workDTO.getWork_seq());
				//파일 확장자 구분
				if(extension.equals("mp4")||extension.equals("avi")||extension.equals("flv")){
					extension="video";
					fileDTO.setFile_kind(extension);
					result = this.fileUpload(workDTO, fileDTO);
				}else if(extension.equals("jpg")||extension.equals("JPG")||extension.equals("png")||extension.equals("PNG")){
					extension="image";
					fileDTO.setFile_kind(extension);
					result = this.fileUpload(workDTO, fileDTO);;
				}else{
					ra.addFlashAttribute("message", "이미지나 동영상 형식의 파일이 아닙니다.");
					result = -1;
				}
			}
		}
		
		return result;
	}
	public void removeFile(String path,String filename){
		File file = new File(path, filename);//경로명,파일명
		System.out.println(file.getPath());
		if(file.exists()==true){
			//파일이 존재 한다면
			boolean check=file.delete();
			System.out.println(check);
		}
	}
	
	@Transactional
	public int fileUpload(WorkDTO workDTO, FileDTO fileDTO) throws Exception{
		int result = 0;
		result=workDAO.salesViewUpdate(workDTO);
		//------------------------------------------
		System.out.println("file_route : ");
		result = fileDAO.salesRequestViewUpdate(fileDTO);
		return result;
	}
	
	@Transactional
	public int fileInsert(WorkDTO workDTO, FileDTO fileDTO) throws Exception{
		int result = 0;
		
		int seq = workDAO.fileNumSelect();
		workDTO.setWork_seq(seq);
		result=workDAO.insert(workDTO);
		//------------------------------------------
		fileDTO.setWork_seq(seq);
		result = fileDAO.fileUpload(fileDTO);
		
		return result;
	}
	public int write(RedirectAttributes ra,MultipartHttpServletRequest request,HttpSession session,FileDTO fileDTO, WorkDTO workDTO) throws Exception{
		int result = 0;
		MemberDTO memberDTO = (MemberDTO)session.getAttribute("member");
		//파일 업로드 영역
		String path = session.getServletContext().getRealPath("resources/upload/");
		
		File dir = new File(path);
		
		if(!dir.isDirectory()){
			dir.mkdir();
		}
		String id = UUID.randomUUID().toString();
		MultipartFile file = request.getFile("file");
		String originalFileName = file.getOriginalFilename();//원본 파일 명
		//파일 확장자 구분
		String extension = FilenameUtils.getExtension(originalFileName);
		String saveFileName = id+"."+extension;
		
		System.out.println("orginalFileName : "+originalFileName);
		System.out.println("saveFileName : "+saveFileName);
		
		if(!originalFileName.equals("")){
				String savePath = path + saveFileName;
				System.out.println("savePath : "+savePath);
				
				file.transferTo(new File(savePath));
				
				workDTO.setNickname(personDAO.selectWriter(memberDTO.getUser_num()));
				workDTO.setUser_num(memberDTO.getUser_num());
				//------------------------------------------
				fileDTO.setFile_route(savePath);
				fileDTO.setFile_name(saveFileName);
				fileDTO.setOriginalName(originalFileName);
				//파일 확장자 구분
				if(extension.equals("mp4")||extension.equals("avi")||extension.equals("flv")){
					extension="video";
					fileDTO.setFile_kind(extension);
					result = this.fileInsert(workDTO, fileDTO);
				}else if(extension.equals("jpg")||extension.equals("JPG")||extension.equals("png")||extension.equals("PNG")){
					extension="image";
					fileDTO.setFile_kind(extension);
					result = this.fileInsert(workDTO, fileDTO);
				}else{
					ra.addFlashAttribute("message", "이미지나 동영상 형식의 파일이 아닙니다.");
					result = -1;
				}
		}
		return result;
	}
	public void writeForm(Model model,int user_num) throws Exception{
		PersonDTO personDTO = personDAO.selectOne(user_num);
		model.addAttribute("nickname", personDTO.getNickname());
	}
	public void salesRequestViewForm(Model model,int work_seq) throws Exception{
		Connection con = null;
		WorkDTO workDTO = null;
		FileDTO fileDTO = null;
		try {
			con = DBConnector.getConnect();
			
			workDTO = workDAO.selectOne(work_seq, con);
			fileDTO = fileDAO.selectOne(work_seq);
			
			model.addAttribute("work", workDTO);
			model.addAttribute("file", fileDTO);
		}catch(Exception e) {
			e.printStackTrace();
			con.rollback();
		}finally {
			con.close();
		}
	}
	public boolean salesRequestView(Model model,int work_seq) throws Exception{
		boolean check = workDAO.adminCheck(work_seq);
		
		if(check) {
			//대기중일때
			WorkDTO workDTO = null;
			FileDTO fileDTO = null;
			Connection con = null;
			
			try {
				con = DBConnector.getConnect();
				con.setAutoCommit(false);
				workDTO = workDAO.selectOne(work_seq, con);
				fileDTO = fileDAO.selectOne(work_seq);
				con.commit();
			}catch(Exception e) {
				e.printStackTrace();
				con.rollback();
			}finally {
				con.setAutoCommit(true);
				con.close();
			}
			model.addAttribute("file", fileDTO);
			model.addAttribute("work", workDTO);
		}
		
		return check;
	}
	public List<WorkDTO> salesRequestList(ListData listData,Model model,int curPage, MemberDTO memberDTO) throws Exception{
		List<WorkDTO> ar = new ArrayList<WorkDTO>();
		RowNum rowNum = listData.makeRow(10);
		int totalCount = 0;
		Pager pager = null;
		if(memberDTO.getKind().equals("admin")) {
			totalCount = workDAO.getTotalCount();
			pager = listData.makePage(totalCount);
			//탈퇴회원을 제외한 유저 번호 가져오기
			List<Integer> user_ar = memberDAO.adminDropOutWork();
			List<WorkDTO> work_ar = workDAO.adminSelectList(rowNum);
			for(int user_num : user_ar){
				for(WorkDTO workDTO : work_ar){
					if(user_num==workDTO.getUser_num()){
						ar.add(workDTO);
					}
				}
			}
		}else {
			totalCount = workDAO.getTotalCount(memberDTO.getUser_num());
			pager = listData.makePage(totalCount);
			ar = workDAO.selectList(memberDTO.getUser_num(), rowNum);
		}
		model.addAttribute("makePage", pager);
		
		return ar;
	}
	//---------<내정보 메뉴 관련>---------
	@Transactional
	public int update(HttpSession session,MemberDTO memberDTO,PersonDTO personDTO,CompanyDTO companyDTO) throws Exception{
		MemberDTO login_info = (MemberDTO)session.getAttribute("member");
		memberDTO.setKind(login_info.getKind());
		if(memberDTO.getToken()!=null){
			memberDTO.setPw("NULL");
		}
		int result = memberDAO.update(memberDTO);	
		if(memberDTO.getKind().equals("company")){
			result = companyDAO.upload(companyDTO);
		}else{
			result = personDAO.upload(personDTO);
		}
		session.setAttribute("member", memberDTO);
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
