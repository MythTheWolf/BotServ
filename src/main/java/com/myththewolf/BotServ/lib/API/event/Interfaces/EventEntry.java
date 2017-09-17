package com.myththewolf.BotServ.lib.API.event.Interfaces;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EventEntry {
	private EventListener Runner;
	private Method RunnnerMethod;

	public EventEntry(EventListener executor, Method m) throws NoSuchMethodException, SecurityException {
		RunnnerMethod = m;
		Runner = executor;
	}

	public EventListener getExecutor() {
		return this.Runner;
	}

	public Method getMethod() {
		return this.RunnnerMethod;
	}

	public static boolean verify(Method M) {
		return ((M.getParameterTypes().length > 0)
				&& (EventType.valueOf(M.getParameterTypes()[0].getSimpleName()) != null));
	}

	public static EventType findType(Method M) {
		return EventType.valueOf(M.getParameterTypes()[0].getSimpleName());
	}

	public void runEvent(Object EventObject)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		System.out.println("running event....object");
		this.RunnnerMethod.invoke(this.Runner, EventObject);
	}
}
