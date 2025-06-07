package com.bankingsystem;

import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/SendMoneyServlet")
public class SendMoneyServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String senderUsername = session.getAttribute("username").toString();
        String receiverUsername = request.getParameter("receiver");
        double amount = Double.parseDouble(request.getParameter("amount"));
        String remark = request.getParameter("remark");

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "12345");

            // Get sender
            pst = conn.prepareStatement("SELECT user_id, balance FROM bank_users WHERE username = ?");
            pst.setString(1, senderUsername);
            rs = pst.executeQuery();

            if (!rs.next()) {
                request.setAttribute("msg", "Sender not found.");
                request.getRequestDispatcher("send-money.jsp").forward(request, response);
                return;
            }

            int senderId = rs.getInt("user_id");
            double senderBalance = rs.getDouble("balance");

            if (amount <= 0 || amount > senderBalance) {
                request.setAttribute("msg", "Insufficient balance.");
                request.getRequestDispatcher("send-money.jsp").forward(request, response);
                return;
            }

            // Get receiver
            pst = conn.prepareStatement("SELECT user_id FROM bank_users WHERE username = ?");
            pst.setString(1, receiverUsername);
            rs = pst.executeQuery();

            if (!rs.next()) {
                request.setAttribute("msg", "Receiver not found.");
                request.getRequestDispatcher("send-money.jsp").forward(request, response);
                return;
            }

            int receiverId = rs.getInt("user_id");

            if (senderId == receiverId) {
                request.setAttribute("msg", "Cannot send money to yourself.");
                request.getRequestDispatcher("send-money.jsp").forward(request, response);
                return;
            }

            // Transaction block
            conn.setAutoCommit(false);

            // Update sender balance
            pst = conn.prepareStatement("UPDATE bank_users SET balance = balance - ? WHERE user_id = ?");
            pst.setDouble(1, amount);
            pst.setInt(2, senderId);
            pst.executeUpdate();

            // Update receiver balance
            pst = conn.prepareStatement("UPDATE bank_users SET balance = balance + ? WHERE user_id = ?");
            pst.setDouble(1, amount);
            pst.setInt(2, receiverId);
            pst.executeUpdate();

            // Log sender txn
            pst = conn.prepareStatement(
                "INSERT INTO bank_transactions (user_id, txn_type, amount, description, remarks, txn_date) " +
                "VALUES (?, 'SEND', ?, ?, ?, SYSDATE)");
            pst.setInt(1, senderId);
            pst.setDouble(2, amount);
            pst.setString(3, "Sent to " + receiverUsername);
            pst.setString(4, remark);
            pst.executeUpdate();

            // Log receiver txn
            pst = conn.prepareStatement(
                "INSERT INTO bank_transactions (user_id, txn_type, amount, description, remarks, txn_date) " +
                "VALUES (?, 'RECEIVE', ?, ?, ?, SYSDATE)");
            pst.setInt(1, receiverId);
            pst.setDouble(2, amount);
            pst.setString(3, "Received from " + senderUsername);
            pst.setString(4, remark);
            pst.executeUpdate();

            conn.commit();
            request.setAttribute("msg", "Money sent successfully!");
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            request.setAttribute("msg", "Transaction failed: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (conn != null) conn.close();
            } catch (Exception ex) { ex.printStackTrace(); }

            request.getRequestDispatcher("send-money.jsp").forward(request, response);
        }
    }
}
