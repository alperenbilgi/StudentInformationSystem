package tr.edu.sis.Controller;

import java.net.InetSocketAddress;
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
import net.spy.memcached.MemcachedClient;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import tr.edu.sis.Model.LectureGrade;
import tr.edu.sis.Model.LectureStudent;
import tr.edu.sis.Model.User;

public class MemcachedManager implements DataOperator {

	private MemcachedClient spyMemCached;

	private final PropertiesManager prop;
	private final String smtpHostKey, smtpSocketFactoryPortKey, smtpSocketFactoryClassKey, smtpAuthKey, smtpPortKey;
	private final String smtpHostValue, smtpSocketFactoryPortValue, smtpSocketFactoryClassValue, smtpAuthValue, smtpPortValue;

	public MemcachedManager() {
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

		try {
			connect();
		} catch (Exception ex) {
			System.err.println(ex);
		}
	}

	@Override
	public boolean connect() throws Exception {
		spyMemCached = new MemcachedClient(new InetSocketAddress("localhost", 11211));
		return true;
	}

	@Override
	public void closeConnection() throws Exception {
		spyMemCached.shutdown();
	}

	@Override
	public User checkLogin(int id, String password) throws Exception {
		User temp;

		if (((temp = (User) spyMemCached.get(id + ".user")) != null) && temp.getUserPassword().equals(DataManager.getSHA(password))) {
			return temp;
		}

		return null;
	}

	@Override
	public boolean changePassword(int id, String newPassword) throws Exception {
		User temp;

		if ((temp = (User) spyMemCached.get(id + ".user")) != null) {
			temp.setUserPassword(DataManager.getSHA(newPassword));
			spyMemCached.delete(id + ".user");
			insertData(id, "user", temp);
			return true;
		}
		return false;
	}

	@Override
	public boolean courseRegistration(int lecture_id, int student_id) throws Exception {
		throw new UnsupportedOperationException("MemcachedManager: courseRegistration not supported yet.");
	}

	@Override
	public List listLectures(int id) throws Exception {
		List<LectureStudent> list;

		if ((list = (List) spyMemCached.get(id + ".lecturestudent")) != null) {
			for (int i = 0; i < list.size(); i++) {
				System.out.println("FROM MEMCACHED:\n"
						+ list.get(i).getLectureID() + " | "
						+ list.get(i).getLectureName() + " | "
						+ list.get(i).getLectureCredit() + " | "
						+ list.get(i).getUserID() + " | "
						+ list.get(i).getUserName() + " | "
						+ list.get(i).getUserSurname() + " | "
						+ list.get(i).getUserEmail() + " | "
						+ list.get(i).getStudent().getUserID() + " | "
						+ list.get(i).getStudent().getUserName() + " | "
						+ list.get(i).getStudent().getUserSurname() + " | "
						+ list.get(i).getStudent().getUserEmail()
				);
			}
		}

		return list;
	}

	@Override
	public List listGrades(int id) throws Exception {
		List<LectureGrade> list;

		if ((list = (List) spyMemCached.get(id + ".lecturegrade")) != null) {
			for (int i = 0; i < list.size(); i++) {
				System.out.println("FROM MEMCACHED:\n"
						+ list.get(i).getLectureID() + " | "
						+ list.get(i).getLectureName() + " | "
						+ list.get(i).getLectureCredit() + " | "
						+ list.get(i).getUserID() + " | "
						+ list.get(i).getUserName() + " | "
						+ list.get(i).getUserSurname() + " | "
						+ list.get(i).getUserEmail() + " | "
						+ list.get(i).getStudent().getUserID() + " | "
						+ list.get(i).getStudent().getUserName() + " | "
						+ list.get(i).getStudent().getUserSurname() + " | "
						+ list.get(i).getStudent().getUserEmail() + " | "
						+ list.get(i).getType() + " | "
						+ list.get(i).getGrade()
				);
			}
		}

		return list;
	}

	@Override
	public boolean assignGrade(int lecture_id, int student_id, int exam, int grade) throws Exception {
		throw new UnsupportedOperationException("MemcachedManager: assignGrade not supported yet.");
	}

	@Override
	public boolean register(int id, String name, String surname, String email, int position) throws Exception {
		User temp = new User();
		temp.setUserID(id);
		temp.setUserName(name);
		temp.setUserSurname(surname);
		temp.setUserEmail(email);

		StringBuilder sb = new StringBuilder();
		int passwordLength = 6;
		String set = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		for (int i = 0; i < passwordLength; i++) {
			int k = new Random().nextInt(set.length() - 1);
			sb.append(set.charAt(k));
		}
		temp.setUserPassword(DataManager.getSHA(sb.toString()));

		temp.setUserPosition(position);
		insertData(id, "user", temp);

//		sendMail(id, "Registration", sb.toString());
		return true;
	}

	@Override
	public boolean deleteAccount(int id) throws Exception {
		spyMemCached.delete(id + ".user");
		return true;
	}

	@Override
	public boolean insertLog(String type, String message) throws Exception {
		throw new UnsupportedOperationException("MemcachedManager: insertLog not supported yet.");
	}

	@Override
	public boolean checkLog() throws Exception {
		throw new UnsupportedOperationException("MemcachedManager: checkLog not supported yet.");
	}

	@Override
	public User sendMail(int id, String subject, String password) throws Exception {
		User temp;

		if ((temp = (User) spyMemCached.get(id + ".user")) != null) {
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
		return temp;
	}

	@Override
	public boolean insertData(int key, String type, Object obj) {
		int time = 60;

		spyMemCached.set(key + "." + type.toLowerCase(), time, obj);

		return true;
	}
}
