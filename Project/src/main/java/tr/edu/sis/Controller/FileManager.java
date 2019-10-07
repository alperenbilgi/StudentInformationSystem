package tr.edu.sis.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import tr.edu.sis.Model.LectureGrade;
import tr.edu.sis.Model.LectureStudent;
import tr.edu.sis.Model.User;

public class FileManager implements DataOperator {

	private FileInputStream fis;
	private FileOutputStream fos;
	private XSSFWorkbook wbUser, wbLecture, wbLectureGrade, wbLectureStudent;
	private XSSFSheet sheetUser, sheetLecture, sheetLectureGrade, sheetLectureStudent;
	private Row row;
	private Cell cell;
	private final DataFormatter dataFormatter = new DataFormatter();

	private final PropertiesManager prop;
	private final String fileDirectory, logDirectory;
	private final String smtpHostKey, smtpSocketFactoryPortKey, smtpSocketFactoryClassKey, smtpAuthKey, smtpPortKey;
	private final String smtpHostValue, smtpSocketFactoryPortValue, smtpSocketFactoryClassValue, smtpAuthValue, smtpPortValue;
	private final int userIdColumn, userNameColumn, userSurnameColumn, userEmailColumn, userPasswordColumn, userPositionColumn;
	private final int lectureIdColumn, lectureNameColumn, lectureCreditColumn, lectureLecturerColumn;
	private final int lecturestudentLectureColumn, lecturestudentStudentColumn;
	private final int lecturegradeLectureColumn, lecturegradeStudentColumn, lecturegradeTypeColumn, lecturegradeGradeColumn;

	public FileManager() {

		//1. reflectionla variable listeleme nasıl yapılır
		//2. reflectionla annotaionları okucak
		//3. annotation işleme
		AbstractApplicationContext context = new AnnotationConfigApplicationContext(PropertiesManager.class);
		prop = (PropertiesManager) context.getBean("dataSource");

		fileDirectory = prop.getFileDirectoryLinux();
		logDirectory = prop.getLogDirectoryLinux();

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

		userIdColumn = Integer.parseInt(prop.getUserId());
		userNameColumn = Integer.parseInt(prop.getUserName());
		userSurnameColumn = Integer.parseInt(prop.getUsersurname());
		userEmailColumn = Integer.parseInt(prop.getUserEmail());
		userPasswordColumn = Integer.parseInt(prop.getUserPassword());
		userPositionColumn = Integer.parseInt(prop.getUserPosition());

		lectureIdColumn = Integer.parseInt(prop.getLectureId());
		lectureNameColumn = Integer.parseInt(prop.getLectureName());
		lectureCreditColumn = Integer.parseInt(prop.getLectureCredit());
		lectureLecturerColumn = Integer.parseInt(prop.getLectureLecturer());

		lecturestudentLectureColumn = Integer.parseInt(prop.getLecturestudentLecture());
		lecturestudentStudentColumn = Integer.parseInt(prop.getLecturestudentStudent());

		lecturegradeLectureColumn = Integer.parseInt(prop.getLecturegradeLecture());
		lecturegradeStudentColumn = Integer.parseInt(prop.getLecturegradeStudent());
		lecturegradeTypeColumn = Integer.parseInt(prop.getLecturegradeType());
		lecturegradeGradeColumn = Integer.parseInt(prop.getLecturegradeGrade());
	}

	@Override
	public boolean connect() throws Exception {
		fis = new FileInputStream(fileDirectory + ("User.xlsx"));
		wbUser = new XSSFWorkbook(fis);
		sheetUser = wbUser.getSheetAt(0);

		fis = new FileInputStream(fileDirectory + ("Lecture.xlsx"));
		wbLecture = new XSSFWorkbook(fis);
		sheetLecture = wbLecture.getSheetAt(0);

		fis = new FileInputStream(fileDirectory + ("LectureStudent.xlsx"));
		wbLectureStudent = new XSSFWorkbook(fis);
		sheetLectureStudent = wbLectureStudent.getSheetAt(0);

		fis = new FileInputStream(fileDirectory + ("LectureGrade.xlsx"));
		wbLectureGrade = new XSSFWorkbook(fis);
		sheetLectureGrade = wbLectureGrade.getSheetAt(0);

		return true;
	}

	@Override
	public void closeConnection() throws Exception {
		fis.close();
		fos.close();
	}

	@Override
	public User checkLogin(int id, String password) throws Exception {
		User currentUser = null;

		if (connect()) {
			for (int i = 1; i <= sheetUser.getLastRowNum(); i++) {
				if (dataFormatter.formatCellValue(sheetUser.getRow(i).getCell(userIdColumn)).equals(Integer.toString(id))
						&& dataFormatter.formatCellValue(sheetUser.getRow(i).getCell(userPasswordColumn)).equals(DataManager.getSHA(password))) {
					currentUser = new User();
					currentUser.setUserID(Integer.parseInt(dataFormatter.formatCellValue(sheetUser.getRow(i).getCell(userIdColumn))));
					currentUser.setUserName(dataFormatter.formatCellValue(sheetUser.getRow(i).getCell(userNameColumn)));
					currentUser.setUserSurname(dataFormatter.formatCellValue(sheetUser.getRow(i).getCell(userSurnameColumn)));
					currentUser.setUserEmail(dataFormatter.formatCellValue(sheetUser.getRow(i).getCell(userEmailColumn)));
					currentUser.setUserPassword(dataFormatter.formatCellValue(sheetUser.getRow(i).getCell(userPasswordColumn)));
					currentUser.setUserPosition(Integer.parseInt(dataFormatter.formatCellValue(sheetUser.getRow(i).getCell(userPositionColumn))));

					break;
				}
			}
		}

		return currentUser;
	}

	@Override
	public boolean changePassword(int id, String newPassword) throws Exception {
		if (connect()) {
			for (int i = 1; i <= sheetUser.getLastRowNum(); i++) {
				if (dataFormatter.formatCellValue(sheetUser.getRow(i).getCell(userIdColumn)).equals(Integer.toString(id))) {
					cell = sheetUser.getRow(i).getCell(userPasswordColumn);
					cell.setCellValue(DataManager.getSHA(newPassword));
					fos = new FileOutputStream(fileDirectory + ("User.xlsx"));
					wbUser.write(fos);

					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	@Override
	public boolean courseRegistration(int lecture_id, int student_id) throws Exception {
		if (connect()) {
			row = sheetLectureStudent.createRow(sheetLectureStudent.getLastRowNum() + 1);
			cell = row.createCell(lecturestudentLectureColumn);
			cell.setCellValue(lecture_id);
			cell = row.createCell(lecturestudentStudentColumn);
			cell.setCellValue(student_id);
			fos = new FileOutputStream(fileDirectory + "LectureStudent.xlsx");
			wbLectureStudent.write(fos);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List listLectures(int id) throws Exception {
		List<LectureStudent> list = null;

		if (connect()) {
			list = new ArrayList<>();

			for (int i = 1; i <= sheetLectureStudent.getLastRowNum(); i++) {
				if (dataFormatter.formatCellValue(sheetLectureStudent.getRow(i).getCell(lecturestudentStudentColumn)).equals(Integer.toString(id))) {
					LectureStudent lectureStudent = new LectureStudent();

					lectureStudent.setLectureID(Integer.parseInt(dataFormatter.formatCellValue(sheetLectureStudent.getRow(i).getCell(lecturestudentLectureColumn))));

					for (int j = 1; j <= sheetLecture.getLastRowNum(); j++) {
						if (dataFormatter.formatCellValue(sheetLecture.getRow(j).getCell(lectureIdColumn)).equals(Integer.toString(lectureStudent.getLectureID()))) {
							lectureStudent.setLectureName(dataFormatter.formatCellValue(sheetLecture.getRow(j).getCell(lectureNameColumn)));
							lectureStudent.setLectureCredit(Integer.parseInt(dataFormatter.formatCellValue(sheetLecture.getRow(j).getCell(lectureCreditColumn))));
							lectureStudent.setUserID(Integer.parseInt(dataFormatter.formatCellValue(sheetLecture.getRow(j).getCell(lectureLecturerColumn))));
							break;
						}
					}

					for (int j = 1; j <= sheetUser.getLastRowNum(); j++) {
						if (dataFormatter.formatCellValue(sheetUser.getRow(j).getCell(userIdColumn)).equals(Integer.toString(lectureStudent.getUserID()))) {
							lectureStudent.setUserName(dataFormatter.formatCellValue(sheetUser.getRow(j).getCell(userNameColumn)));
							lectureStudent.setUserSurname(dataFormatter.formatCellValue(sheetUser.getRow(j).getCell(userSurnameColumn)));
							lectureStudent.setUserEmail(dataFormatter.formatCellValue(sheetUser.getRow(j).getCell(userEmailColumn)));
						} else if (dataFormatter.formatCellValue(sheetUser.getRow(j).getCell(userIdColumn)).equals(Integer.toString(id))) {
							User userTemp = new User();
							userTemp.setUserID(id);
							userTemp.setUserName(dataFormatter.formatCellValue(sheetUser.getRow(j).getCell(userNameColumn)));
							userTemp.setUserSurname(dataFormatter.formatCellValue(sheetUser.getRow(j).getCell(userSurnameColumn)));
							userTemp.setUserEmail(dataFormatter.formatCellValue(sheetUser.getRow(j).getCell(userEmailColumn)));

							lectureStudent.setStudent(userTemp);
						}
					}

					list.add(lectureStudent);
				}
			}
		}

//		for (int i = 0; i < list.size(); i++) {
//			System.out.println(
//					list.get(i).getLectureID() + " | "
//					+ list.get(i).getLectureName() + " | "
//					+ list.get(i).getLectureCredit() + " | "
//					+ list.get(i).getUserID() + " | "
//					+ list.get(i).getUserName() + " | "
//					+ list.get(i).getUserSurname() + " | "
//					+ list.get(i).getUserEmail() + " | "
//					+ list.get(i).getStudent().getUserID() + " | "
//					+ list.get(i).getStudent().getUserName() + " | "
//					+ list.get(i).getStudent().getUserSurname() + " | "
//					+ list.get(i).getStudent().getUserEmail()
//			);
//		}
		return list;
	}

	@Override
	public List listGrades(int id) throws Exception {
		List<LectureGrade> list = null;

		if (connect()) {
			list = new ArrayList<>();

			for (int i = 1; i <= sheetLectureGrade.getLastRowNum(); i++) {
				if (dataFormatter.formatCellValue(sheetLectureGrade.getRow(i).getCell(lecturegradeStudentColumn)).equals(Integer.toString(id))) {
					LectureGrade lectureGrade = new LectureGrade();

					lectureGrade.setLectureID(Integer.parseInt(dataFormatter.formatCellValue(sheetLectureGrade.getRow(i).getCell(lecturegradeLectureColumn))));

					for (int j = 1; j <= sheetLecture.getLastRowNum(); j++) {
						if (dataFormatter.formatCellValue(sheetLecture.getRow(j).getCell(lectureIdColumn)).equals(Integer.toString(lectureGrade.getLectureID()))) {
							lectureGrade.setLectureName(dataFormatter.formatCellValue(sheetLecture.getRow(j).getCell(lectureNameColumn)));
							lectureGrade.setLectureCredit(Integer.parseInt(dataFormatter.formatCellValue(sheetLecture.getRow(j).getCell(lectureCreditColumn))));
							lectureGrade.setUserID(Integer.parseInt(dataFormatter.formatCellValue(sheetLecture.getRow(j).getCell(lectureLecturerColumn))));
							break;
						}
					}

					for (int j = 1; j <= sheetUser.getLastRowNum(); j++) {
						if (dataFormatter.formatCellValue(sheetUser.getRow(j).getCell(userIdColumn)).equals(Integer.toString(lectureGrade.getUserID()))) {
							lectureGrade.setUserName(dataFormatter.formatCellValue(sheetUser.getRow(j).getCell(userNameColumn)));
							lectureGrade.setUserSurname(dataFormatter.formatCellValue(sheetUser.getRow(j).getCell(userSurnameColumn)));
							lectureGrade.setUserEmail(dataFormatter.formatCellValue(sheetUser.getRow(j).getCell(userEmailColumn)));
						} else if (dataFormatter.formatCellValue(sheetUser.getRow(j).getCell(userIdColumn)).equals(Integer.toString(id))) {
							User userTemp = new User();
							userTemp.setUserID(id);
							userTemp.setUserName(dataFormatter.formatCellValue(sheetUser.getRow(j).getCell(userNameColumn)));
							userTemp.setUserSurname(dataFormatter.formatCellValue(sheetUser.getRow(j).getCell(userSurnameColumn)));
							userTemp.setUserEmail(dataFormatter.formatCellValue(sheetUser.getRow(j).getCell(userEmailColumn)));

							lectureGrade.setStudent(userTemp);
						}
					}

					lectureGrade.setType(Integer.parseInt(dataFormatter.formatCellValue(sheetLectureGrade.getRow(i).getCell(lecturegradeTypeColumn))));
					lectureGrade.setGrade(Integer.parseInt(dataFormatter.formatCellValue(sheetLectureGrade.getRow(i).getCell(lecturegradeGradeColumn))));

					list.add(lectureGrade);
				}
			}
		}

//		for (int i = 0; i < list.size(); i++) {
//			System.out.println(
//					list.get(i).getLectureID() + " | "
//					+ list.get(i).getLectureName() + " | "
//					+ list.get(i).getLectureCredit() + " | "
//					+ list.get(i).getUserID() + " | "
//					+ list.get(i).getUserName() + " | "
//					+ list.get(i).getUserSurname() + " | "
//					+ list.get(i).getUserEmail() + " | "
//					+ list.get(i).getStudent().getUserID() + " | "
//					+ list.get(i).getStudent().getUserName() + " | "
//					+ list.get(i).getStudent().getUserSurname() + " | "
//					+ list.get(i).getStudent().getUserEmail() + " | "
//					+ list.get(i).getType() + " | "
//					+ list.get(i).getGrade()
//			);
//		}
		return list;
	}

	@Override
	public boolean assignGrade(int lecture_id, int student_id, int type, int grade) throws Exception {
		if (connect()) {
			row = sheetLectureGrade.createRow(sheetLectureGrade.getLastRowNum() + 1);
			cell = row.createCell(lecturegradeLectureColumn);
			cell.setCellValue(lecture_id);
			cell = row.createCell(lecturegradeStudentColumn);
			cell.setCellValue(student_id);
			cell = row.createCell(lecturegradeTypeColumn);
			cell.setCellValue(type);
			cell = row.createCell(lecturegradeGradeColumn);
			cell.setCellValue(grade);
			fos = new FileOutputStream(fileDirectory + "LectureGrade.xlsx");
			wbLectureGrade.write(fos);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean register(int id, String name, String surname, String email, int position) throws Exception {
		if (connect()) {
			row = sheetUser.createRow(sheetUser.getLastRowNum() + 1);
			cell = row.createCell(userIdColumn);
			cell.setCellValue(id);
			cell = row.createCell(userNameColumn);
			cell.setCellValue(name);
			cell = row.createCell(userSurnameColumn);
			cell.setCellValue(surname);
			cell = row.createCell(userEmailColumn);
			cell.setCellValue(email);
			cell = row.createCell(userPasswordColumn);

			StringBuilder sb = new StringBuilder();
			int passwordLength = 6;
			String set = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
			for (int i = 0; i < passwordLength; i++) {
				int k = new Random().nextInt(set.length() - 1);
				sb.append(set.charAt(k));
			}
			cell.setCellValue(DataManager.getSHA(sb.toString()));

			cell = row.createCell(userPositionColumn);
			cell.setCellValue(position);
			fos = new FileOutputStream(fileDirectory + "User.xlsx");
			wbUser.write(fos);

			sendMail(id, "Registration", sb.toString());

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean deleteAccount(int id) throws Exception {
		if (connect()) {
			for (int i = 1; i <= sheetUser.getLastRowNum(); i++) {
				if (dataFormatter.formatCellValue(sheetUser.getRow(i).getCell(userIdColumn)).equals(Integer.toString(id))) {
					row = sheetUser.getRow(i);
					sheetUser.removeRow(row);
					if (i != (sheetUser.getLastRowNum() + 1)) {
						sheetUser.shiftRows(i, sheetUser.getLastRowNum(), -1);
					}
					break;
				}
			}
			fos = new FileOutputStream(fileDirectory + "User.xlsx");
			wbUser.write(fos);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean insertLog(String type, String message) throws Exception {
		JsonObject jsonObject = Json.createObjectBuilder().
				add("Time", new java.sql.Time(new java.util.Date().getTime()).toString()).
				add("Date", new SimpleDateFormat("dd-MM-yyyy").format(new Date())).
				add("Type", type.toUpperCase(Locale.ENGLISH)).
				add("Message", message).
				build();

		Map<String, Boolean> config = new HashMap<>();
		config.put(JsonGenerator.PRETTY_PRINTING, true);

		try (FileWriter fw = new FileWriter(new File(logDirectory + "log.txt"), true)) {
			Json.createWriterFactory(config).createWriter(fw).writeObject(jsonObject);
		}

		return true;
	}

	@Override
	public boolean checkLog() throws Exception {
		return true;
	}

	@Override
	public User sendMail(int id, String subject, String password) throws Exception {
		User temp = null;

		if (connect()) {
			for (int i = 1; i <= sheetUser.getLastRowNum(); i++) {
				if (dataFormatter.formatCellValue(sheetUser.getRow(i).getCell(userIdColumn)).equals(Integer.toString(id))) {
					temp = new User();
					temp.setUserID(Integer.parseInt(dataFormatter.formatCellValue(sheetUser.getRow(i).getCell(userIdColumn))));
					temp.setUserName(dataFormatter.formatCellValue(sheetUser.getRow(i).getCell(userNameColumn)));
					temp.setUserSurname(dataFormatter.formatCellValue(sheetUser.getRow(i).getCell(userSurnameColumn)));
					temp.setUserEmail(dataFormatter.formatCellValue(sheetUser.getRow(i).getCell(userEmailColumn)));
					temp.setUserPassword(dataFormatter.formatCellValue(sheetUser.getRow(i).getCell(userPasswordColumn)));
					temp.setUserPosition(Integer.parseInt(dataFormatter.formatCellValue(sheetUser.getRow(i).getCell(userPositionColumn))));
					break;
				}
			}

			if (temp != null) {
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
		throw new UnsupportedOperationException("FileManager: insertData not supported yet.");
	}
}
