package com.rollingstone.cloud.messaging.aws;

import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.rollingstone.command.GenericCommandResult;
import com.rollingstone.command.interfaces.CommandQueueNameResolver;
import com.rollingstone.command.interfaces.GenericCommand;
import com.rollingstone.command.interfaces.GenericCommandDispatcher;

@Component
public class SQSCommandDispatcher implements GenericCommandDispatcher {

	private final SQSQueueSender sqsQueueSender;
	
	private final CommandQueueNameResolver commandQueueNameResolver;
	
	public SQSCommandDispatcher(SQSQueueSender sqsQueueSender, CommandQueueNameResolver commandQueueNameResolver) {
		this.sqsQueueSender = sqsQueueSender;
		this.commandQueueNameResolver = commandQueueNameResolver;
	}

	@Override
	public Future<GenericCommandResult> dispatch(GenericCommand command) {
		String type = command.getHeader().getCommandType();
		
		String queue;
		
		if (commandQueueNameResolver == null || commandQueueNameResolver.resolve(type) == null) {
			queue = new DefaultQueueNameResolver().resolve(type);
		} else {
			queue = this.commandQueueNameResolver.resolve(type);
		}
		
		return new AsyncResult<>(this.sqsQueueSender.send(queue, command));
	}
	
}
