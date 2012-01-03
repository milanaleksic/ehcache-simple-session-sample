package net.milanaleksic.test.ehcachesimplesessionsample.service;

import net.milanaleksic.test.ehcachesimplesessionsample.service.value.SessionInformation;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface SessionService {

    String COOKIE_NAME = "MAN-BACKEND-AUTH-COOKIE";

    SessionInformation checkLoggedIn(HttpServletRequest servletRequest, HttpServletResponse servletResponse);

    boolean getFailedLoginInformation(HttpServletRequest servletRequest);

    void saveLoginFailedInformation(HttpServletRequest servletRequest);

    Object authorize(ProceedingJoinPoint joinPoint) throws Throwable;

    void login(HttpServletResponse servletResponse, String username, String password);

    void logOut(HttpServletRequest servletRequest, HttpServletResponse servletResponse);

    void setIllegalAccessViewName(String illegalAccessViewName);

}
