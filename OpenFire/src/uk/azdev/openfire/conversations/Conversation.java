/*
 * OpenFire - a Java API to access the XFire instant messaging network.
 * Copyright (C) 2007 Iain McGinniss
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package uk.azdev.openfire.conversations;

import java.util.LinkedList;
import java.util.List;

import uk.azdev.openfire.friendlist.Friend;
import uk.azdev.openfire.net.IMessageSender;
import uk.azdev.openfire.net.messages.bidirectional.ChatMessage;

public class Conversation {
	
	private IMessageSender messageSender;
	private Friend self;
	private Friend peer;
	
	private int myMessageIndex;
	private StringBuffer chatLog;
	
	private List<IConversationListener> listeners;
	
	public Conversation(Friend self, Friend peer, IMessageSender messageSender) {
		this.self = self;
		this.peer = peer;
		this.messageSender = messageSender;
		this.chatLog = new StringBuffer();
		this.myMessageIndex = 0;
		this.listeners = new LinkedList<IConversationListener>();
	}
	
	public void receiveMessage(ChatMessage chatMsg) {
		if(chatMsg.isContentMessage()) {
			addMessage(peer, chatMsg.getMessage());
			notifyListeners();
		}
	}

	public void sendMessage(String text) {
		ChatMessage message = new ChatMessage();
		message.setSessionId(peer.getSessionId());
		message.setContentPayload(myMessageIndex++, text);
		messageSender.sendMessage(message);
		
		addMessage(self, text);
		notifyListeners();
	}
	
	private void addMessage(Friend user, String message) {
		chatLog.append(user.getDisplayName());
		chatLog.append(": ");
		chatLog.append(message);
		chatLog.append("\n");
	}
	
	public Friend getPeer() {
		return peer;
	}
	
	public String getChatLog() {
		return chatLog.toString();
	}
	
	public void addChatListener(IConversationListener listener) {
		listeners.add(listener);
	}
	
	public void notifyListeners() {
		for(IConversationListener listener : listeners) {
			listener.chatLogUpdated();
		}
	}
}
