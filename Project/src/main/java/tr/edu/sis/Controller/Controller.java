package tr.edu.sis.Controller;

import java.util.List;
import tr.edu.sis.Model.User;

public class Controller implements DataOperator {

	private DataOperator primaryOperator;
	private DataOperator secondaryOperator;

	public void setPrimaryOperator(DataOperator primaryOperator) {
		this.primaryOperator = primaryOperator;
	}

	public void setSecondaryOperator(DataOperator secondaryOperator) {
		this.secondaryOperator = secondaryOperator;
	}

	@Override
	public boolean connect() throws Exception {
		throw new UnsupportedOperationException("Controller: connect not supported yet.");
	}

	@Override
	public void closeConnection() throws Exception {
		throw new UnsupportedOperationException("Controller: closeConnection not supported yet.");
	}

	@Override
	public User checkLogin(int id, String password) throws Exception {
		User temp;

		if ((temp = primaryOperator.checkLogin(id, password)) != null) {
		} else {
			temp = secondaryOperator.checkLogin(id, password);
			if (temp != null) {
				insertData(id, "user", temp);
			}
		}

		return temp;
	}

	@Override
	public boolean changePassword(int id, String newPassword) throws Exception {
		boolean temp;

		if (temp = primaryOperator.changePassword(id, newPassword)) {
			secondaryOperator.changePassword(id, newPassword);
		} else {
			temp = secondaryOperator.changePassword(id, newPassword);
		}

		return temp;
	}

	@Override
	public boolean courseRegistration(int lecture_id, int student_id) throws Exception {
		secondaryOperator.courseRegistration(lecture_id, student_id);
		return true;
	}

	@Override
	public List listLectures(int id) throws Exception {
		List temp;

		if ((temp = primaryOperator.listLectures(id)) != null) {
		} else {
			temp = secondaryOperator.listLectures(id);
			if (temp != null) {
				insertData(id, "lecturestudent", temp);
			}
		}

		return temp;
	}

	@Override
	public List listGrades(int id) throws Exception {
		List temp;

		if ((temp = primaryOperator.listGrades(id)) != null) {
		} else {
			temp = secondaryOperator.listGrades(id);
			if (temp != null) {
				insertData(id, "lecturegrade", temp);
			}
		}

		return temp;
	}

	@Override
	public boolean assignGrade(int lecture_id, int student_id, int exam, int grade) throws Exception {
		secondaryOperator.assignGrade(lecture_id, student_id, exam, grade);
		return true;
	}

	@Override
	public boolean register(int id, String name, String surname, String email, int position) throws Exception {
		if (secondaryOperator.register(id, name, surname, email, position)) {
			primaryOperator.register(id, name, surname, email, position);
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteAccount(int id) throws Exception {
		primaryOperator.deleteAccount(id);
		secondaryOperator.deleteAccount(id);
		return true;
	}

	@Override
	public boolean insertLog(String type, String message) throws Exception {
//		secondaryOperator.insertLog(type, message);
		return true;
	}

	@Override
	public boolean checkLog() throws Exception {
		secondaryOperator.checkLog();
		return true;
	}

	@Override
	public User sendMail(int id, String subject, String password) throws Exception {
		User temp;

		if ((temp = primaryOperator.sendMail(id, subject, password)) != null) {
		} else {
			temp = secondaryOperator.sendMail(id, subject, password);
			if (temp != null) {
				insertData(id, "user", temp);
			}
		}

		return temp;
	}

	@Override
	public boolean insertData(int key, String type, Object obj) {
		primaryOperator.insertData(key, type, obj);
		return true;
	}
}
