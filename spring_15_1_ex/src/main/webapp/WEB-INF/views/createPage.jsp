<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
createPage.jsp
<br />
<% String conPage = request.getContextPath(); %>

	 <form action="<%=conPage%>/student/create">
	 이름 : <input type="text" name ="name" value="${student.name}"> <br />
	 아이디 : <input type="text" name="id" value="${student.id}" ><br />
	 <input type="submit" value="전송" > <br />
	 </form>

</body>
</html>