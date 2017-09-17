package com.myththewolf.BotServ.lib.event.Objects;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class UserSendEvent {
	public MessageReceivedEvent e;
	public UserSendEvent(MessageReceivedEvent e) {
		this.e = e;
	}
	
	public MessageReceivedEvent getEvent() {
		return e;
	}
}
