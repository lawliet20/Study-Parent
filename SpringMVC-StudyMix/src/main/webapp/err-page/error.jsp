<%@ page language="java" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>My JSP 'error.jsp' starting page</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <!--
        <link rel="stylesheet" type="text/css" href="styles.css">
        -->

</head>

<body>
<h1>所有的演示例子</h1>

<h3>[url=./dao.do?id=1]Dao正常错误[/url]</h3>

<h3>[url=./dao.do?id=10]Dao参数错误[/url]</h3>

<h3>[url=./dao.do?id=]Dao未知错误[/url]</h3>


<h3>[url=./service.do?id=1]Service正常错误[/url]</h3>

<h3>[url=./service.do?id=10]Service参数错误[/url]</h3>

<h3>[url=./service.do?id=]Service未知错误[/url]</h3>


<h3>[url=./controller.do?id=1]Controller正常错误[/url]</h3>

<h3>[url=./controller.do?id=10]Controller参数错误[/url]</h3>

<h3>[url=./controller.do?id=]Controller未知错误[/url]</h3>


<h3>[url=./404.do?id=1]404错误[/url]</h3>
</body>
</html>
