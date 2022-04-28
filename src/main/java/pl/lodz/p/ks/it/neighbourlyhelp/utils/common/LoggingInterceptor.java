package pl.lodz.p.ks.it.neighbourlyhelp.utils.common;

import lombok.extern.java.Log;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;

@Component
@Log
public class LoggingInterceptor implements HandlerInterceptor {

    public String getCaller() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "Guest";
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String className = ((HandlerMethod) handler).getBeanType().getName();
        String methodName = ((HandlerMethod) handler).getMethod().getName();

        log.log(Level.INFO, "{0}.{1} is called by {2}",
                new Object[]{className, methodName, getCaller()});
        return true;
    }
}