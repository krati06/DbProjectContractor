

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
			
			PreparedStatement ps1=conn.prepareStatement("select currval('week_id')");
			ResultSet rs1=ps1.executeQuery(); 
			int week_id=1;
			if(rs1.next()) {
				week_id=rs1.getInt(1);
			}
			
			
			PreparedStatement ps=conn.prepareStatement("select nextval('waitlist_number')");
			ResultSet rs=ps.executeQuery(); 
			int waitlist_number=1;
			if(rs.next()) {
				waitlist_number=rs.getInt(1);
			}
				
			PreparedStatement pStmt = conn.prepareStatement("insert into waitlist(student_id,hostel_id,waitlist_number,week_id) values(?,?,?,?);");
			pStmt.setString(1, id);
			pStmt.setString(2, hostel_id);
			pStmt.setInt(3,waitlist_number);
			pStmt.setInt(4,week_id);
			if(pStmt.executeUpdate()>0)
			{
				obj.put("status", true);
				obj.put("data","Added to waitlist Successfully");				
			}
			else
			{
				obj.put("status",false);
				obj.put("message", "Could not add to waitlist");
			}	
			
			}catch (Exception sqle)
			{
				sqle.printStackTrace();
			}
		return obj;
	}
	
	public static JSONObject optout(String id) throws JSONException
	{
		JSONObject obj = new JSONObject();
		try (
			    Connection conn = DriverManager.getConnection(
			    		connString, userName, "");
				
				//PreparedStatement check = conn.prepareStatement("select * from register where student_id=? and hostel_id=? and week_id=?"); 
			    		
			)
		{
			PreparedStatement ps=conn.prepareStatement("select hostel_id from student where student_id=?");
			ps.setString(1, id);
			ResultSet rs=ps.executeQuery(); 
			String hostel_id="";
			if(rs.next()) {
			hostel_id=rs.getString(1);
			}
			
			PreparedStatement ps1=conn.prepareStatement("select currval('week_id')");
			ResultSet rs1=ps1.executeQuery(); 
			int week_id=1;
			if(rs1.next()) {
			week_id=rs1.getInt(1);
			}
			
			PreparedStatement St = conn.prepareStatement("delete from register where student_id=? and hostel_id=? and week_id=?");
			St.setString(1, id);
			St.setString(2, hostel_id);
			St.setInt(3, week_id);
			
			PreparedStatement commSt = conn.prepareStatement("delete from waitlist where student_id=? and week_id=?");
			commSt.setString(1, id);
			//commSt.setString(2, hostel_id);
			commSt.setInt(2, week_id);
			if(commSt.executeUpdate()>0 && St.executeUpdate()>0)
			{
				obj.put("status", true);
				obj.put("data", "opted out "+id+"from hostel "+hostel_id+" and from every waitlist");
			}
			else
			{
				obj.put("status", false);
				obj.put("message", "could not optout");
					
			}
			}
			
		
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	
	public static JSONObject Hostel(String uid) throws JSONException{
		JSONObject obj = new JSONObject();
		try {
			obj.put("status", true);
			JSONArray jsonObj = new JSONArray();
			try{
				// Create the connection
				Connection conn = DriverManager.getConnection(connString, userName, passWord);
				String query = "select hostel_id from student where student_id = ?";
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, uid);
				ResultSet result =  preparedStmt.executeQuery();
				
				jsonObj = DBHandler.ResultSetConverter(result);	
				preparedStmt.close();
				conn.close();
				 
			} catch(Exception e){
				e.printStackTrace();
			}
			
			obj.put("data", jsonObj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	
	public static JSONObject Menu(String hostel_id, String day, String week_id) throws JSONException{
		JSONObject obj = new JSONObject();
		try {
			obj.put("status", true);
			JSONArray jsonObj = new JSONArray();
			try{
				// Create the connection
				Connection conn = DriverManager.getConnection(connString, userName, passWord);
				String query = "select dish from menu where hostel_id = ? and week_id = ? and day = ?";
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, hostel_id);
				preparedStmt.setString(2, week_id);
				preparedStmt.setString(3, day);
				ResultSet result =  preparedStmt.executeQuery();
				
				jsonObj = DBHandler.ResultSetConverter(result);	
				preparedStmt.close();
				conn.close();
				 
			} catch(Exception e){
				e.printStackTrace();
			}
			
			obj.put("data", jsonObj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	
	public static JSONObject Waitlist(String student_id, String week_interval) throws JSONException{
		JSONObject obj = new JSONObject();
		try {
			obj.put("status", true);
			JSONArray jsonObj = new JSONArray();
			try{
				// Create the connection
				Connection conn = DriverManager.getConnection(connString, userName, passWord);
				String query = "select hostel_id, waitlist_number from waitlist where student_id = ? and week_interval = ?";
				PreparedStatement preparedStmt = conn.prepareStatement(query);
				preparedStmt.setString(1, student_id);
				preparedStmt.setString(2, week_interval);
				ResultSet result =  preparedStmt.executeQuery();
				
				jsonObj = DBHandler.ResultSetConverter(result);	
				preparedStmt.close();
				conn.close();
				 
			} catch(Exception e){
				e.printStackTrace();
			}
			
			obj.put("data", jsonObj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
        public static JSONArray GetHostels(){
		JSONArray json = new JSONArray();
		try (
		    Connection conn = DriverManager.getConnection(
		    		connString, userName, "");
		    PreparedStatement postSt = conn.prepareStatement("select hostel_id from hostel");
		)
		{
			ResultSet rs = postSt.executeQuery();
			conn.close();
			json = ResultSetConverter(rs);			
			return json;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
		
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
