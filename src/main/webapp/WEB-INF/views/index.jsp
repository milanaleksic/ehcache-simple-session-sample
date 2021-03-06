<%@ include file="/WEB-INF/views/includes.jsp" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
    <html lang="en" xml:lang="en">
        <head>
        <title>ehcache-simple-session-sample Test Login</title>
        <meta charset="utf-8" />
        <link rel="stylesheet" href="/css/core.css">
    </head>
    <body>
        <div class="container">
            <h4>ehcache-simple-session-sample Test Login</h4>
            <div>Only admin/admin accepted</div>
            <c:if test="${error != null}">
                <div class="errorMessage">
                    ${error}
                </div>
            </c:if>
            <form:form method="post" action="login">
                <table class="loginTable">
                    <tr>
                        <td><form:label path="username">Username</form:label></td>
                        <td><form:input path="username" /></td>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td><form:label path="password">Password</form:label></td>
                        <td><form:password path="password" /></td>
                        <td><input id="dologin" type="submit" value="Login"/></td>
                    </tr>
                </table>
            </form:form>
        </div>
    </body>
</html>
