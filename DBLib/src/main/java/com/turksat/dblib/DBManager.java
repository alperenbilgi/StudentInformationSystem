package com.turksat.dblib;

import com.turksat.dblib.model.Data;
import com.turksat.dblib.model.LectureGrade;
import com.turksat.dblib.model.LectureStudent;
import com.turksat.dblib.model.User;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class DBManager {

	private Connection conn = null;
	private PreparedStatement pstmt;
	private ResultSet res;

	public boolean connect() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		conn = DriverManager.getConnection("jdbc:postgresql://localhost/StudentInformationSystem", "postgres", "pass");
		return true;
	}

	public void closeConnection() throws SQLException {
		conn.close();
		pstmt.close();
//		res.close();
	}

	public Data checkLogin(int id, String password) throws Exception {
		Data data = new Data();
		data.setStatus(1);

		if (connect()) {
			pstmt = conn.prepareStatement("SELECT * FROM project.\"user\" WHERE user_id = ?");
			pstmt.setInt(1, id);
			res = pstmt.executeQuery();

			if (res.next() && res.getString("user_password").equals(getSHA(password))) {
				User currentUser = new User();
				currentUser.setUserID(res.getInt("user_id"));
				currentUser.setUserName(res.getString("user_name"));
				currentUser.setUserSurname(res.getString("user_surname"));
				currentUser.setUserEmail(res.getString("user_email"));
				currentUser.setUserPassword(res.getString("user_password"));
				currentUser.setUserPosition(res.getInt("user_position"));
				data.setObject(currentUser);
				data.setStatus(0);
			}

			closeConnection();
			res.close();
			insertLog("LOGIN", id + " tried to log in");
		}

		return data;
	}

	public Data changePassword(int id, String newPassword) throws Exception {
		Data data = new Data();
		data.setStatus(1);

		if (connect()) {
			pstmt = conn.prepareStatement("UPDATE project.\"user\" SET user_password = ? WHERE user_id = ?");
			pstmt.setString(1, getSHA(newPassword));
			pstmt.setInt(2, id);
			pstmt.executeUpdate();

			data.setStatus(0);
			closeConnection();
		}

		return data;
	}

	public Data courseRegistration(int lecture_id, int student_id) throws Exception {
		Data data = new Data();
		data.setStatus(1);

		if (connect()) {
			pstmt = conn.prepareStatement("INSERT INTO project.lecturestudent (SELECT ?, ? WHERE NOT EXISTS\n"
					+ "(SELECT * FROM project.lecturestudent WHERE lecturestudent_lecture = ? AND lecturestudent_student = ?))");
			pstmt.setInt(1, lecture_id);
			pstmt.setInt(2, student_id);
			pstmt.setInt(3, lecture_id);
			pstmt.setInt(4, student_id);
			pstmt.executeUpdate();

			data.setStatus(0);
			closeConnection();
		}

		return data;
	}

	public Data listLectures(int id) throws Exception {
		Data data = new Data();
		data.setStatus(1);

		if (connect()) {
			pstmt = conn.prepareStatement("SELECT *\n"
					+ "FROM project.\"user\", project.lecture, project.lecturestudent\n"
					+ "WHERE lecturestudent_lecture = lecture_id\n"
					+ "AND lecture_lecturer = user_id\n"
					+ "AND lecturestudent_student = ?");
			pstmt.setInt(1, id);
			res = pstmt.executeQuery();

			List<LectureStudent> list = null;

			pstmt = conn.prepareStatement("SELECT * FROM project.\"user\" WHERE user_id = ?");
			pstmt.setInt(1, id);
			ResultSet res2 = pstmt.executeQuery();
			User temp = null;

			if (res2.next()) {
				temp = new User();
				temp.setUserID(res2.getInt("user_id"));
				temp.setUserName(res2.getString("user_name"));
				temp.setUserSurname(res2.getString("user_surname"));
				temp.setUserEmail(res2.getString("user_email"));
				list = new ArrayList<>();
			}

			while (res.next()) {
				LectureStudent lectureStudent = new LectureStudent();

				lectureStudent.setLectureID(res.getInt("lecture_id"));
				lectureStudent.setLectureName(res.getString("lecture_name"));
				lectureStudent.setLectureCredit(res.getInt("lecture_credit"));

				lectureStudent.setUserID(res.getInt("user_id"));
				lectureStudent.setUserName(res.getString("user_name"));
				lectureStudent.setUserSurname(res.getString("user_surname"));
				lectureStudent.setUserEmail(res.getString("user_email"));

				lectureStudent.setStudent(temp);

				list.add(lectureStudent);
			}

			data.setStatus(0);
			data.setObject(list);
			closeConnection();
		}

		return data;
	}

	public Data listGrades(int id) throws Exception {
		Data data = new Data();
		data.setStatus(1);

		if (connect()) {
			pstmt = conn.prepareStatement("SELECT *\n"
					+ "FROM project.\"user\", project.lecture, project.lecturegrade\n"
					+ "WHERE lecturegrade_lecture = lecture_id\n"
					+ "AND lecture_lecturer = user_id\n"
					+ "AND lecturegrade_student = ?");
			pstmt.setInt(1, id);
			res = pstmt.executeQuery();

			List<LectureGrade> list = null;

			pstmt = conn.prepareStatement("SELECT * FROM project.\"user\" WHERE user_id = ?");
			pstmt.setInt(1, id);
			ResultSet res2 = pstmt.executeQuery();
			User temp = null;

			if (res2.next()) {
				temp = new User();
				temp.setUserID(res2.getInt("user_id"));
				temp.setUserName(res2.getString("user_name"));
				temp.setUserSurname(res2.getString("user_surname"));
				temp.setUserEmail(res2.getString("user_email"));
				list = new ArrayList<>();
			}

			while (res.next()) {
				LectureGrade lectureGrade = new LectureGrade();

				lectureGrade.setLectureID(res.getInt("lecture_id"));
				lectureGrade.setLectureName(res.getString("lecture_name"));
				lectureGrade.setLectureCredit(res.getInt("lecture_credit"));

				lectureGrade.setUserID(res.getInt("user_id"));
				lectureGrade.setUserName(res.getString("user_name"));
				lectureGrade.setUserSurname(res.getString("user_surname"));
				lectureGrade.setUserEmail(res.getString("user_email"));

				lectureGrade.setStudent(temp);

				lectureGrade.setType(res.getInt("lecturegrade_type"));
				lectureGrade.setGrade(res.getInt("lecturegrade_grade"));

				list.add(lectureGrade);
			}

			data.setStatus(0);
			data.setObject(list);
			closeConnection();
		}

		return data;
	}

	public Data assignGrade(int lecture_id, int student_id, int type, int grade) throws Exception {
		Data data = new Data();
		data.setStatus(1);

		if (connect()) {
			pstmt = conn.prepareStatement("INSERT INTO project.lecturegrade (SELECT ?, ?, ?, ? WHERE NOT EXISTS\n"
					+ "(SELECT * FROM project.lecturegrade WHERE lectureGrade_lecture = ? AND lectureGrade_student = ? AND lectureGrade_type = ?))");
			pstmt.setInt(1, lecture_id);
			pstmt.setInt(2, student_id);
			pstmt.setInt(3, type);
			pstmt.setInt(4, grade);
			pstmt.setInt(5, lecture_id);
			pstmt.setInt(6, student_id);
			pstmt.setInt(7, type);
			pstmt.executeUpdate();

			data.setStatus(0);
			closeConnection();
		}

		return data;
	}

	public Data register(int id, String name, String surname, String email, int position) {
		Data data = new Data();
		data.setStatus(1);

		try {
			if (connect()) {
				pstmt = conn.prepareStatement("INSERT INTO project.\"user\" VALUES(?, ?, ?, ?, ?, ?)");
				pstmt.setInt(1, id);
				pstmt.setString(2, name.toLowerCase());
				pstmt.setString(3, surname.toLowerCase());
				pstmt.setString(4, email);

				StringBuilder sb = new StringBuilder();
				String set = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
				for (int i = 0; i < 8; i++) {
					int k = new Random().nextInt(set.length() - 1);
					sb.append(set.charAt(k));
				}
				pstmt.setString(5, getSHA(sb.toString()));

				pstmt.setInt(6, position);
				pstmt.executeUpdate();

//			sendMail(email, "Registration", sb.toString());
				data.setStatus(0);
				closeConnection();
			}
		} catch (ClassNotFoundException | NoSuchAlgorithmException | SQLException e) {
			e.printStackTrace();
		}

		return data;
	}

	public Data deleteAccount(int id) throws Exception {
		Data data = new Data();
		data.setStatus(1);

		if (connect()) {
			pstmt = conn.prepareStatement("DELETE FROM project.\"user\" WHERE user_id = ?");
			pstmt.setInt(1, id);
			pstmt.executeUpdate();

			data.setStatus(0);
			closeConnection();
		}

		return data;
	}

	public void insertLog(String type, String message) throws Exception {
		if (connect()) {
			pstmt = conn.prepareStatement("INSERT INTO project.log VALUES(?, ?, ?, ?)");
			pstmt.setString(1, new java.sql.Time(new java.util.Date().getTime()).toString());
			pstmt.setString(2, new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
			pstmt.setString(3, type.toUpperCase(Locale.ENGLISH));
			pstmt.setString(4, message);
			pstmt.executeUpdate();

			closeConnection();
		}
	}

	public void sendMail(String email, String subject, String password) throws Exception {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", "465");

		Session session = Session.getInstance(properties, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("studentinformatiionsystem@gmail.com", "Project.123");
			}
		});

		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress("studentinformatiionsystem@gmail.com", "Student Information System"));

		message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

		if (subject.equalsIgnoreCase("Registration")) {
			message.setSubject("Test");
			String htmlText = "<font size=5><b>Your registration is completed. Welcome to Student Information System"
					+ "<br/>Your password is: </b></font>" + password;
			message.setContent(htmlText, "text/html");
		}

		Transport.send(message);
	}

	public String getSHA(String input) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		BigInteger number = new BigInteger(1, md.digest(input.getBytes(StandardCharsets.UTF_8)));
		StringBuilder hexString = new StringBuilder(number.toString(16));

		while (hexString.length() < 32) {
			hexString.insert(0, '0');
		}

		return hexString.toString();
	}
}
