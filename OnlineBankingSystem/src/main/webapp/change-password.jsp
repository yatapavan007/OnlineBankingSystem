<%@ page contentType="text/html;charset=UTF-8" %>
<%
    if (session == null || session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    String msg = (String) request.getAttribute("msg");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Change Password</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
</head>
<body class="bg-light">
<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-5">
            <div class="card shadow">
                <div class="card-header text-center">
                    <h4>Change Password</h4>
                </div>
                <div class="card-body">
                    <% if (msg != null) { %>
                        <div class="alert alert-info"><%= msg %></div>
                    <% } %>
                    <form method="post" action="ChangePasswordServlet">
                        <div class="mb-3">
                            <label>Old Password</label>
                            <input type="password" name="old_password" class="form-control" required />
                        </div>
                        <div class="mb-3">
                            <label>New Password</label>
                            <input type="password" name="new_password" class="form-control" required />
                        </div>
                        <div class="mb-3">
                            <label>Confirm New Password</label>
                            <input type="password" name="confirm_password" class="form-control" required />
                        </div>
                        <div class="d-grid">
                            <button type="submit" class="btn btn-primary">Update Password</button>
                        </div>
                    </form>
                </div>
                <div class="card-footer text-center">
                    <a href="dashboard.jsp">Back to Dashboard</a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
