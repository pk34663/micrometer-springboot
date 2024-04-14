package org.example;

import io.micrometer.core.instrument.MeterRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class CounterImpl {
    @Around("@annotation(c)")
    public Object Counter(ProceedingJoinPoint joinPoint,Counter c) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        GreetingController gc = (GreetingController)joinPoint.getTarget();

        System.out.println("classname:" + c.classname());
        gc.registry.counter(c.classname()).increment();
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println(
                String.format("Method %s execution time: %d ms",
                        joinPoint.getSignature(), executionTime));

        return result;
    }
}
