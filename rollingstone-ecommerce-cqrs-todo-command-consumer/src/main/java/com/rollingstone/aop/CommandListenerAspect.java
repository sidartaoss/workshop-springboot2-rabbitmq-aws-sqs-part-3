package com.rollingstone.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;

@Aspect
@Component
public class CommandListenerAspect {

	private static final Logger LOG = LoggerFactory.getLogger(CommandListenerAspect.class);
	
	private Counter genericAPICallCounter = Metrics.counter("com.rollingstone.generic.listener.counter");
	
	private Counter createTodoCounter = Metrics.counter("com.rollingstone.service.todoService.saveTodo");
	
	@Before("execution(public * com.rollingstone.*Listener.*(..))")
	public void logBeforeCommandMethodCall(JoinPoint pjp) throws Throwable {
		LOG.info(":::: AOP Method that would be called in all cases: {}", pjp);
		this.genericAPICallCounter.increment();
	}
	
	@Before("execution(public * com.rollingstone.service.*Service.saveTodo*(..))")
	public void logBeforeCommandMethodSaveTodoCall(JoinPoint pjp) throws Throwable {
		LOG.info(":::: AOP Method that would be called in saveTodo use case: {}", pjp);
		this.createTodoCounter.increment();
	}	
}
