package com.example.rememberconstellations.aspects;

import com.example.rememberconstellations.services.VisitsService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class VisitsAspect {

    private final VisitsService visitsService;

    public VisitsAspect(VisitsService visitsService) {
        this.visitsService = visitsService;
    }

    @Before("@within(org.springframework.web.bind.annotation.RestController) && "
            + "(bean(starsController) || bean(logsController) || bean(constellationsController))")
    public void incrementCount() {
        HttpServletRequest request =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String uri = request.getRequestURI();
        visitsService.incrementCount(uri);
    }
}
