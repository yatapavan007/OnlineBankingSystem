package com.bankingsystem;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/ChangePasswordServlet")
public class ChangePasswordServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String username = session.getAttribute("username").toString();
        String oldPwd = request.getParameter("old_password");
        String newPwd = request.getParameter("new_password");
        String confirmPwd = request.getParameter("confirm_password");

        String msg = "";

        if (!newPwd.equals(confirmPwd)) {
            msg = "New passwords do not match.";
        } else {
            try (Connection con = DBConnection.getConnection()) {
                // Check old password
                String checkSql = "SELECT password FROM bank_users WHERE username = ?";
                PreparedStatement checkStmt = con.prepareStatement(checkSql);
                checkStmt.setString(1, username);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    String dbPwd = rs.getString("password");
                    if (!dbPwd.equals(oldPwd)) {
                        msg = "Old password is incorrect.";
                    } else {
                        // Update password
                        String updateSql = "UPDATE bank_users SET password = ? WHERE username = ?";
                        PreparedStatement updateStmt = con.prepareStatement(updateSql);
                        updateStmt.setString(1, newPwd);
                        updateStmt.setString(2, username);
                        int result = updateStmt.executeUpdate();
                        msg = result > 0 ? "Password updated successfully." : "Failed to update password.";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                msg = "Error: " + e.getMessage();
            }
        }

        request.setAttribute("msg", msg);
        RequestDispatcher rd = request.getRequestDispatcher("change-password.jsp");
        rd.forward(request, response);
    }
}
