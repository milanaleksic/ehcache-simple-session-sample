package net.milanaleksic.test.ehcachesimplesessionsample.service.impl;

import net.milanaleksic.test.ehcachesimplesessionsample.service.CacheService;
import net.milanaleksic.test.ehcachesimplesessionsample.service.SessionService;
import net.milanaleksic.test.ehcachesimplesessionsample.service.value.SessionInformation;
import net.milanaleksic.test.ehcachesimplesessionsample.service.value.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.*;
import java.util.UUID;

public class SessionServiceImpl implements SessionService {

    private static final Logger log = LoggerFactory.getLogger(SessionServiceImpl.class);

    private CacheService cacheService;

    private String illegalAccessViewName;

    private int expirationInSeconds;
    private String logOutUri;

    public void setLogOutUri(String logOutUri) {
        this.logOutUri = logOutUri;
    }

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public void setIllegalAccessViewName(String illegalAccessViewName) {
        this.illegalAccessViewName = illegalAccessViewName;
    }

    public void setExpirationInSeconds(int expirationInSeconds) {
        this.expirationInSeconds = expirationInSeconds;
    }

    @Override
    public SessionInformation checkLoggedIn(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        String authToken = getAuthorizationTokenFromRequest(servletRequest);
        if (authToken == null)
            return null;
        SessionInformation sessionInformation = checkIfAuthTokenIsValid(authToken);
        if (null == sessionInformation) {
            logOut(servletRequest, servletResponse);
            return null;
        }
        if (logOutUri != null && servletRequest.getRequestURI().endsWith(logOutUri)) {
            log.info("Log Out detected, no session refresh will be executed");
        } else {
            createOrRefreshSessionInformation(authToken, sessionInformation, servletResponse);
        }
        return sessionInformation;
    }

    private void createOrRefreshSessionInformation(String authToken, SessionInformation sessionInformation, HttpServletResponse servletResponse) {
        createOrRefreshSessionInformationInCache(authToken, sessionInformation);
        createOrRefreshSessionInformationInCookie(authToken, servletResponse);
    }

    private void createOrRefreshSessionInformationInCache(String authToken, SessionInformation sessionInformation) {
        cacheService.put(createTokenCacheKey(authToken), sessionInformation, expirationInSeconds);
    }

    private void createOrRefreshSessionInformationInCookie(String authToken, HttpServletResponse servletResponse) {
        Cookie tokenCookie = new Cookie(COOKIE_NAME, authToken);
        tokenCookie.setMaxAge(expirationInSeconds);
        tokenCookie.setPath("/");
        servletResponse.addCookie(tokenCookie);
    }

    @Override
    public void logOut(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        String authToken = getAuthorizationTokenFromRequest(servletRequest);
        Cookie deletedCookie = new Cookie(COOKIE_NAME, "");
        deletedCookie.setMaxAge(0);
        servletResponse.addCookie(deletedCookie);
        cacheService.remove(createTokenCacheKey(authToken));
    }

    @Override
    public Object authorize(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = getHttpServletRequestArgumentFromJointPoint(joinPoint);
        if (request == null)
            throw new IllegalArgumentException(String.format("Method %s does not have HttpServletRequest argument, can't be defended", joinPoint.getSignature()));
        HttpServletResponse response = getHttpServletResponseArgumentFromJointPoint(joinPoint);
        if (response == null)
            throw new IllegalArgumentException(String.format("Method %s does not have HttpServletResponse argument, can't be defended", joinPoint.getSignature()));
        String authToken = getAuthorizationTokenFromRequest(request);
        if (authToken == null)
            return new ModelAndView(illegalAccessViewName);
        if (null == checkLoggedIn(request, response))
            return new ModelAndView(illegalAccessViewName);
        return joinPoint.proceed();
    }

    private HttpServletResponse getHttpServletResponseArgumentFromJointPoint(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof HttpServletResponse)
                return (HttpServletResponse) arg;
        }
        return null;
    }

    private String getAuthorizationTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return null;
        for (Cookie cookie : cookies) {
            if (COOKIE_NAME.equals(cookie.getName()))
                return cookie.getValue();
        }
        return null;
    }

    private HttpServletRequest getHttpServletRequestArgumentFromJointPoint(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest)
                return (HttpServletRequest) arg;
        }
        return null;
    }

    private SessionInformation checkIfAuthTokenIsValid(String token) {
        Object recordedSessionInformation = cacheService.get(createTokenCacheKey(token));
        if (null == recordedSessionInformation)
            return null;
        return (SessionInformation) recordedSessionInformation;
    }

    private String createTokenCacheKey(String token) {
        return "SecurityToken," + token;
    }

    private void recordLogIn(User user, HttpServletResponse servletResponse) {
        String token = UUID.randomUUID().toString();
        SessionInformation sessionInformation = new SessionInformation();
        sessionInformation.setUser(user);
        createOrRefreshSessionInformation(token, sessionInformation, servletResponse);
    }

    @Override
    public void login(HttpServletResponse servletResponse, String username, String password) {
        if (username == null || password == null)
            throw new RuntimeException("Unrecognized username/password combination");
        User user = null;
        if ("admin".equals(username) && "admin".equals(password)) {
            user = new User(username, password);
        }
        if (null == user)
            throw new RuntimeException("Unrecognized username/password combination");
        if (!username.equals(user.getUsername()) || !password.equals(user.getPassword()))
            throw new RuntimeException("Unrecognized username/password combination");
        recordLogIn(user, servletResponse);
    }

}
