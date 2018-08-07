/**
 * 
 */
package custom;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.imageio.ImageIO;

import com.pervasive.jdbc.v2.Connection;
import com.pervasive.jdbc.v2.ResultSet;

import pojo.User;

/**
 * @author Ali Hassan
 *
 */

public class DBConnection {
	public static Connection con;
	
//	----------------------------------------------------------------------
//					Compression and decompression algorithm
//	----------------------------------------------------------------------

	  public static byte[] compress(byte[] data) throws IOException {  
	   Deflater deflater = new Deflater();  
	   deflater.setInput(data);  
	   ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);   
	   deflater.finish();  
	   byte[] buffer = new byte[1024];   
	   while (!deflater.finished()) {  
	    int count = deflater.deflate(buffer); // returns the generated code... index  
	    outputStream.write(buffer, 0, count);   
	   }  
	   outputStream.close();  
	   byte[] output = outputStream.toByteArray();  
	   System.out.println("Original: " + data.length / 1024 + " Kb");  
	   System.out.println("Compressed: " + output.length / 1024 + " Kb");  
	   return output;  
	  }  
	  public static byte[] decompress(byte[] data) throws IOException, DataFormatException {  
	   Inflater inflater = new Inflater();   
	   inflater.setInput(data);  
	   ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);  
	   byte[] buffer = new byte[1024];  
	   while (!inflater.finished()) {  
	    int count = inflater.inflate(buffer);  
	    outputStream.write(buffer, 0, count);  
	   }  
	   outputStream.close();  
	   byte[] output = outputStream.toByteArray();  
	   System.out.println("Original: " + data.length);  
	   System.out.println("Compressed: " + output.length);  
	   return output;  
	  }  
	
//	---------------------------------------------------------------------
	  
	  
	public static byte[] convertImage(String path) throws IOException {
		BufferedImage originalImage = ImageIO.read(new File(path));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(originalImage, "jpg", baos);
		byte[] imageInByte = baos.toByteArray();
		return imageInByte;
	}

	public static void getImage(byte[] imageInByte) throws IOException {
		InputStream in = new ByteArrayInputStream(imageInByte);
		BufferedImage bImageFromConvert = ImageIO.read(in);

		ImageIO.write(bImageFromConvert, "jpg", new File(".\\image\\cat-copy.jpg"));
	}

	public static Connection setUpConnection() {
		String url = "jdbc:pervasive://localhost:1583/test?user=tcp";
		try {
			Class.forName("com.pervasive.jdbc.v2.Driver");
			con = (Connection) DriverManager.getConnection(url);
			System.out.println("Connection Established");
		} catch (Exception e) {
			System.err.println("Connection Error");
			System.err.print("ClassNotFoundException: ");
			System.out.println(e.toString());
			System.err.println(e.getMessage());
		}
		return con;
	}

	/**
	 * @param args
	 * @throws SQLException
	 * @throws IOException
	 */
	public static void insertData(User user) throws SQLException, IOException {
		// String sql = "INSERT INTO \"user\" (id, Name, Photo) values
		// (?,?,?);";
		// PreparedStatement ps = con.prepareStatement(sql);
		// ps.setInt(1, user.getId());
		// ps.setString(2, user.getName());
		// ps.setString(3, user.getPhoto().toString());
		//String myString = Arrays.toString(user.getPhoto());

//		String sql = "INSERT INTO \"user\" (id, Name, image_dir) values (" + user.getId() + ",'" + user.getName() + "', '"+ user.getPhoto() + "');";
//		PreparedStatement ps = con.prepareStatement(sql);
//
//		ps.execute();
//		System.out.println("Data Inserted Successfully.");
//		con.commit();
//		ps.close();
	}

//	public static List<User> retrieveData() throws SQLException, IOException {
//		String sql = "SELECT * FROM \"user\"";
//		List<User> users = new ArrayList();
//		PreparedStatement ps = con.prepareStatement(sql);
//		ResultSet rs = (ResultSet) ps.executeQuery();
//		while (rs.next()) {
//			User user = new User(rs.getInt(1), rs.getString(2), rs.getString(3));
//			users.add(user);
//		}
//	 return users;
//	}
	

//		setUpConnection(); // Connection setup
//		User u = new User();
//		u.setId(3);
//		u.setName("Mano");
//		u.setPhoto(convertImage(".\\image\\cat.jpg"));
//
//		System.out.println("Original = "+convertImage(".\\image\\cat.jpg").length);
//
//		System.out.println(squish(convertImage(".\\image\\cat.jpg")).length);
//		System.out.println(bloat(squish(convertImage(".\\image\\cat.jpg"))).length);
		
		
		
		
		
		//insertData(u);

//		retrieveData();
		// List users = retrieveData();
		// for(int i=0;i<users.size();i++){
		// User user = new User();
		// users.get(i);
		// }
//		
//		con.close();
		// System.out.println(new File(".\\image\\cat.jpg").exists());
		//
		// // open image
		// File imgPath = new File(".\\image\\cat.jpg");
		//
		// BufferedImage originalImage = ImageIO.read(imgPath);
		// ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// ImageIO.write(originalImage, "jpg", baos);
		// byte[] imageInByte = baos.toByteArray();
		// // BufferedImage bufferedImage = ImageIO.read(imgPath);
		// //
		// // // get DataBufferBytes from Raster
		// // WritableRaster raster = bufferedImage .getRaster();
		// // DataBufferByte data = (DataBufferByte) raster.getDataBuffer();
		// // System.out.println(data.getData());
		// System.out.println(imageInByte);
		// //
		// // System.out.println(new File( ".\\image\\cat.jpg").exists());
		//
		// // convert byte array back to BufferedImage

}
