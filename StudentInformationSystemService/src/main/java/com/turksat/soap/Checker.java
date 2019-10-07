package com.turksat.soap;

public class Checker {

	final String id_regex = "[1-9][0-9][0-9][0-9]";
	final String password_regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
	final String email_regex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

	public boolean loginCheck(int id, String password) {
		if (password instanceof String && password.matches(password_regex)) {
			return true;
		}

		return false;
	}

	public boolean registerCheck(int id, String password, String email) {
		if (Integer.toString(id).matches(id_regex) && password instanceof String && password.matches(password_regex) && email.matches(email_regex)) {
			return true;
		}

		return false;
	}

	public boolean changePasswordCheck(int id, String password) {
		if (Integer.toString(id).matches(id_regex) && password instanceof String && password.matches(password_regex)) {
			return true;
		}

		return false;
	}

	public boolean deleteAccountCheck(int id) {
		if (Integer.toString(id).matches(id_regex)) {
			return true;
		}

		return false;
	}

	public boolean courseRegistrationCheck(int id) {
		if (Integer.toString(id).matches(id_regex)) {
			return true;
		}

		return false;
	}

	public boolean listLecturesCheck(int id) {
		if (Integer.toString(id).matches(id_regex)) {
			return true;
		}

		return false;
	}

	public boolean listGradesCheck(int id) {
		if (Integer.toString(id).matches(id_regex)) {
			return true;
		}

		return false;
	}
}
