<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 <%@ taglib uri="http://www.springframework.org/security/tags" prefix="s" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>login.jsp</h1>

<%-- <c:if test="${not empty pageContext.request.userPrincipal}">
<p> is Log-in </p>
</c:if>

<c:if test="${empty pageContext.request.userPrincipal}">
<p> is Log-out </p>
</c:if> --%>

<s:authorize access="hasRole('ROLE_USER')">
<p> is Log-in </p>
</s:authorize>

<s:authorize access="hasRole('ROLE_USER')">
<p> is Log-out </p>
</s:authorize>


<%-- USER ID : ${pageContext.request.userPrincipal.name} --%>
USER ID : <s:authentication property="name"/> 
<c:url var="logoutUrl" value="/logout"/>
<form action="${logoutUrl}" 	method="post">
<input type="submit" value="Log out" />
<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>
</body>
</html>