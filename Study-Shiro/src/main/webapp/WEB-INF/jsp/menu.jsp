<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/common/common.jspf"%>
<html>
<head>
    <title>菜单栏</title>
    <meta charset="UTF-8">
    

    <script TYPE="TEXT/JAVASCRIPT">
        $(function () {
            //alert("$(BASE_PATH)");
        })
    </script>
</head>
<body>
<ul id="tt" class="easyui-tree">
    <li>
        <span>菜单栏</span>
        <ul>
            <c:forEach var="menu" items="${menus}">
                <li>
                    <span>我的菜单</span>
                    <ul>
                        <li><span><a href="${BASE_PATH}/${menu.url}">${menu.name}</a></span></li>
                    </ul>
                </li>
            </c:forEach>
        </ul>
    </li>
</ul>
</body>
</html>