package com.turksat.servlet;

import com.google.gson.Gson;
import com.turksat.dblib.DBManager;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class checkLogin extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			PrintWriter out = resp.getWriter();
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			out.print(new Gson().toJson(new DBManager().checkLogin(Integer.parseInt(req.getParameter("id")), req.getParameter("password"))));
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
