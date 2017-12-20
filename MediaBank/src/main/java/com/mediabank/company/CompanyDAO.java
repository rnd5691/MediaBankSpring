package com.mediabank.company;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyDAO {
	@Autowired
	private SqlSession sqlSession;
	
	private static final String NAMESPACE = "companyMapper.";
	//작성자가 회사계정인지 확인 하는 부분
		public int kindCheck(String search) throws Exception{
			CompanyDTO companyDTO = sqlSession.selectOne(NAMESPACE+"kindCheck", search);
			int result = 0;
			if(companyDTO!=null) {
				result=1;
			}
			return result;
		}
	//작성자
	public String selectWriter(int user_num) throws Exception{
		return sqlSession.selectOne(NAMESPACE+"selectWriter", user_num);
	}
	//업로드 부분
	public int upload(CompanyDTO companyDTO) throws Exception{
		return sqlSession.update(NAMESPACE+"upload", companyDTO);
	}
	public CompanyDTO selectOne(int user_num) throws Exception{
		return sqlSession.selectOne(NAMESPACE+"selectOne", user_num);
	}
	public int insert(CompanyDTO companyDTO) throws Exception{
		return sqlSession.insert(NAMESPACE+"insert", companyDTO);
	}
}
