package tr.edu.sis.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("file:/home/alperen/NetBeansProjects/StudentInformationSystem/src/main/resources/Properties/config.properties")
public class PropertiesManager {

	@Value("${db.URL}")
	private String dbURL;

	@Value("${db.USER}")
	private String dbUSER;

	@Value("${db.PASS}")
	private String dbPASS;

	@Value("${file.directory.windows}")
	private String fileDirectoryWindows;

	@Value("${file.directory.linux}")
	private String fileDirectoryLinux;

	@Value("${log.directory.linux}")
	private String logDirectoryLinux;

	@Value("${smtp.host.key}")
	private String smtpHostKey;

	@Value("${smtp.socketFactory.port.key}")
	private String smtpSocketFactoryPortKey;

	@Value("${smtp.socketFactory.class.key}")
	private String smtpSocketFactoryClassKey;

	@Value("${smtp.auth.key}")
	private String smtpAuthKey;

	@Value("${smtp.port.key}")
	private String smtpPortKey;

	@Value("${smtp.host.value}")
	private String smtpHostValue;

	@Value("${smtp.socketFactory.port.value}")
	private String smtpSocketFactoryPortValue;

	@Value("${smtp.socketFactory.class.value}")
	private String smtpSocketFactoryClassValue;

	@Value("${smtp.auth.value}")
	private String smtpAuthValue;

	@Value("${smtp.port.value}")
	private String smtpPortValue;

	@Value("${user.id}")
	private String userId;

	/**
	 * Not user.name because it returns active user's name in Linux!
	 */
	@Value("${user.namee}")
	private String userName;

	@Value("${user.surname}")
	private String usersurname;

	@Value("${user.email}")
	private String userEmail;

	@Value("${user.password}")
	private String userPassword;

	@Value("${user.position}")
	private String userPosition;

	@Value("${lecture.id}")
	private String lectureId;

	@Value("${lecture.name}")
	private String lectureName;

	@Value("${lecture.credit}")
	private String lectureCredit;

	@Value("${lecture.lecturer}")
	private String lectureLecturer;

	@Value("${lecturestudent.lecture}")
	private String lecturestudentLecture;

	@Value("${lecturestudent.student}")
	private String lecturestudentStudent;

	@Value("${lecturegrade.lecture}")
	private String lecturegradeLecture;

	@Value("${lecturegrade.student}")
	private String lecturegradeStudent;

	@Value("${lecturegrade.type}")
	private String lecturegradeType;

	@Value("${lecturegrade.grade}")
	private String lecturegradeGrade;

	@Bean
	public PropertiesManager dataSource() {
		return this;
	}

	public String getDbURL() {
		return dbURL;
	}

	public String getDbUSER() {
		return dbUSER;
	}

	public String getDbPASS() {
		return dbPASS;
	}

	public String getFileDirectoryWindows() {
		return fileDirectoryWindows;
	}

	public String getFileDirectoryLinux() {
		return fileDirectoryLinux;
	}

	public String getLogDirectoryLinux() {
		return logDirectoryLinux;
	}

	public String getSmtpHostKey() {
		return smtpHostKey;
	}

	public String getSmtpSocketFactoryPortKey() {
		return smtpSocketFactoryPortKey;
	}

	public String getSmtpSocketFactoryClassKey() {
		return smtpSocketFactoryClassKey;
	}

	public String getSmtpAuthKey() {
		return smtpAuthKey;
	}

	public String getSmtpPortKey() {
		return smtpPortKey;
	}

	public String getSmtpHostValue() {
		return smtpHostValue;
	}

	public String getSmtpSocketFactoryPortValue() {
		return smtpSocketFactoryPortValue;
	}

	public String getSmtpSocketFactoryClassValue() {
		return smtpSocketFactoryClassValue;
	}

	public String getSmtpAuthValue() {
		return smtpAuthValue;
	}

	public String getSmtpPortValue() {
		return smtpPortValue;
	}

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getUsersurname() {
		return usersurname;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public String getUserPosition() {
		return userPosition;
	}

	public String getLectureId() {
		return lectureId;
	}

	public String getLectureName() {
		return lectureName;
	}

	public String getLectureCredit() {
		return lectureCredit;
	}

	public String getLectureLecturer() {
		return lectureLecturer;
	}

	public String getLecturestudentLecture() {
		return lecturestudentLecture;
	}

	public String getLecturestudentStudent() {
		return lecturestudentStudent;
	}

	public String getLecturegradeLecture() {
		return lecturegradeLecture;
	}

	public String getLecturegradeStudent() {
		return lecturegradeStudent;
	}

	public String getLecturegradeType() {
		return lecturegradeType;
	}

	public String getLecturegradeGrade() {
		return lecturegradeGrade;
	}
}
