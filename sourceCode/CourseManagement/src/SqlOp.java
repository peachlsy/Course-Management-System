import java.sql.*;
public class SqlOp {
	public static String  url="jdbc:mysql://localhost:3306/coursemanagement?useUnicode=true&characterEncoding=utf-8&useSSL=false";
	public static String user="root";
	public static String pwd="123456";
	public static String driver ="com.mysql.jdbc.Driver";
	public static Statement getConnection() throws SQLException, ClassNotFoundException{
		Class.forName(driver );	//º”‘ÿdriver¿‡
		Connection conn=DriverManager.getConnection(url, user, pwd);
		if(!conn.isClosed()){
			//System.out.println("Succeeded connecting to the Database!");
			Statement statement = conn.createStatement();
			return statement;
		}
		else{
			return null;
		}
	}
}
