package ir.mrmoein.quezapplication.util;

import ir.mrmoein.quezapplication.controller.admin.AdminController;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger("LoggingAspect");


    @Before("execution(* ir.mrmoein.quezapplication.controller.*.*.*(..))")
    public void logBeforeMethods(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getPrincipal().equals("anonymousUser")) {
            UserDetails user = (UserDetails) authentication.getPrincipal();
            logger.info("{} , user :{} ({}) start this methode {}", joinPoint.getTarget().getClass().getSimpleName(), user.getUsername(), user.getAuthorities(), joinPoint.getSignature().getName());
        }else {
            logger.info("{}: start this methode {}", joinPoint.getTarget().getClass().getSimpleName(), joinPoint.getSignature().getName());
        }
    }

}
