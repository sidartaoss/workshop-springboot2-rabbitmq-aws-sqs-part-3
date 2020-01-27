package com.rollingstone.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rollingstone.domain.Todo;
import com.rollingstone.repository.TodoRepository;

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
		LOG.info("Calling TodoService.getTodo() by id: {}", id);
		Optional<Todo> optional = this.todoRepository.findById(id);

		return optional.isPresent() ? this.todoRepository.findById(id).get()
				: null;
	}

	public List<Todo> getAllTodos() {
		LOG.info("Calling TodoService.getAllTodos");
		Iterable<Todo> iterable = this.todoRepository.findAll();
		if (iterable == null || !iterable.iterator().hasNext()) {
			return Collections.<Todo>emptyList();
		}
		return iterable == null || !iterable.iterator().hasNext() ?
				Collections.<Todo>emptyList() :
					StreamSupport.stream(iterable.spliterator(), false)
					.collect(Collectors.toList());
	}

}
