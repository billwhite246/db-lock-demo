package webpro;

import  java.sql.*;

public class DbUtil {
	
	// 参数配置
	private  static  final  String  dbName     =   "xxh";
	private  static  final  String  URL        =   "jdbc:mysql://127.0.0.1:3306/" + dbName  + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
	private  static  final  String  className  =   "com.mysql.jdbc.Driver";
	private  static  final  String  user       =   "root";
	private  static  final  String  password   =   "root";
	// 数据库句柄
	public Connection  conn;
	
	DbUtil () {
		// 加载JDBC驱动
        try  {
            Class.forName(className);
            System.out.println("JDBC Load SUCCESS");
        }catch(ClassNotFoundException  e) {
            // 捕捉到错误
            System.out.println("JDBC Load error!" + e);
        }
        // 连接至数据库
        try  {
            conn  = DriverManager.getConnection(URL, user, password);
            System.out.println(dbName  + " opened SUCCESS ");
        }catch(SQLException  e) {
            // 捕捉到错误
            System.out.println(dbName  + " opened error!" + e);
        }
	}

}
