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
import com.mediabank.util.MakePage;
import com.mediabank.util.MakeRow;
import com.mediabank.util.PageMaker;
import com.mediabank.util.salesPageMaker;
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
	//--------------<작품 별 수익 현황>-------------
	public void salesRequestMoney(Model model,int curPage, MemberDTO memberDTO) throws Exception{
		int user_num = memberDTO.getUser_num();
		int totalCount = workDAO.getTotalCount(user_num, "승인");
		int workTotalCount = workDAO.getTotalCount(user_num);
		
		PageMaker pageMaker = new PageMaker(curPage, totalCount);
		
		List<WorkDTO> ar = workDAO.MoneySelectList(user_num, pageMaker.getMakeRow());
		int totalMoney = workDAO.totalMoney(user_num);
		
		model.addAttribute("totalMoney", totalMoney);
		model.addAttribute("workTotal", workTotalCount);
		model.addAttribute("list", ar);
		model.addAttribute("makePage", pageMaker.getMakePage());
	}
	//---------<현재 판매중인 내 작품>-----
	public int saelsRequestNowUpdate(HttpSession session,MemberDTO memberDTO,String [] view,String file_kind) throws Exception{
		List<Integer> checkWork_seq = new ArrayList<Integer>();//체크가 된 작품들
		List<Integer> ncheckWork_seq = new ArrayList<Integer>(); //체크가 안된 작품들
		List<Integer> totalWork_seq = null; //전체 작품
		MakeRow makeRow = (MakeRow)session.getAttribute("makeRow");
		Connection con = null;
		int result = 0;
		try {
			//문자열로 되어있는 체크된 작품 번호를 숫자로 변환
			for(int i=0; i<view.length; i++) {
				int work_seq = Integer.parseInt(view[i]);
				checkWork_seq.add(work_seq);
			}
			
			totalWork_seq = fileDAO.work_seq(memberDTO.getUser_num(), file_kind, makeRow);
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
				System.out.println("i:"+i+", totalWork_seq : "+totalWork_seq.get(i));
				for(int j=ch; j<checkWork_seq.size(); j++) {
					System.out.println("j:"+j+", checkWork_seq : "+checkWork_seq.get(j));
					ch++;
					if(checkWork_seq.get(j).equals(totalWork_seq.get(i))) {
						System.out.println("이리로 옴");
						check = false;
						break;
					}else {
						System.out.println("요기로 옴");
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
			ncheckWork_seq = fileDAO.work_seq(memberDTO.getUser_num(), file_kind, makeRow);
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
		MakePage makePage = (MakePage)session.getAttribute("makePage");
		MakeRow makeRow = (MakeRow)session.getAttribute("makeRow");
		
		List<FileDTO> imageAr = fileDAO.selectNow(user_num, file_kind, makeRow);
		List<FileDTO> videoAr = fileDAO.selectNow(user_num, file_kind, makeRow);
		
		model.addAttribute("file", imageAr);
		model.addAttribute("video", videoAr);
		model.addAttribute("file_kind", file_kind);
		model.addAttribute("user_num", user_num);
		model.addAttribute("makePage", makePage);
		
		
	}
	public void salesRequestNowForm(HttpSession session,Model model,int curPage,String file_kind, MemberDTO memberDTO) throws Exception{
		int totalCount = fileDAO.getTotalCount(memberDTO.getUser_num(), file_kind);
		salesPageMaker pageMaker = new salesPageMaker(curPage, totalCount);	
		session.setAttribute("makePage", pageMaker.getMakePage());
		session.setAttribute("makeRow", pageMaker.getMakeRow());
		
		model.addAttribute("file_kind", file_kind);
	}
	//---------<내 작품 판매승인 요청 현황>---
	public int adminReplyUpdate(WorkDTO workDTO) throws Exception{
		return workDAO.replyUpdate(workDTO);
	}
	public int adminApprovalUpdate(int work_seq) throws Exception{
		return workDAO.approvalUpdate(work_seq);
	}
	public void viewDelete(HttpSession session,int work_seq) throws Exception{
		Connection con = null;
		try{
			con = DBConnector.getConnect();
			con.setAutoCommit(false);
			FileDTO deleteDTO = fileDAO.selectOne(work_seq);
			String path = session.getServletContext().getRealPath("resources/upload/");
			this.removeFile(path, deleteDTO.getFile_name());
			fileDAO.salesRequestViewDelete(work_seq, con);
			workDAO.salesRequestViewDelete(work_seq, con);
			con.commit();
		}catch(Exception e){
			e.printStackTrace();
			con.rollback();
		}finally{
			con.setAutoCommit(true);
			con.close();
		}
	}
	public int viewUpdate(RedirectAttributes ra,MultipartHttpServletRequest request,HttpSession session,FileDTO fileDTO, WorkDTO workDTO) throws Exception{
		FileDTO deleteDTO = fileDAO.selectOne(workDTO.getWork_seq());
		String path = session.getServletContext().getRealPath("resources/upload/");
		int result=0;
		//파일 삭제
		this.removeFile(path, deleteDTO.getFile_name());
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
				
				fileDTO.setFile_route(savePath);
				fileDTO.setFile_name(saveFileName);
				
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
	public int fileUpload(WorkDTO workDTO, FileDTO fileDTO) throws Exception{
		Connection con = null;
		int result = 0;
		try{
			con = DBConnector.getConnect();
			con.setAutoCommit(false);
			
			result=workDAO.salesViewUpdate(workDTO, con);
			//------------------------------------------
			
			result = fileDAO.salesRequestViewUpdate(fileDTO, con);
		}catch(Exception e){
			con.rollback();
			e.printStackTrace();
		}finally{
			con.setAutoCommit(true);
			con.close();
		}
		
		return result;
	}
	public int fileInsert(WorkDTO workDTO, FileDTO fileDTO) throws Exception{
		Connection con = null;
		int result = 0;
		try{
			con = DBConnector.getConnect();
			con.setAutoCommit(false);
			int seq = workDAO.fileNumSelect(con);
			workDTO.setWork_seq(seq);
			result=workDAO.insert(workDTO, con);
			//------------------------------------------
			fileDTO.setWork_seq(seq);
			result = fileDAO.fileUpload(fileDTO, con);
		}catch(Exception e){
			con.rollback();
			e.printStackTrace();
		}finally{
			con.setAutoCommit(true);
			con.close();
		}
		
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
				
				System.out.println("work_seq : "+workDTO.getWork_seq());
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
