package com.rollingstone.dispatcher;

import java.util.concurrent.Future;

import org.springframework.stereotype.Component;

import com.rollingstone.command.GenericCommandResult;
import com.rollingstone.command.interfaces.GenericCommand;
import com.rollingstone.command.interfaces.GenericCommandBus;
import com.rollingstone.command.interfaces.GenericCommandDispatcher;

@Component
public class DispatchingCommandBus implements GenericCommandBus {

	private final GenericCommandDispatcher genericCommandDispatcher;
	
	public DispatchingCommandBus(GenericCommandDispatcher genericCommandDispatcher) {
		this.genericCommandDispatcher = genericCommandDispatcher;
	}

	@Override
	public <T extends GenericCommand> Future<GenericCommandResult> send(T command) {
		return this.genericCommandDispatcher.dispatch(command);
	}
	
}
