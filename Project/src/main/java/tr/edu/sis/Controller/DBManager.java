package tr.edu.sis.Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import tr.edu.sis.Model.LectureGrade;
import tr.edu.sis.Model.LectureStudent;
import tr.edu.sis.Model.User;

public class DBManager implements DataOperator {

	private Connection conn = null;
	private PreparedStatement pstmt;
	private ResultSet res;

	final String email_regex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
	final String password_regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

	private final PropertiesManager prop;
	private final String smtpHostKey, smtpSocketFactoryPortKey, smtpSocketFactoryClassKey, smtpAuthKey, smtpPortKey;
	private final String smtpHostValue, smtpSocketFactoryPortValue, smtpSocketFactoryClassValue, smtpAuthValue, smtpPortValue;

	public DBManager() {
		AbstractApplicationContext context = new AnnotationConfigApplicationContext(PropertiesManager.class);
		prop = (PropertiesManager) context.getBean("dataSource");

		smtpHostKey = prop.getSmtpHostKey();
		smtpHostValue = prop.getSmtpHostValue();
		smtpSocketFactoryPortKey = prop.getSmtpSocketFactoryPortKey();
		smtpSocketFactoryPortValue = prop.getSmtpSocketFactoryPortValue();
		smtpSocketFactoryClassKey = prop.getSmtpSocketFactoryClassKey();
		smtpSocketFactoryClassValue = prop.getSmtpSocketFactoryClassValue();
		smtpAuthKey = prop.getSmtpAuthKey();
		smtpAuthValue = prop.getSmtpAuthValue();
		smtpPortKey = prop.getSmtpPortKey();
		smtpPortValue = prop.getSmtpPortValue();
	}

	@Override
	public boolean connect() throws Exception {
		conn = DriverManager.getConnection(prop.getDbURL(), prop.getDbUSER(), prop.getDbPASS());
		return true;
	}

	@Override
	public void closeConnection() throws Exception {
//		conn.close();
//		pstmt.close();
//		res.close();
	}

	@Override
	public User checkLogin(int id, String password) throws Exception {
		User currentUser = null;

		if (connect()) {
			pstmt = conn.prepareStatement("SELECT * FROM project.\"user\" WHERE user_id = ?");
			pstmt.setInt(1, id);
			res = pstmt.executeQuery();

			if (res.next() && res.getString("user_password").equals(DataManager.getSHA(password))) {
				currentUser = new User();
				currentUser.setUserID(res.getInt("user_id"));
				currentUser.setUserName(res.getString("user_name"));
				currentUser.setUserSurname(res.getString("user_surname"));
				currentUser.setUserEmail(res.getString("user_email"));
				currentUser.setUserPassword(res.getString("user_password"));
				currentUser.setUserPosition(res.getInt("user_position"));

				closeConnection();
			}
		}

		return currentUser;
	}

	@Override
	public boolean changePassword(int id, String newPassword) throws Exception {
		if (connect()) {
			pstmt = conn.prepareStatement("UPDATE project.\"user\" SET user_password = ? WHERE user_id = ?");
			pstmt.setString(1, DataManager.getSHA(newPassword));
			pstmt.setInt(2, id);
			pstmt.executeUpdate();

			closeConnection();

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean courseRegistration(int lecture_id, int student_id) throws Exception {
		if (connect()) {
			pstmt = conn.prepareStatement("INSERT INTO project.lecturestudent (SELECT ?, ? WHERE NOT EXISTS\n"
					+ "(SELECT * FROM project.lecturestudent WHERE lecturestudent_lecture = ? AND lecturestudent_student = ?))");
			pstmt.setInt(1, lecture_id);
			pstmt.setInt(2, student_id);
			pstmt.setInt(3, lecture_id);
			pstmt.setInt(4, student_id);
			pstmt.executeUpdate();

			closeConnection();

			return true;
		} else {
			return false;
		}
	}

	@Override
	public List listLectures(int id) throws Exception {
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

//			for (int i = 0; i < list.size(); i++) {
//				System.out.println(
//						list.get(i).getLectureID() + " | "
//						+ list.get(i).getLectureName() + " | "
//						+ list.get(i).getLectureCredit() + " | "
//						+ list.get(i).getUserID() + " | "
//						+ list.get(i).getUserName() + " | "
//						+ list.get(i).getUserSurname() + " | "
//						+ list.get(i).getUserEmail() + " | "
//						+ list.get(i).getStudent().getUserID() + " | "
//						+ list.get(i).getStudent().getUserName() + " | "
//						+ list.get(i).getStudent().getUserSurname() + " | "
//						+ list.get(i).getStudent().getUserEmail()
//				);
//			}
			closeConnection();
			return list;
		}

		return null;
	}

	@Override
	public List listGrades(int id) throws Exception {
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

//			for (int i = 0; i < list.size(); i++) {
//				System.out.println(
//						list.get(i).getLectureID() + " | "
//						+ list.get(i).getLectureName() + " | "
//						+ list.get(i).getLectureCredit() + " | "
//						+ list.get(i).getUserID() + " | "
//						+ list.get(i).getUserName() + " | "
//						+ list.get(i).getUserSurname() + " | "
//						+ list.get(i).getUserEmail() + " | "
//						+ list.get(i).getStudent().getUserID() + " | "
//						+ list.get(i).getStudent().getUserName() + " | "
//						+ list.get(i).getStudent().getUserSurname() + " | "
//						+ list.get(i).getStudent().getUserEmail() + " | "
//						+ list.get(i).getType() + " | "
//						+ list.get(i).getGrade()
//				);
//			}
			closeConnection();
			return list;
		}

		return null;
	}

	@Override
	public boolean assignGrade(int lecture_id, int student_id, int type, int grade) throws Exception {
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

			closeConnection();

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean register(int id, String name, String surname, String email, int position) throws Exception {
		if (connect()) {
			pstmt = conn.prepareStatement("INSERT INTO project.\"user\" VALUES(?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, id);
			pstmt.setString(2, name.toLowerCase());
			pstmt.setString(3, surname.toLowerCase());
			pstmt.setString(4, email);

			StringBuilder sb = new StringBuilder();

			while (!sb.toString().matches(password_regex)) {
				sb.delete(0, sb.capacity());
				String set = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
				for (int i = 0; i < 8; i++) {
					int k = new Random().nextInt(set.length() - 1);
					sb.append(set.charAt(k));
				}
			}
			pstmt.setString(5, DataManager.getSHA(sb.toString()));

			pstmt.setInt(6, position);
			pstmt.executeUpdate();

//			sendMail(id, "Registration", sb.toString());
			closeConnection();

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean deleteAccount(int id) throws Exception {
		if (connect()) {
			pstmt = conn.prepareStatement("DELETE FROM project.\"user\" WHERE user_id = ?");
			pstmt.setInt(1, id);
			pstmt.executeUpdate();

			closeConnection();

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean insertLog(String type, String message) throws Exception {
		if (connect()) {
			pstmt = conn.prepareStatement("INSERT INTO project.log VALUES(?, ?, ?, ?)");
			pstmt.setString(1, new java.sql.Time(new java.util.Date().getTime()).toString());
			pstmt.setString(2, new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
			pstmt.setString(3, type.toUpperCase(Locale.ENGLISH));
			pstmt.setString(4, message);
			pstmt.executeUpdate();

			closeConnection();

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean checkLog() throws Exception {
		if (connect()) {
			pstmt = conn.prepareStatement("SELECT * FROM project.log WHERE \"type\" = 'TEST'");
			res = pstmt.executeQuery();

			if (res.next()) {
				pstmt = conn.prepareStatement("DELETE FROM project.log WHERE \"type\" = 'TEST'");
				pstmt.executeUpdate();

				closeConnection();

				return true;
			} else {
				closeConnection();
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public User sendMail(int id, String subject, String password) throws Exception {
		User temp = null;

		if (connect()) {
			pstmt = conn.prepareStatement("SELECT * FROM project.\"user\" WHERE user_id = ?");
			pstmt.setInt(1, id);
			res = pstmt.executeQuery();

			if (res.next()) {
				temp = new User();
				temp.setUserID(res.getInt("user_id"));
				temp.setUserName(res.getString("user_name"));
				temp.setUserSurname(res.getString("user_surname"));
				temp.setUserEmail(res.getString("user_email"));

				Properties properties = new Properties();
				properties.put(smtpHostKey, smtpHostValue);
				properties.put(smtpSocketFactoryPortKey, smtpSocketFactoryPortValue);
				properties.put(smtpSocketFactoryClassKey, smtpSocketFactoryClassValue);
				properties.put(smtpAuthKey, smtpAuthValue);
				properties.put(smtpPortKey, smtpPortValue);

				Session session = Session.getInstance(properties, new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("studentinformatiionsystem@gmail.com", "Project.123");
					}
				});

				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress("studentinformatiionsystem@gmail.com", "Student Information System"));
				message.setSubject("Test");

				message.addRecipient(Message.RecipientType.TO, new InternetAddress(temp.getUserEmail()));

				if (subject.equalsIgnoreCase("Registration")) {
					message.setContent("Your registration is completed. Welcome:\n"
							+ temp.getUserName().toUpperCase(Locale.ENGLISH) + " "
							+ temp.getUserSurname().toUpperCase(Locale.ENGLISH), "text/plain");
				}

				Transport.send(message);
			}
		}
		return temp;
	}

	@Override
	public boolean insertData(int key, String type, Object obj) {
		throw new UnsupportedOperationException("DBManager: insertData not supported yet.");
	}
}
