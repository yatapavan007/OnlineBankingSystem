package com.bankingsystem;

import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/TransactionServlet")
public class TransactionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String username = session.getAttribute("username").toString();
        String type = request.getParameter("type"); // DEPOSIT or WITHDRAWAL
        String amountStr = request.getParameter("amount");

        if (type == null || amountStr == null || amountStr.trim().isEmpty()) {
            response.sendRedirect("deposit_withdraw.jsp?error=Missing transaction details");
            return;
        }

        Connection con = null;
        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                response.sendRedirect("deposit_withdraw.jsp?error=Amount must be greater than 0");
                return;
            }

            con = DBConnection.getConnection();
            con.setAutoCommit(false); // start transaction

            // 1. Get user_id from username
            int userId = 0;
            try (PreparedStatement psUser = con.prepareStatement("SELECT user_id FROM bank_users WHERE username = ?")) {
                psUser.setString(1, username);
                try (ResultSet rsUser = psUser.executeQuery()) {
                    if (rsUser.next()) {
                        userId = rsUser.getInt("user_id");
                    } else {
                        response.sendRedirect("deposit_withdraw.jsp?error=User not found");
                        return;
                    }
                }
            }

            if (type.equals("WITHDRAWAL")) {
                // 2. Check balance
                try (PreparedStatement psCheck = con.prepareStatement("SELECT balance FROM bank_users WHERE user_id = ?")) {
                    psCheck.setInt(1, userId);
                    try (ResultSet rs = psCheck.executeQuery()) {
                        if (rs.next()) {
                            double balance = rs.getDouble("balance");
                            if (balance < amount) {
                                response.sendRedirect("deposit_withdraw.jsp?error=Insufficient balance");
                                return;
                            }
                        }
                    }
                }

                // 3. Deduct balance
                try (PreparedStatement psWithdraw = con.prepareStatement("UPDATE bank_users SET balance = balance - ? WHERE user_id = ?")) {
                    psWithdraw.setDouble(1, amount);
                    psWithdraw.setInt(2, userId);
                    psWithdraw.executeUpdate();
                }
            } else {
                // Deposit balance
                try (PreparedStatement psDeposit = con.prepareStatement("UPDATE bank_users SET balance = balance + ? WHERE user_id = ?")) {
                    psDeposit.setDouble(1, amount);
                    psDeposit.setInt(2, userId);
                    psDeposit.executeUpdate();
                }
            }

            // 4. Insert transaction record
            try (PreparedStatement psTxn = con.prepareStatement(
                    "INSERT INTO bank_transactions (user_id, txn_type, amount, description, remarks, txn_date) VALUES (?, ?, ?, ?, ?, SYSDATE)")) {
                psTxn.setInt(1, userId);
                psTxn.setString(2, type);
                psTxn.setDouble(3, amount);
                String desc = type + " of ₹" + amount;
                psTxn.setString(4, desc);
                psTxn.setString(5, desc);
                psTxn.executeUpdate();
            }

            // 5. Update session balance with updated value
            try (PreparedStatement psBalance = con.prepareStatement("SELECT balance FROM bank_users WHERE user_id = ?")) {
                psBalance.setInt(1, userId);
                try (ResultSet rsBalance = psBalance.executeQuery()) {
                    if (rsBalance.next()) {
                        double updatedBalance = rsBalance.getDouble("balance");
                        session.setAttribute("balance", updatedBalance);
                    }
                }
            }

            con.commit();

            response.sendRedirect("deposit_withdraw.jsp?msg=" + type + " successful");

        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            response.sendRedirect("deposit_withdraw.jsp?error=Something went wrong");
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
