<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<c:set var="BASE" value="${pageContext.request.contextPath}" ></c:set>
<script type="text/javascript" src="${BASE}/resources/jquery/jquery-3.0.0.js"></script>
<head>
    <title>登录成功页面</title>
    <script type="text/javascript">
        function showDialog(id,name,url,roles,permissions,type){
            if("add" == type){

            }else if("update" == type){
                $("#detailTab").show();
                $("#id").val(id)
                $("#name").val(name)
                $("#url").val(url)
                $("#roles").val(roles)
                $("#permissions").val(permissions)
            }else if("del" == type){

            }
        }

        function formSubmit(){
            $("#urlPerForm").attr("action","${BASE}/urlFilter/update");
            $("#urlPerForm").submit();
        }

    </script>
</head>
<body>
登录成功页面:欢迎你<shiro:principal/>
<shiro:hasAnyRoles name="admin">
    <shiro:principal/>拥有角色admin
</shiro:hasAnyRoles>
<form id="urlPerForm" name="urlPerForm" method="post" action="">
    <table border="1">
        <tr>
            <td>ID</td>
            <td>姓名</td>
            <td>url</td>
            <td>角色</td>
            <td>权限</td>
            <td>操作</td>
        </tr>
        <c:forEach items="${perList}" var="per">
            <tr>
                <td>${per.id}</td>
                <td>${per.name}</td>
                <td>${per.url}</td>
                <td>${per.roles}</td>
                <td>${per.permissions}</td>
                <td>
                    <a href="#" onclick="showDialog('add')">新增</a>
                    <a href="#" onclick="showDialog('${per.id}','${per.name}','${per.url}','${per.roles}','${per.permissions}','update')" >更新</a>
                    <a href="#" onclick="showDialog('del')">删除</a>
                </td>
            </tr>
        </c:forEach>
    </table>

    <table id="detailTab" border="1" style="display: none">
        <tr><td>id:</td><td><input id="id" name="id"></td></tr>
        <tr><td>name:</td><td><input id="name" name="name"></td></tr>
        <tr><td>url:</td><td><input id="url" name="url"></td></tr>
        <tr><td>roles:</td><td><input id="roles" name="roles"></td></tr>
        <tr><td>permissions:</td><td><input id="permissions" name="permissions"></td></tr>
        <tr><input type="button" onclick="formSubmit()" value="提交"></tr>
    </table>
</form>
</body>
</html>