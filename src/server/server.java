/**
 * 
 */
package server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.imageio.ImageIO;

import com.pervasive.jdbc.v2.Connection;

import custom.DBConnection;
import pojo.User;

/**
 * @author Ali Hassan
 *
 */

// setUpConnection(); // Connection setup
// User u = new User();
// u.setId(3);
// u.setName("Mano");
// u.setPhoto(convertImage(".\\image\\cat.jpg"));
//
// System.out.println("Original = "+convertImage(".\\image\\cat.jpg").length);
//
// System.out.println(squish(convertImage(".\\image\\cat.jpg")).length);
// System.out.println(bloat(squish(convertImage(".\\image\\cat.jpg"))).length);

public class server implements Runnable {
	public static final String dir = ".\\image\\";
	Socket soc = null;

	server(Socket soc) {
		this.soc = soc;
	}

	@Override
	public void run() {
		InputStream inputStream = null;
		DBConnection db = new DBConnection();
		try {
			Connection con = db.setUpConnection();
			// Reading Data
			ObjectInputStream clientInputStream = new ObjectInputStream(soc.getInputStream());
			User u = (User)clientInputStream.readObject();
			System.out.println("---<Data Received>----\n"+u.getId()+"  "+u.getName());
			// Reading Image File
			inputStream = this.soc.getInputStream();
			System.out.println("Reading: " + System.currentTimeMillis());
			byte[] sizeAr = new byte[4];
			inputStream.read(sizeAr);
			int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
			byte[] imageAr = new byte[size];
			inputStream.read(imageAr);
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
			System.out.println(
					"Received " + image.getHeight() + "x" + image.getWidth() + ": " + System.currentTimeMillis());
			ImageIO.write(image, "jpg", new File(dir + System.currentTimeMillis() + ".jpg"));

			// ============================================================================
			String sql = "INSERT INTO \"user\" (id, Name, image_dir) values (" + u.getId() + ",'" + u.getName()
					+ "', '" + dir+System.currentTimeMillis() + ".jpg');";
			PreparedStatement ps = con.prepareStatement(sql);

			ps.execute();
			System.out.println("Data Inserted Successfully.");
			con.commit();
			ps.close();
			con.close();
			inputStream.close();
			clientInputStream.close();
		} catch (IOException | SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}

	}

	public static void main(String[] args) throws Exception {
		ServerSocket serverSocket = new ServerSocket(13085);
		while (true) {
			System.out.println("Listening....");
			Socket socket = serverSocket.accept();
			server imgUploadServer = new server(socket);
			Thread thread = new Thread(imgUploadServer);
			thread.start();
		}
	}
}