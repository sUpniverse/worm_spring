<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>loginForm.jsp</h1>

<c:url var="loginUrl" value="/login" />
<form action="${loginUrl}" method="post">
	<c:if test="${param.error != null}">        
		<p>
			Invalid username and password. <br/>						
		</p>		
	</c:if>
	<c:if test="${param.logout != null}">       
		<p>
			You have been logged out.
		</p>
	</c:if>
 <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
 ID : <input type="text" id="username" name="username"/		>
 PW : <input type="password" id="password" name="password"/>
 <input type="submit" value="Login">
</form>

</body>
</html>