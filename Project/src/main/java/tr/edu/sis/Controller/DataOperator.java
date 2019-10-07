package tr.edu.sis.Controller;

import java.util.List;
import tr.edu.sis.Model.User;

public interface DataOperator {

	/**
	 * Create connection to the database or file(s)
	 *
	 * @return TRUE if successfully connected to database / files)
	 * @throws Exception
	 */
	public boolean connect() throws Exception;

	/**
	 * Close connection to the database
	 *
	 * @throws Exception
	 */
	public void closeConnection() throws Exception;

	/**
	 * Check user informations. If confirmed, set user informations to currentUser parameter
	 *
	 * @param id User ID
	 * @param password User Password
	 * @return
	 * @throws Exception
	 */
	public User checkLogin(int id, String password) throws Exception;

	/**
	 * Change currentUser's password
	 *
	 * @param id currentUser's ID
	 * @param newPassword currentUser's newPassword
	 * @return
	 * @throws Exception
	 */
	public boolean changePassword(int id, String newPassword) throws Exception;

	/**
	 * Insert student and lecture IDs to related table
	 *
	 * @param lecture_id Lecture ID
	 * @param student_id Student ID
	 * @return
	 * @throws Exception
	 */
	public boolean courseRegistration(int lecture_id, int student_id) throws Exception;

	/**
	 * List lectures taken by related user
	 *
	 * @param id Student ID
	 * @return
	 * @throws Exception
	 */
	public List listLectures(int id) throws Exception;

	/**
	 * List grades of lectures taken by related user
	 *
	 * @param id Student ID
	 * @return
	 * @throws Exception
	 */
	public List listGrades(int id) throws Exception;

	/**
	 * Assign a grade to student
	 *
	 * @param lecture_id Lecture ID
	 * @param student_id Student ID
	 * @param type Exam Type (Midterm or Final etc.)
	 * @param grade Grade
	 * @return
	 * @throws Exception
	 */
	public boolean assignGrade(int lecture_id, int student_id, int type, int grade) throws Exception;

	/**
	 * New member registration
	 *
	 * @param id User ID
	 * @param name User Name
	 * @param surname User Surname
	 * @param email User Email
	 * @param position User's Position (Student, Lecturer or Admin)
	 * @return
	 * @throws Exception
	 */
	public boolean register(int id, String name, String surname, String email, int position) throws Exception;

	/**
	 * Delete an account
	 *
	 * @param id User ID to be deleted
	 * @return
	 * @throws Exception
	 */
	public boolean deleteAccount(int id) throws Exception;

	/**
	 * Inserts log to related database table or log.txt
	 *
	 * @param type Log Type (INFO or ERROR)
	 * @param message Log Record
	 * @return
	 * @throws Exception
	 */
	public boolean insertLog(String type, String message) throws Exception;

	/**
	 * Check if TEST log is inserted. If it is inserted delete it
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean checkLog() throws Exception;

	/**
	 * Send mail to corresponding user
	 *
	 * @return
	 */
	public User sendMail(int id, String subject, String password) throws Exception;

	/**
	 * Insert data to cache
	 *
	 * @param key
	 * @param type Table name (User, LectureStudent, LectureGrade)
	 * @param obj
	 * @return
	 */
	public boolean insertData(int key, String type, Object obj);
}
