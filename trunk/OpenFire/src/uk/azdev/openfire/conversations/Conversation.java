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

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import uk.azdev.openfire.common.Logging;
import uk.azdev.openfire.common.OpenFireConfiguration;
import uk.azdev.openfire.friendlist.Friend;
import uk.azdev.openfire.net.IMessageSender;
import uk.azdev.openfire.net.ProtocolConstants;
import uk.azdev.openfire.net.messages.bidirectional.ChatMessage;
import uk.azdev.openfire.net.util.IOUtil;

public class Conversation {
	
	private IMessageSender messageSender;
	private Friend self;
	private Friend peer;
	
	private int myMessageIndex;
	private LinkedList<String> chatLog;
	
	private long lastClientInfoTransmitTime;
	
	private List<IConversationListener> listeners;
	
	private OpenFireConfiguration config;
	
	public Conversation(Friend self, Friend peer, IMessageSender messageSender, OpenFireConfiguration config) {
		this.self = self;
		this.peer = peer;
		this.messageSender = messageSender;
		this.chatLog = new LinkedList<String>();
		this.myMessageIndex = 0;
		this.lastClientInfoTransmitTime = 0;
		this.listeners = new LinkedList<IConversationListener>();
		this.config = config;
	}
	
	public void receiveMessage(ChatMessage chatMsg) {
		if(chatMsg.isContentMessage()) {
			addMessage(peer, chatMsg.getMessage());
			notifyListeners();
		} else if(chatMsg.isTypingMessage()) {
			notifyListenersOfTyping();
		}
		
		sendClientInfoIfNecessary();
	}

	public void sendMessage(String text) {
		ChatMessage message = new ChatMessage();
		message.setSessionId(peer.getSessionId());
		message.setContentPayload(myMessageIndex++, text);
		messageSender.sendMessage(message);
		
		sendClientInfoIfNecessary();

		addMessage(self, text);
		notifyListeners();
	}

	private void sendClientInfoIfNecessary() {
		if(System.currentTimeMillis() - lastClientInfoTransmitTime > ProtocolConstants.CLIENT_INFO_TRANSMIT_INTERVAL) {
			messageSender.sendMessage(generateClientInfo());
			lastClientInfoTransmitTime = System.currentTimeMillis();
		}
	}
	
	private ChatMessage generateClientInfo() {
		ChatMessage message = new ChatMessage();
		message.setSessionId(peer.getSessionId());
		InetSocketAddress netAddr = self.getAddress();
		InetSocketAddress localAddr;
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			localAddr = new InetSocketAddress(localHost, config.getLocalPort());
		} catch (UnknownHostException e) {
			Logging.connectionLogger.warning("Unable to resolve local host address: will report 192.168.0.1 to peer");
			localAddr = InetSocketAddress.createUnresolved("192.168.0.1", config.getLocalPort());
		}
		
		message.setClientInfoPayload(netAddr, localAddr, 1L, IOUtil.generateSaltString());
		return message;
	}

	private void addMessage(Friend user, String message) {
		StringBuffer logLine = new StringBuffer();
		logLine.append(user.getDisplayName());
		logLine.append(": ");
		logLine.append(message);
		logLine.append("\n");
		
		chatLog.add(logLine.toString());
	}
	
	public Friend getPeer() {
		return peer;
	}
	
	public String getChatLog() {
		StringBuffer entireChatLog = new StringBuffer();
		for(String chatLine : chatLog) {
			entireChatLog.append(chatLine);
		}
		
		return entireChatLog.toString();
	}
	
	public String getLastMessage() {
		return chatLog.getLast();
	}
	
	public void addChatListener(IConversationListener listener) {
		listeners.add(listener);
	}
	
	public void notifyListeners() {
		for(IConversationListener listener : listeners) {
			listener.chatLogUpdated();
		}
	}
	
	public void notifyListenersOfTyping() {
		for(IConversationListener listener : listeners) {
			listener.peerIsTyping();
		}
	}
}
