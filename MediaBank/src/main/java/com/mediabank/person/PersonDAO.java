package com.mediabank.person;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PersonDAO {
	@Autowired
	private SqlSession sqlSession;
	
	private static final String NAMESPACE = "personMapper.";
	//작성자가 일반계정인지 확인 하는 부분
	public int kindCheck(String search) throws Exception{
		int result=0;
		PersonDTO personDTO = sqlSession.selectOne(NAMESPACE+"kindCheck", search);
		if(personDTO != null) {
			result=1;
		}
		return result;
	}
	//작성자
	public String selectWriter(int user_num) throws Exception{
		return sqlSession.selectOne(NAMESPACE+"selectWriter", user_num);
	}
	//작가인지 아닌지 확인
	public String checkArt(int user_num) throws Exception{
		return sqlSession.selectOne(NAMESPACE+"checkArt", user_num);
	}
	//수정 업로드
	public int upload(PersonDTO personDTO) throws Exception{
		return sqlSession.update(NAMESPACE+"upload", personDTO);
	}
	public PersonDTO selectOne(int user_num) throws Exception{
		System.out.println("user_num:"+user_num);
		return sqlSession.selectOne(NAMESPACE+"selectOne", user_num);
	}
	//회원가입
	public int insert(PersonDTO personDTO) throws Exception{
		return sqlSession.insert(NAMESPACE+"insert", personDTO);
	}
}
