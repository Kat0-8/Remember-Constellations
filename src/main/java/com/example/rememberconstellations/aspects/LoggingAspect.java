package com.example.rememberconstellations.aspects;

import java.util.Arrays;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("within(com.example.rememberconstellations.services..*) || "
            + "within(com.example.rememberconstellations.controllers..*)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (logger.isInfoEnabled()) {
            logger.info("Entering: {} with arguments {}",
                    joinPoint.getSignature().toShortString(),
                    Arrays.toString(joinPoint.getArgs()));
        }
        Object result = joinPoint.proceed();
        if (logger.isInfoEnabled()) {
            logger.info("Exiting: {} with result = {}",
                    joinPoint.getSignature().toShortString(),
                    result);
        }
        return result;
    }

    @AfterThrowing(pointcut = "within(com.example.rememberconstellations.services..*)", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        logger.error("Exception in {} with arguments {}: {}",
                joinPoint.getSignature().toShortString(),
                Arrays.toString(joinPoint.getArgs()),
                exception.getMessage(),
                exception);
    }
}
