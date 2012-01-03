package net.milanaleksic.test.ehcachesimplesessionsample.controller;

import net.milanaleksic.test.ehcachesimplesessionsample.controller.value.LoginForm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public interface PageController {

    @RequestMapping("/*")
    public ModelAndView catchAll(HttpServletRequest servletRequest, HttpServletResponse servletResponse);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
                              @ModelAttribute("loginForm") LoginForm loginForm);

    @RequestMapping(value = "/logOut", method = RequestMethod.GET)
    public ModelAndView logOut(HttpServletRequest servletRequest, HttpServletResponse servletResponse);

    @RequestMapping(value = "/console", method = RequestMethod.GET)
    public ModelAndView console(HttpServletRequest servletRequest, HttpServletResponse servletResponse);

}
