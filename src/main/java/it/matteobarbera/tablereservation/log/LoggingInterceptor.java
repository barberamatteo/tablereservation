package it.matteobarbera.tablereservation.log;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


@Component
public class LoggingInterceptor implements HandlerInterceptor {
    private final static String SUBMITTED_CALL_TO_ENDPOINT = "Submitted call to {}";
    private final static String RETURNED_WITH_CODE = "Call to {} returned with code {}";
    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) {
        log.atInfo().log(SUBMITTED_CALL_TO_ENDPOINT, request.getRequestURI());
        return true;
    }


    @Override
    public void postHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler,
            ModelAndView modelAndView
    ) {
        log.atInfo().log(RETURNED_WITH_CODE, request.getRequestURI(), response.getStatus());
    }
}
