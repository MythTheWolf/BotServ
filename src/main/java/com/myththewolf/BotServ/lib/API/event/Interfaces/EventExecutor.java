package com.myththewolf.BotServ.lib.API.event.Interfaces;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EventExecutor{
	EventType   eventType() default EventType.NOPEvent;
}
