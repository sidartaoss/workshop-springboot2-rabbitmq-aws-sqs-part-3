package com.rollingstone.cloud.messaging.aws;

import java.util.LinkedHashMap;
import java.util.Map;

import com.rollingstone.command.GenericCommandType;

public class CommandQueues {

	private static final String CATEGORY_QUEUE = "Category_Command_Queue";
	
	private static final String TODO_QUEUE = "TODO_Queue";
	
	public static Map<String, String> commandQueueMap;
	
	static {
		commandQueueMap = new LinkedHashMap<>();
		
		commandQueueMap.put(GenericCommandType.CREATE_CATEGORY.toString(), CATEGORY_QUEUE);
		commandQueueMap.put(GenericCommandType.UPDATE_CATEGORY.toString(), CATEGORY_QUEUE);
		
		commandQueueMap.put(GenericCommandType.CREATE_TODO.toString(), TODO_QUEUE);
		commandQueueMap.put(GenericCommandType.UPDATE_TODO.toString(), TODO_QUEUE);
		commandQueueMap.put(GenericCommandType.DELETE_TODO.toString(), TODO_QUEUE);
		
	}
}
