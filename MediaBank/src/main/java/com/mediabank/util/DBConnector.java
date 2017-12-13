package com.mediabank.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBConnector {
	public static Connection getConnect() throws Exception	{
		//1.로그인 정보
    	String user="MediaBank";
    	String password="MediaBank";
    	String url="jdbc:oracle:thin:@localhost:1521:xe";
    	String driver="oracle.jdbc.driver.OracleDriver";
    	//2.드라이버를 메모리에 로딩
    	Class.forName(driver);
    	//3.connection
    	Connection con = DriverManager.getConnection(url, user, password);
    	
    	return con;
	}
	
	public static void disConnect(ResultSet rs, PreparedStatement st, Connection con) throws Exception	{
		rs.close();
		st.close();
		con.close();
	}
	
	public static void disConnect(PreparedStatement st, Connection con) throws Exception	{
		st.close();
		con.close();
	}
}
