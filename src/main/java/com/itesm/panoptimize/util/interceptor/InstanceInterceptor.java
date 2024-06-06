package com.itesm.panoptimize.util.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itesm.panoptimize.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class InstanceInterceptor implements HandlerInterceptor {

    private final UserService userService;

    public InstanceInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String firebaseId = authentication.getName();
            String instanceId = userService.getInstanceIdFromFirebaseId(firebaseId);

            request.setAttribute("instanceId", instanceId);
        }

        return true;
    }
}
