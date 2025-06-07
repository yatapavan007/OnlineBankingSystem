<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.sql.*, javax.servlet.http.*" %>

<%
    HttpSession currentSession = request.getSession(false);
    if (currentSession == null || currentSession.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String username = currentSession.getAttribute("username").toString();
    int userId = -1;
    double balance = 0.0;
    Connection con = null;
%>

<!DOCTYPE html>
<html>
<head>
    <title>Transaction History</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
</head>
<body>
<div class="container mt-5">
    <h3 class="mb-4">Transaction History</h3>

    <%
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "system", "12345");

            // Get user_id and balance from bank_users
            PreparedStatement psUser = con.prepareStatement("SELECT user_id, balance FROM bank_users WHERE username = ?");
            psUser.setString(1, username);
            ResultSet rsUser = psUser.executeQuery();
            if (rsUser.next()) {
                userId = rsUser.getInt("user_id");
                balance = rsUser.getDouble("balance");
            }
            rsUser.close();
            psUser.close();
    %>

    <div class="alert alert-success fs-5">
        Current Balance: ₹ <strong><%= String.format("%.2f", balance) %></strong>
    </div>

    <table class="table table-bordered table-striped">
        <thead class="table-dark">
            <tr>
                <th>Date</th>
                <th>Type</th>
                <th>Amount</th>
                <th>Description</th>
            </tr>
        </thead>
        <tbody>
        <%
            if (userId != -1) {
                PreparedStatement ps = con.prepareStatement(
                    "SELECT txn_date, txn_type, amount, description FROM bank_transactions WHERE user_id = ? ORDER BY txn_date DESC"
                );
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();
                boolean hasTransactions = false;
                while (rs.next()) {
                    hasTransactions = true;
        %>
            <tr>
                <td><%= rs.getTimestamp("txn_date") %></td>
                <td><%= rs.getString("txn_type") %></td>
                <td>₹<%= String.format("%.2f", rs.getDouble("amount")) %></td>
                <td><%= rs.getString("description") %></td>
            </tr>
        <%
                }
                if (!hasTransactions) {
        %>
            <tr><td colspan="4" class="text-center">No transactions found.</td></tr>
        <%
                }
                rs.close();
                ps.close();
            } else {
        %>
            <tr><td colspan="4" class="text-danger">User ID not found.</td></tr>
        <%
            }
            con.close();
        } catch (Exception e) {
        %>
            <tr>
                <td colspan="4" class="text-danger">
                    Error fetching transaction history:<br>
                    <pre><%= e %></pre>
                </td>
            </tr>
        <%
        }
        %>
        </tbody>
    </table>

    <a href="dashboard.jsp" class="btn btn-primary">Back to Dashboard</a>
</div>
</body>
</html>
