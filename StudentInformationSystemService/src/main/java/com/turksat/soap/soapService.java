package com.turksat.soap;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.turksat.dblib.DBManager;
import com.turksat.dblib.model.Data;
import com.turksat.dblib.model.User;
import java.net.InetSocketAddress;
import java.util.UUID;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import net.spy.memcached.MemcachedClient;

@WebService(serviceName = "soapService")
public class soapService {

	Checker check = new Checker();
	Data data = new Data();

	@WebMethod(operationName = "checkLogin")
	public Data checkLogin(@WebParam(name = "id") int id, @WebParam(name = "password") String password) {
		try {
			MemcachedClient spyMemCached = new MemcachedClient(new InetSocketAddress("localhost", 11211));
			if (check.loginCheck(id, password)) {
				User temp = new User();
				temp.setUserID(id);
				temp.setUserPassword(password);
				temp.setUUID(UUID.randomUUID().toString());

				ConnectionFactory factory = new ConnectionFactory();
				factory.setHost("localhost");
				try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
					byte[] dataToSent = temp.getBytes();
					channel.queueDeclare("login", false, false, false, null);
					channel.basicPublish("", "login", MessageProperties.PERSISTENT_TEXT_PLAIN, dataToSent);

					Data dataTemp = null;
					for (int i = 0; i < 3; i++) {
						if (i == 0) {
							Thread.sleep(100);
						} else {
							Thread.sleep(5000);
						}

						if ((dataTemp = (Data) spyMemCached.get(temp.getUUID() + ".login")) != null) {
							break;
						}
					}
					return dataTemp;
				}
			} else {
				data.setStatus(1);
				return data;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@WebMethod(operationName = "register")
	public Data register(@WebParam(name = "id") int id,
			@WebParam(name = "name") String name,
			@WebParam(name = "surname") String surname,
			@WebParam(name = "email") String email,
			@WebParam(name = "position") int position
	) {
		try {
			if (check.registerCheck(id, name, email)) {
				return new DBManager().register(id, name, surname, email, position);
			} else {
				data.setStatus(1);
				return data;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@WebMethod(operationName = "changePassword")
	public Data changePassword(@WebParam(name = "id") int id,
			@WebParam(name = "password") String password
	) {
		try {
			if (check.changePasswordCheck(id, password)) {
				return new DBManager().changePassword(id, password);
			} else {
				data.setStatus(1);
				return data;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@WebMethod(operationName = "deleteAccount")
	public Data deleteAccount(@WebParam(name = "id") int id
	) {
		try {
			if (check.deleteAccountCheck(id)) {
				return new DBManager().deleteAccount(id);
			} else {
				data.setStatus(1);
				return data;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@WebMethod(operationName = "courseRegistration")
	public Data courseRegistration(@WebParam(name = "lecture_id") int lecture_id,
			@WebParam(name = "student_id") int student_id
	) {
		try {
			if (check.courseRegistrationCheck(student_id)) {
				return new DBManager().courseRegistration(lecture_id, student_id);
			} else {
				data.setStatus(1);
				return data;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@WebMethod(operationName = "listLectures")
	public Data listLectures(@WebParam(name = "id") int id
	) {
		try {
			if (check.listLecturesCheck(id)) {
				return new DBManager().listLectures(id);
			} else {
				data.setStatus(1);
				return data;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@WebMethod(operationName = "listGrades")
	public Data listGrades(@WebParam(name = "id") int id
	) {
		try {
			if (check.listGradesCheck(id)) {
				return new DBManager().listGrades(id);
			} else {
				data.setStatus(1);
				return data;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@WebMethod(operationName = "assignGrade")
	public Data assignGrade(@WebParam(name = "lecture_id") int lecture_id,
			@WebParam(name = "student_id") int student_id,
			@WebParam(name = "type") int type,
			@WebParam(name = "grade") int grade
	) {
		try {
			return new DBManager().assignGrade(lecture_id, student_id, type, grade);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
