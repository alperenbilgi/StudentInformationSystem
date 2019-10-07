package tr.edu.sis.Controller;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class DataManager {

	public static final String[] Position = {"Student", "Lecturer", "Admin"};
	public static final String[] Exam = {"Midterm", "Final"};

	private static DataOperator instance = null;

	private DataManager() {
	}

	public static DataOperator getInstance(int selection) throws Exception {
//		DataOperator cacheOperator = new MemcachedManager();
		instance = new Controller();
//		((Controller) instance).setPrimaryOperator(cacheOperator);

		switch (selection) {
			case 1:
				DataOperator dbOperator = new DBManager();
				((Controller) instance).setSecondaryOperator(dbOperator);
				break;
			case 2:
				DataOperator fileManager = new FileManager();
				((Controller) instance).setSecondaryOperator(fileManager);
				break;
		}

		return instance;
	}

	public static String getSHA(String input) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		BigInteger number = new BigInteger(1, md.digest(input.getBytes(StandardCharsets.UTF_8)));
		StringBuilder hexString = new StringBuilder(number.toString(16));

		while (hexString.length() < 32) {
			hexString.insert(0, '0');
		}

		return hexString.toString();
	}
}
