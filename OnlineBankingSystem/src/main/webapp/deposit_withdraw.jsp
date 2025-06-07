<%@ page contentType="text/html;charset=UTF-8" language="java" import="javax.servlet.http.*" session="true" %>

<%
   
    if (session == null || session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String msg = request.getParameter("msg");
    String error = request.getParameter("error");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Deposit / Withdraw</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
</head>
<body>
<div class="container mt-5">
    <h3 class="mb-4">Deposit / Withdrawal</h3>

    <% if (msg != null) { %>
        <div class="alert alert-success"><%= msg %></div>
    <% } %>

    <% if (error != null) { %>
        <div class="alert alert-danger"><%= error %></div>
    <% } %>

    <form action="TransactionServlet" method="post" class="border p-4 rounded shadow-sm bg-light">
        <div class="mb-3">
            <label for="type" class="form-label">Transaction Type</label>
            <select name="type" id="type" class="form-select" required>
                <option value="DEPOSIT">Deposit</option>
                <option value="WITHDRAWAL">Withdraw</option>
            </select>
        </div>
        <div class="mb-3">
            <label for="amount" class="form-label">Amount</label>
            <input type="number" step="0.01" min="1" name="amount" id="amount" class="form-control" required />
        </div>
        <button type="submit" class="btn btn-primary">Submit</button>
        <a href="dashboard.jsp" class="btn btn-secondary">Back</a>
    </form>

    <hr/>

    <h5>Your Current Balance: <%= session.getAttribute("balance") != null ? session.getAttribute("balance") : "0.00" %></h5>
</div>
</body>
</html>
