<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Bank Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet" />
</head>
<body class="bg-dark">
<div class="container mt-5  ">
    <div class="row justify-content-center">
        <div class="col-md-4">
            <div class="card shadow-lg">
                <div class="card-header text-center ">
                    <h4>Online Banking Login</h4>
                </div>
                <div class="card-body">
                    <% String error = (String) request.getAttribute("errorMessage"); %>
                    <% if (error != null) { %>
                        <div class="alert alert-danger"><%= error %></div>
                    <% } %>
                    <form method="post" action="LoginServlet">
                        <div class="mb-3">
                            <label>Username</label>
                            <input type="text" name="username" class="form-control" required />
                        </div>
                        <div class="mb-3">
                            <label>Password</label>
                            <input type="password" name="password" class="form-control" required />
                        </div>
                        <div class="d-grid">
                            <button type="submit" class="btn btn-dark">Login</button>
                        </div>
                    </form>
                </div>
                <div class="card-footer text-center">
                    <small>Don't have an account? <a href="register.jsp">Register</a></small>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
