package com.bankingsystem;  // change this to your package

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/DepositServlet")
public class DepositServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String username = session.getAttribute("username").toString();
        String amountStr = request.getParameter("amount");

        try {
            double amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                response.sendRedirect("deposit.jsp?error=Enter a valid amount");
                return;
            }

            Connection con = DBConnection.getConnection();

            // Update balance
            String updateBalanceSQL = "UPDATE bank_users SET balance = balance + ? WHERE username = ?";
            PreparedStatement ps1 = con.prepareStatement(updateBalanceSQL);
            ps1.setDouble(1, amount);
            ps1.setString(2, username);
            int rows = ps1.executeUpdate();

            // Insert transaction record
            String insertTxnSQL = "INSERT INTO bank_transactions(username, type, amount, txn_date, remarks) VALUES (?, 'CREDIT', ?, SYSTIMESTAMP, 'Deposit')";
            PreparedStatement ps2 = con.prepareStatement(insertTxnSQL);
            ps2.setString(1, username);
            ps2.setDouble(2, amount);
            ps2.executeUpdate();

            ps1.close();
            ps2.close();
            con.close();

            if (rows > 0) {
                response.sendRedirect("dashboard.jsp?msg=Deposit Successful");
            } else {
                response.sendRedirect("deposit.jsp?error=Deposit Failed");
            }

        } catch (NumberFormatException e) {
            response.sendRedirect("deposit.jsp?error=Invalid amount");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("deposit.jsp?error=Internal error occurred");
        }
    }
}
