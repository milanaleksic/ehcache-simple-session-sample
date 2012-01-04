<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/includes.jsp" %>
<!DOCTYPE html>
<html lang="en" xml:lang="en">
    <head>
        <title>ehcache-simple-session-sample Console</title>
        <meta charset="utf-8" />
        <link rel="stylesheet" href="/css/core.css">
    </head>
    <body class="backend">
        <div class="header">
            <h3>ehcache-simple-session-sample Console</h3>
        </div>
        <div class="menu">
            <a href="/console">Main console</a>
            <a href="/logOut">Log out</a>
        </div>
        <div class="content">
            <c:if test="${info != null}">
                <div class="infoMessage">
                    ${info}
                </div>
            </c:if>
            <div>Great, you are logged in correctly!</div>
        </div>
        <div class="footer">&copy; 2011-2012 by Milan.Aleksic@gmail.com</div>
    </body>
</html>