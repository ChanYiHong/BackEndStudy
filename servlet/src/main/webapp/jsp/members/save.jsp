<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="hcy.servlet.domain.member.Member" %>
<%@ page import="hcy.servlet.domain.member.MemberRepository" %>
<%
    // request, response는 그냥 사용 가능!!
    // JSP 도 결국 서블릿으로 바뀌는데, (우리눈에는 보이지 않음. 사실 그래서 서비스 로직이 그대로 호출됨).
    MemberRepository memberRepository = MemberRepository.getInstance();
    System.out.println("MemberSaveServlet.service");
    String username = request.getParameter("username");
    int age = Integer.parseInt(request.getParameter("age"));

    Member member = new Member(username, age);
    memberRepository.save(member);
%>
<html>
<head>
    <title>Title</title>
</head>
<body>
성공
<ul>
    <li>id=<%=member.getId()%></li>
    <li>username=<%=member.getUsername()%></li>
    <li>age=<%=member.getAge()%></li>
</ul>
</body>
</html>