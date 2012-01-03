package net.milanaleksic.test.ehcachesimplesessionsample.controller.impl;

import net.milanaleksic.test.ehcachesimplesessionsample.BackEndAuthorizationNeeded;
import net.milanaleksic.test.ehcachesimplesessionsample.controller.PageController;
import net.milanaleksic.test.ehcachesimplesessionsample.controller.value.LoginForm;
import net.milanaleksic.test.ehcachesimplesessionsample.service.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PageControllerImpl implements PageController {

    private static final Logger log = LoggerFactory.getLogger(PageControllerImpl.class);

    @Autowired
    private SessionService sessionService;

    public ModelAndView catchAll(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        if (null == sessionService.checkLoggedIn(servletRequest, servletResponse)) {
            ModelAndView mav = new ModelAndView("index");
            mav.getModel().put("command", new LoginForm());
            return mav;
        } else
            return new ModelAndView("backend");
    }

    public ModelAndView login(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
                              @ModelAttribute("loginForm") LoginForm loginForm) {
        try {
            if (log.isInfoEnabled())
                log.info("Login attempt for user: {}", loginForm.getUsername());
            sessionService.login(servletResponse, loginForm.getUsername(), loginForm.getPassword());
            return new ModelAndView("redirect:/");
        } catch (Exception e) {
            log.warn("Login failed for user: " + loginForm.getUsername(), e);
            sessionService.saveLoginFailedInformation(servletRequest);
            ModelAndView catchAll = catchAll(servletRequest, servletResponse);
            catchAll.getModel().put("error", "Login Failed: " + e.getMessage());
            return catchAll;
        }
    }

    @Override
    @BackEndAuthorizationNeeded
    public ModelAndView logOut(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        sessionService.logOut(servletRequest, servletResponse);
        return new ModelAndView("redirect:/");
    }

    @Override
    @BackEndAuthorizationNeeded
    public ModelAndView console(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        return new ModelAndView("console");
    }

}