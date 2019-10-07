package com.turksat.servlet;

import com.google.gson.Gson;
import com.turksat.dblib.DBManager;
import com.turksat.dblib.model.Data;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class register extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		try {
			out.print(new Gson().toJson(new DBManager().register(Integer.parseInt(req.getParameter("id")), req.getParameter("name"), req.getParameter("surname"), req.getParameter("email"), Integer.parseInt(req.getParameter("position")))));
		} catch (Exception e) {
			Data data = new Data();
			data.setStatus(1);
			out.print(new Gson().toJson(data));
			e.printStackTrace();
		} finally {
			out.flush();
		}
	}
}
