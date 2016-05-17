<%@ page import="com.ssm.pay.utils.SsmAppConstants" %>
<%
    String ctx = request.getContextPath();
    String adminPath = SsmAppConstants.getAdminPath();
    String frontPath = SsmAppConstants.getFrontPath();
//    response.sendRedirect(ctx + adminPath + "/login/welcome");
    response.sendRedirect(ctx + adminPath);
%>