package com.rollingstone.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;

@Aspect
@Component
public class RestControllerAspect {

	private static final Logger LOG = LoggerFactory.getLogger(RestControllerAspect.class);
	
	private Counter salesOrderCreatedCounter = Metrics.counter("com.rollingstone.salesorder.created");
	
	@Before("execution(public * com.rollingstone.spring.controller.*Controller.*(..))")
	public void generalAllMethodAspect() {
		LOG.info("All method calls invoke this general aspect method");
	}
	
	@AfterReturning("execution(public * com.rollingstone.spring.controller.*Controller.createSalesOrder*(..))")
	public void getCalledOnSalesOrderSave() {
		LOG.info("This Aspect method is called only on Sales Order Save");
		salesOrderCreatedCounter.increment();
	}
}
