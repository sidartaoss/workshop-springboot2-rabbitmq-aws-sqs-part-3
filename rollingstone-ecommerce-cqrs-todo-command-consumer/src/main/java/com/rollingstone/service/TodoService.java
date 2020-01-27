package com.rollingstone.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rollingstone.domain.Todo;
import com.rollingstone.repositories.TodoRepository;

@Service
public class TodoService {

	private static final Logger LOG = LoggerFactory.getLogger(TodoService.class);

	@Autowired
	private TodoRepository todoRepository;

	public TodoService() {
		super();
	}

	public TodoService(TodoRepository todoRepository) {
		super();
		this.todoRepository = todoRepository;
	}
	
	public Todo getTodo(Long id) {
		LOG.info("Calling getTodo(). ID: {}", id);
		return this.todoRepository.findById(id)
				.get();
	}
	
	public List<Todo> getAllTodos() {
		LOG.info("Calling getAllTodos");
		return StreamSupport.stream(this
				.todoRepository.findAll().spliterator(), false)
				.collect(Collectors.toList());
	}
	
	public void saveTodo(Todo todo) {
		LOG.info("Calling saveTodo(): {}", todo.toString());
		
		this.todoRepository.save(todo);
	}

	public void deleteTodoByID(Long id) {
		LOG.info("Calling deleteTodoByID(): {}", id);
		
		this.todoRepository.deleteById(id);
	}
	
}
