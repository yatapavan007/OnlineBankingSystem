<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="javax.servlet.http.*,java.sql.*" %>
<%

    if (session == null || session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Send Money</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container mt-5 col-md-6">
    <div class="card shadow p-4">
        <h3 class="mb-4 text-primary text-center">Send Money</h3>

        <%
            String msg = (String) request.getAttribute("msg");
            if (msg != null) {
        %>
            <div class="alert alert-info"><%= msg %></div>
        <%
            }
        %>

        <form action="SendMoneyServlet" method="post">
            <div class="mb-3">
                <label class="form-label">Receiver Username</label>
                <input type="text" class="form-control" name="receiver" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Amount</label>
                <input type="number" class="form-control" name="amount" step="0.01" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Remark</label>
                <input type="text" class="form-control" name="remark">
            </div>
            <button type="submit" class="btn btn-success w-100">Send</button>
            <a href="dashboard.jsp" class="btn btn-secondary w-100 mt-2">Back to Dashboard</a>
        </form>
    </div>
</div>
</body>
</html>
