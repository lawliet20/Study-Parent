<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/common/common.jspf"%>
<html>
<head>
    <title>登录</title>
    <style>.error{color:red;}</style>
</head>
<body>
<div class="error">${error}</div>
<form action="${BASE_PATH}/login" method="post">
    用户名：<input type="text" name="username"><br/>
    密码：<input type="password" name="password"><br/>
    <input type="submit" value="登录">
</form>

</body>
</html>