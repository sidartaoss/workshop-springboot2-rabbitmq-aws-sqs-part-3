package com.rollingstone.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;

import com.rollingstone.command.GenericCommandType;
import com.rollingstone.command.TodoCommand;
import com.rollingstone.domain.Todo;
import com.rollingstone.service.TodoService;

@Service
public class AWSSQSQueueListener {

	private static final Logger LOG = LoggerFactory.getLogger(AWSSQSQueueListener.class);

	private TodoService todoService;

	public AWSSQSQueueListener(TodoService todoService) {
		this.todoService = todoService;
	}

	@SqsListener(value = "${sqs.todo.queue}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
	public void handleTodoCreateOrUpdateMessage(TodoCommand todoCommand) {
		LOG.info("We received the TodoCommand Message from AWS SQS");

		String type = todoCommand.getHeader().getCommandType();

		Todo todo = todoCommand.getTodo();

		LOG.info("The Todo We Received is: " + todo.toString());

		if (type.equalsIgnoreCase(GenericCommandType.CREATE_TODO.toString())) {
			this.todoService.saveTodo(todo);
			LOG.info("Created a new Todo: {}", todo.toString());
		} else if (type.equalsIgnoreCase(GenericCommandType.UPDATE_TODO.toString())) {
			this.todoService.saveTodo(todo);
			LOG.info("Updated an existing Todo: {}", todo.toString());
		} else if (type.equalsIgnoreCase(GenericCommandType.DELETE_TODO.toString())) {
			this.todoService.deleteTodoByID(todo.getId());
			LOG.info("Deleted Todo with ID: {}", todo.getId());
		}
	}
}
