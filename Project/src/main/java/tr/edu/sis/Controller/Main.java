package tr.edu.sis.Controller;

import java.util.Scanner;
import tr.edu.sis.soap.Data;
import tr.edu.sis.soap.SoapService;
import tr.edu.sis.soap.SoapService_Service;

public class Main {

	private static final String ANSI_RED = "\u001B[31m";
	private static final String ANSI_GREEN = "\u001B[32m";
	private static final String ANSI_YELLOW = "\u001B[33m";
	private static final String ANSI_BLUE = "\u001B[34m";
	private static final String ANSI_PURPLE = "\u001B[35m";
	private static final String ANSI_CYAN = "\u001B[36m";
	private static final String ANSI_WHITE = "\u001B[37m";

	public static void main(String[] args) throws Exception {
		DataOperator manager = null;
		tr.edu.sis.soap.User currentUser = null;
		Scanner scanner;
		String input, regex, id, password;
		boolean flag = true;
		SoapService_Service soapService = new SoapService_Service();
		SoapService soap = soapService.getSoapServicePort();

		Data data1 = soap.checkLogin(3007, "pass");
		if (data1.getStatus() == 0) {
			System.out.println("Başarılı");
		} else {
			System.out.println("Başarısız");
		}

		Data data2 = soap.checkLogin(3007, "Deneme123");
		if (data2.getStatus() == 0) {
			System.out.println("Başarılı");
		} else {
			System.out.println("Başarısız");
		}

		Data data3 = soap.checkLogin(3010, "Deneme123");
		if (data3.getStatus() == 0) {
			System.out.println("Başarılı");
		} else {
			System.out.println("Başarısız");
		}

		System.out.println(Main.ANSI_WHITE + "Welcome");

		while (true) {
			System.out.print(ANSI_GREEN + "Type (1) for database, (2) for file: ");
			scanner = new Scanner(System.in);
			input = scanner.nextLine();
			regex = "[1-2]";
			if (input.matches(regex)) {
				manager = DataManager.getInstance(Integer.parseInt(input));
				break;
			}
			System.out.println(ANSI_RED + "Invalid Input!");
		}

		while (flag) {
			if (currentUser == null) {
				System.out.print(ANSI_GREEN + "Type (1) for login: ");
			} else {
				System.out.print(ANSI_GREEN + "Type (2) for changing password, (3) for logout: ");
			}
			scanner = new Scanner(System.in);
			input = scanner.nextLine();
			regex = "[1-3]";
			if (input.matches(regex)) {
				switch (Integer.parseInt(input)) {
					case 1:
						if (currentUser == null) {
							System.out.println(ANSI_RED + "Login Panel:");
							while (true) {
								System.out.print(ANSI_CYAN + "- ID: ");
								scanner = new Scanner(System.in);
								id = scanner.nextLine();
								regex = "[1-9][0-9][0-9][0-9]";
								if (id.matches(regex)) {
									break;
								}
								System.out.println(ANSI_RED + "ID Must Be In Number-Number-Number-Number Format!");
							}
							System.out.print(ANSI_CYAN + "- Password: ");
							scanner = new Scanner(System.in);
							password = scanner.nextLine();

							Data data = soap.checkLogin(Integer.parseInt(id), password);

							if (data.getStatus() == 0) {
								currentUser = (tr.edu.sis.soap.User) data.getObject();
							} else {
								currentUser = null;
							}

							if (currentUser != null) {
								System.out.println("Login Succesfull!");
								System.out.println("Welcome "
										+ currentUser.getUserName().toUpperCase() + " "
										+ currentUser.getUserSurname().toUpperCase() + " ["
										+ DataManager.Position[currentUser.getUserPosition()].toUpperCase() + "]");
							} else {
								System.out.println("Login Failed!");
							}
						}
						break;
					case 2:
						if (currentUser != null) {
							System.out.println(ANSI_RED + "Change Password Panel:");
							System.out.print(ANSI_CYAN + "- New Password: ");
							scanner = new Scanner(System.in);
							password = scanner.nextLine();

							if (manager.changePassword(currentUser.getUserID(), password)) {
								System.out.println("Password Changed!");
							} else {
								System.out.println("Password Change Failed!");
							}
							break;
						}
					case 3:
						if (currentUser != null) {
							currentUser = null;
							System.out.println("Logged Out!");
							break;
						}
				}
			} else {
				System.out.println(ANSI_RED + "Invalid Input!");
			}
		}
	}
}
