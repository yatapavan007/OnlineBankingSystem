package com.bankingsystem;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullName = request.getParameter("fullname");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        try {
            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO bank_users (username, password, full_name, email, phone, balance) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);	
            ps.setString(2, password);
            ps.setString(3, fullName);
            ps.setString(4, email);
            ps.setString(5, phone);
            ps.setDouble(6, 1000); // initial balance

            int rows = ps.executeUpdate();
            if (rows > 0) {
                response.sendRedirect("login.jsp");
            } else {
                response.getWriter().println("Registration failed. Please try again.");
            }

            con.close();
        } catch (SQLIntegrityConstraintViolationException e) {
            response.getWriter().println("Username already exists.");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }
}
