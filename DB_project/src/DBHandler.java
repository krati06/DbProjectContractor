

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




public class DBHandler {
	// connection strings
	private static String connString = "jdbc:postgresql://localhost:5230/project";
	private static String userName = "snehal";
	private static String passWord = "";
	
	
	
	
	public static JSONObject register(String id, String hostel_id)
	{
		JSONObject obj = new JSONObject();
		
		
		
		
		try{   
			Connection conn = DriverManager.getConnection(connString, userName, passWord);
			
			PreparedStatement ps1=conn.prepareStatement("select nextval('week_id')");
			ResultSet rs1=ps1.executeQuery(); 
			int week_id=1;
			if(rs1.next()) {
			week_id=rs1.getInt(1);
			
			}
			
			PreparedStatement pStmt = conn.prepareStatement("insert into register(student_id,hostel_id,week_id) values(?,?,?);");
			pStmt.setString(1, id);
			pStmt.setString(2, hostel_id);
			pStmt.setInt(3,week_id);
			if(pStmt.executeUpdate()>0)
			{
				obj.put("status", true);
				obj.put("data","Created Post Successfully");				
			}
			else
			{
				obj.put("status",false);
				obj.put("message", "Could not Post");
			}	
			}catch (Exception sqle)
			{
				sqle.printStackTrace();
			}
		return obj;
	}
	
	public static JSONObject optout(String id,String hostel_id) throws JSONException
	{
		JSONObject obj = new JSONObject();
		try (
			    Connection conn = DriverManager.getConnection(
			    		connString, userName, "");
				
				PreparedStatement check = conn.prepareStatement("select * from register where student_id=? and hostel_id=? and week_id=?"); 
			    		
			)
		{
			PreparedStatement ps1=conn.prepareStatement("select nextval('week_id')");
			ResultSet rs1=ps1.executeQuery(); 
			int week_id=1;
			if(rs1.next()) {
			week_id=rs1.getInt(1);
			}
			
			check.setString(1, id);
			check.setString(2, hostel_id);
			check.setInt(3, week_id);
			
			ResultSet result =  check.executeQuery();
			if(result.next())
			{
				PreparedStatement commSt = conn.prepareStatement("delete from register where student_id=? and hostel_id=? and week_id=?");
				commSt.setString(1, id);
				commSt.setString(2, hostel_id);
				commSt.setInt(3, week_id);
				if(commSt.executeUpdate()>0)
				{
					obj.put("status", true);
					obj.put("data", "opted out "+id+"from hostel "+hostel_id);
				}
				else
				{
					obj.put("status", false);
					obj.put("message", "could not optout");
					
				}
			}
			else
			{
				obj.put("status", false);
				obj.put("message", "student not registered");
			}
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	private static JSONArray ResultSetConverter(ResultSet rs) throws SQLException, JSONException {
		
		// TODO Auto-generated method stub
		JSONArray json = new JSONArray();
	    ResultSetMetaData rsmd = rs.getMetaData();
	    while(rs.next()) {
	        int numColumns = rsmd.getColumnCount();
	        JSONObject obj = new JSONObject();
	        int postid=-1;
	        for (int i=1; i<numColumns+1; i++) {
	          String column_name = rsmd.getColumnName(i);

	          if(rsmd.getColumnType(i)==java.sql.Types.ARRAY){
	           obj.put(column_name, rs.getArray(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.BIGINT){
	           obj.put(column_name, rs.getInt(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.BOOLEAN){
	           obj.put(column_name, rs.getBoolean(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.BLOB){
	           obj.put(column_name, rs.getBlob(column_name));
	          }
	         
	          else if(rsmd.getColumnType(i)==java.sql.Types.DOUBLE){
	           obj.put(column_name, rs.getDouble(column_name)); 
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.FLOAT){
	           obj.put(column_name, rs.getFloat(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.INTEGER){
	           obj.put(column_name, rs.getInt(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.NVARCHAR){
	           obj.put(column_name, rs.getNString(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.VARCHAR){
	           obj.put(column_name, rs.getString(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.TINYINT){
	           obj.put(column_name, rs.getInt(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.SMALLINT){
	           obj.put(column_name, rs.getInt(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.DATE){
	           obj.put(column_name, rs.getDate(column_name));
	          }
	          else if(rsmd.getColumnType(i)==java.sql.Types.TIMESTAMP){
	          obj.put(column_name, rs.getTimestamp(column_name));   
	          }
	          else{
	           obj.put(column_name, rs.getObject(column_name));
	          }
	          
	          /*if(column_name.equals((String)"image"))
	          {
	        	  try {
	        	  obj.put(column_name, readFully(rs.getBinaryStream(column_name)));
	        	  }
	        	  catch (Exception e) {
	      			// TODO Auto-generated catch block
	      			e.printStackTrace();
	      		}
	        	  //obj.put(column_name, rs.getString(column_name));
	        	  
	          }*/
	          
	        }
	        json.put(obj);
	       
	       
	      }
	    return json;
	}
	

	
	
	
	
}
