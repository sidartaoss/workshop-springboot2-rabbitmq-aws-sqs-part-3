package com.rollingstone.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.rollingstone.service.event.TodoServiceEvent;

@Component
public class TodoEventListener {

	private static final Logger LOG = LoggerFactory.getLogger(TodoEventListener.class);

	@EventListener
	public void onApplicationEvent(TodoServiceEvent todoServiceEvent) {
		LOG.info("Received Todo Event Type: {} ", todoServiceEvent.getEventType());
		LOG.info("Received Todo from Todo Event: {} ", todoServiceEvent.getEventTodo().toString());

	}

}
