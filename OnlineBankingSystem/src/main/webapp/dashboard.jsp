<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%
   
    if (session == null || session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String username = session.getAttribute("username").toString();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Dashboard - PhonePe Style</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            background: #f8f9fa;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .container {
            padding-top: 20px;
            padding-bottom: 40px;
        }
        .welcome-text {
            font-weight: 600;
            font-size: 1.5rem;
            margin-bottom: 1rem;
            color: #212529;
            text-align: center;
        }
        .grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(90px, 1fr));
            gap: 1rem;
            max-width: 480px;
            margin: 0 auto;
        }
        .card-button {
            background: white;
            border-radius: 16px;
            padding: 15px 0;
            text-align: center;
            box-shadow: 0 4px 8px rgb(0 0 0 / 0.1);
            cursor: pointer;
            transition: box-shadow 0.2s ease-in-out, transform 0.15s ease-in-out;
            user-select: none;
            color: #212529;
            text-decoration: none;
            display: flex;
            flex-direction: column;
            align-items: center;
        }
        .card-button:hover {
            box-shadow: 0 6px 14px rgb(0 0 0 / 0.15);
            transform: translateY(-3px);
            text-decoration: none;
            color: #0d6efd;
        }
        .card-icon {
            font-size: 2.2rem;
            margin-bottom: 8px;
            color: #0d6efd;
        }
        .card-label {
            font-size: 0.85rem;
            font-weight: 500;
            line-height: 1.2;
        }
        @media (max-width: 400px) {
            .grid {
                max-width: 100%;
                gap: 0.75rem;
            }
            .card-button {
                padding: 12px 0;
                border-radius: 12px;
            }
            .card-icon {
                font-size: 1.8rem;
                margin-bottom: 6px;
            }
            .card-label {
                font-size: 0.75rem;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="welcome-text">Welcome, <%= username %>!</div>
        <div class="grid">
            <a href="dashboard.jsp" class="card-button" title="View Profile">
                <i class="bi bi-person-circle card-icon"></i>
                <span class="card-label">Profile</span>
            </a>
            <a href="send-money.jsp" class="card-button" title="Send Money">
                <i class="bi bi-currency-exchange card-icon"></i>
                <span class="card-label">Send</span>
            </a>
            <a href="transaction_history.jsp" class="card-button" title="Transaction History">
                <i class="bi bi-clock-history card-icon"></i>
                <span class="card-label">History</span>
            </a>
            <a href="deposit_withdraw.jsp" class="card-button" title="Deposit Money">
                <i class="bi bi-wallet2 card-icon"></i>
                <span class="card-label">Deposit</span>
            </a>
            <a href="deposit_withdraw.jsp" class="card-button" title="Withdraw Money">
                <i class="bi bi-cash-coin card-icon"></i>
                <span class="card-label">Withdraw</span>
            </a>
            <a href="change-password.jsp" class="card-button" title="Change Password">
                <i class="bi bi-key card-icon"></i>
                <span class="card-label">Password</span>
            </a>
            <a href="logout.jsp" class="card-button" title="Logout">
                <i class="bi bi-box-arrow-right card-icon"></i>
                <span class="card-label">Logout</span>
            </a>
        </div>
    </div>
</body>
</html>
