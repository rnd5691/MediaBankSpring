package com.mediabank.person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.stereotype.Repository;

import com.mediabank.util.DBConnector;

@Repository
public class PersonDAO {
	//작성자가 일반계정인지 확인 하는 부분
			public int kindCheck(String search) throws Exception{
				Connection con = DBConnector.getConnect();
				String sql = "select * from person where nickname like ?";
				
				PreparedStatement st = con.prepareStatement(sql);
				st.setString(1, "%"+search+"%");
				
				ResultSet rs = st.executeQuery();
				
				int result = 0;
				if(rs.next()){
					result = 1;
				}
				
				return result;
			}
		//작성자
		public String selectWriter(int user_num) throws Exception{
			Connection con = DBConnector.getConnect();
			String sql = "select nickname from person where user_num=?";
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setInt(1, user_num);
			
			ResultSet rs = st.executeQuery();
			
			String writer = null;
			
			if(rs.next()){
				writer = rs.getString("nickname");
			}
			
			DBConnector.disConnect(rs, st, con);
			
			return writer;
		}
		//작가인지 아닌지 확인
		public String checkArt(int user_num) throws Exception{
			Connection con = DBConnector.getConnect();
			String sql = "select artist from person where user_num=?";
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setInt(1, user_num);
			
			ResultSet rs = st.executeQuery();
			
			String artist = null;
			if(rs.next()){
				artist = rs.getString("artist");
			}
			
			return artist;
		}
		//수정 업로드
		public int upload(PersonDTO personDTO, Connection con) throws Exception{
			String sql = "update person set name=?, birth=?, artist=? where user_num=?";
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setString(1, personDTO.getName());
			st.setDate(2, personDTO.getBirth());
			st.setString(3, personDTO.getArtist());
			st.setInt(4, personDTO.getUser_num());
			
			int result = st.executeUpdate();
			
			st.close();
			
			return result;
		}
		public PersonDTO selectOne(int user_num) throws Exception{
			Connection con = DBConnector.getConnect();
			String sql = "select * from person where user_num=?";
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, user_num);
			ResultSet rs = st.executeQuery();
			PersonDTO personDTO = null;
			if(rs.next()){
				personDTO = new PersonDTO();
				personDTO.setUser_num(user_num);
				personDTO.setName(rs.getString("name"));
				personDTO.setBirth(rs.getDate("birth"));
				personDTO.setArtist(rs.getString("artist"));
				personDTO.setNickname(rs.getString("nickname"));
			}
			DBConnector.disConnect(rs, st, con);
			
			System.out.println("-------------");
			
			return personDTO;
		}
		//회원가입
		public int insert(PersonDTO personDTO, Connection con) throws Exception{
			String sql = "insert into person values(?, ?, ?, ?, ?)";
			
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setInt(1, personDTO.getUser_num());
			st.setString(2, personDTO.getNickname());
			st.setString(3, personDTO.getName());
			st.setDate(4, personDTO.getBirth());
			st.setString(5, personDTO.getArtist());
			
			int result = st.executeUpdate();
			
			return result;
		}
}
