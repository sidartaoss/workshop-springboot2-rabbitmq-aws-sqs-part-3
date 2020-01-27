package com.rollingstone.api;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rollingstone.command.GenericCommandHeader;
import com.rollingstone.command.GenericCommandType;
import com.rollingstone.command.TodoCommand;
import com.rollingstone.command.interfaces.GenericCommandBus;
import com.rollingstone.domain.RSResponse;
import com.rollingstone.domain.Todo;
import com.rollingstone.service.TodoService;
import com.rollingstone.service.event.TodoServiceEvent;

@RestController
@RequestMapping("/rsecommerce/cqrs/todo/api")
public class TodoController extends AbstractController {

	private static final Logger LOG = LoggerFactory.getLogger(TodoController.class);
	
	private static final String SCHEMA_VERSION = "1.0";

	private final GenericCommandBus genericCommandBus;
	
	private final TodoService todoService;
	
	private static Validator validator;
	
	public TodoController(GenericCommandBus genericCommandBus, TodoService todoService) {
		this.genericCommandBus = genericCommandBus;
		this.todoService = todoService;
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
	
	@GetMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<Todo> getTodos() {
		return this.todoService.getAllTodos();
	}
	
	@GetMapping("/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Todo getTodoById(@PathVariable("id") Long id) {
		return this.todoService.getTodo(id);
	}
	
	@PostMapping
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public RSResponse<Todo> createTodo(@RequestBody Todo todo) {
		RSResponse<Todo> rsResponse = new RSResponse<>();
		
		LOG.info("Received request to create Todo");
		
		Set<ConstraintViolation<Todo>> constraintViolations = validator.validate(todo);
		
		String errorMessage = this.buildErrorMessage(constraintViolations);
		
		if (!errorMessage.isEmpty()) {
			LOG.error("Error When Creating Todo: " + errorMessage);
			rsResponse.setErrorMessage("Error When Creating Todo: " + errorMessage);
			return rsResponse;
		} else {
			TodoCommand todoCommand = new TodoCommand();
			todoCommand.setTodo(todo);
			todoCommand.setId(UUID.randomUUID());
			
			GenericCommandHeader header = new GenericCommandHeader(GenericCommandType.CREATE_TODO.toString(), SCHEMA_VERSION, new Timestamp(System.currentTimeMillis()));
			
			todoCommand.setHeader(header);
			
			this.genericCommandBus.send(todoCommand);
			
			this.eventPublisher.publishEvent(new TodoServiceEvent(this, todo, "TodoCreatedEvent"));
			
			rsResponse.setMessage("Todo sent to AWS for creation");
			rsResponse.setPayload(todo);
			
			return rsResponse;
			
		}
	}
	
	@PutMapping("/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public RSResponse<Todo> updateTodo(@RequestBody Todo todo) {
		RSResponse<Todo> rsResponse = new RSResponse<>();
		
		LOG.info("Received request to update Todo");
		
		Set<ConstraintViolation<Todo>> constraintViolations = validator.validate(todo);
		
		String errorMessage = this.buildErrorMessage(constraintViolations);
		
		if (!errorMessage.isEmpty()) {
			LOG.error("Error When Updating Todo: " + errorMessage);
			rsResponse.setErrorMessage("Error When Updating Todo: " + errorMessage);
			return rsResponse;
		} else {
			TodoCommand todoCommand = new TodoCommand();
			todoCommand.setTodo(todo);
			todoCommand.setId(UUID.randomUUID());
			
			GenericCommandHeader header = new GenericCommandHeader(GenericCommandType.UPDATE_TODO.toString(), SCHEMA_VERSION, new Timestamp(System.currentTimeMillis()));
			
			todoCommand.setHeader(header);
			
			this.genericCommandBus.send(todoCommand);
			
			this.eventPublisher.publishEvent(new TodoServiceEvent(this, todo, "TodoUpdatedEvent"));
			
			rsResponse.setMessage("Todo sent to AWS for update");
			rsResponse.setPayload(todo);
			
			return rsResponse;
			
		}
	}	

	@DeleteMapping("/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public RSResponse<Todo> deleteTodo(@PathVariable("id") Long id) {
		RSResponse<Todo> rsResponse = new RSResponse<>();
		
		LOG.info("Received request to delete Todo");
		
		Todo todo = this.todoService.getTodo(id);
		
		Set<ConstraintViolation<Todo>> constraintViolations = validator.validate(todo);
		
		String errorMessage = this.buildErrorMessage(constraintViolations);
		
		if (todo == null) {
			LOG.error("Error When Deleting Todo: " + errorMessage);
			rsResponse.setErrorMessage("Error When Deleting Todo: " + errorMessage);
			return rsResponse;
		} else {
			TodoCommand todoCommand = new TodoCommand();
			todoCommand.setTodo(todo);
			todoCommand.setId(UUID.randomUUID());
			
			GenericCommandHeader header = new GenericCommandHeader(GenericCommandType.DELETE_TODO.toString(), SCHEMA_VERSION, new Timestamp(System.currentTimeMillis()));
			
			todoCommand.setHeader(header);
			
			this.genericCommandBus.send(todoCommand);
			
			this.eventPublisher.publishEvent(new TodoServiceEvent(this, todo, "TodoDeletedEvent"));
			
			rsResponse.setMessage("Todo sent to AWS for deletion");
			rsResponse.setPayload(todo);
			
			return rsResponse;
			
		}
	}	
	
	private String buildErrorMessage(Set<ConstraintViolation<Todo>> constraintViolations) {
		String message = "";
		
		if (constraintViolations == null || constraintViolations.isEmpty()) {
			return message;
		} else {
			for (ConstraintViolation<Todo> constraintViolation : constraintViolations) {
				message += constraintViolation.getMessage() + " ";
			}
		}
		
		return message;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
