package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)"  )
    public void autoFillPointCut(){}
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("开始进行填充。。。");
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0)    return ;
        Object arg = args[0];
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        AutoFill annotation = method.getAnnotation(AutoFill.class);
        if(annotation.value() == OperationType.INSERT){
            Method method1 = arg.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
            Method method2 = arg.getClass().getMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
            Method method3 = arg.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method method4 = arg.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
            method1.invoke(arg,LocalDateTime.now());
            method2.invoke(arg, BaseContext.getCurrentId());
            method3.invoke(arg,LocalDateTime.now());
            method4.invoke(arg, BaseContext.getCurrentId());
        }
        else{
            Method method3 = arg.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method method4 = arg.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
            method3.invoke(arg,LocalDateTime.now());
            method4.invoke(arg, BaseContext.getCurrentId());
        }


    }
}
