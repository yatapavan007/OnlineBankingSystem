<%@ page contentType="text/html;charset=UTF-8" %>
<%
    // Invalidate session
    session.invalidate();
    response.sendRedirect("login.jsp");
%>
