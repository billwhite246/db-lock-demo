package webpro;

import  java.sql.*;

public class DbUtil {
	
	// ��������
	private  static  final  String  dbName     =   "xxh";
	private  static  final  String  URL        =   "jdbc:mysql://127.0.0.1:3306/" + dbName  + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
	private  static  final  String  className  =   "com.mysql.jdbc.Driver";
	private  static  final  String  user       =   "root";
	private  static  final  String  password   =   "root";
	// ���ݿ���
	public Connection  conn;
	
	DbUtil () {
		// ����JDBC����
        try  {
            Class.forName(className);
            System.out.println("JDBC Load SUCCESS");
        }catch(ClassNotFoundException  e) {
            // ��׽������
            System.out.println("JDBC Load error!" + e);
        }
        // ���������ݿ�
        try  {
            conn  = DriverManager.getConnection(URL, user, password);
            System.out.println(dbName  + " opened SUCCESS ");
        }catch(SQLException  e) {
            // ��׽������
            System.out.println(dbName  + " opened error!" + e);
        }
	}

}
