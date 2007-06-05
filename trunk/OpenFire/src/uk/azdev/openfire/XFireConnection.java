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
package uk.azdev.openfire;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import uk.azdev.openfire.common.OpenFireConfiguration;
import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.conversations.Conversation;
import uk.azdev.openfire.friendlist.Friend;
import uk.azdev.openfire.friendlist.FriendsList;
import uk.azdev.openfire.friendlist.messageprocessors.ChatMessageProcessor;
import uk.azdev.openfire.friendlist.messageprocessors.FriendListMessageProcessor;
import uk.azdev.openfire.friendlist.messageprocessors.FriendOfFriendListMessageProcessor;
import uk.azdev.openfire.friendlist.messageprocessors.FriendStatusMessageProcessor;
import uk.azdev.openfire.friendlist.messageprocessors.IMessageProcessor;
import uk.azdev.openfire.friendlist.messageprocessors.LoginChallengeMessageProcessor;
import uk.azdev.openfire.friendlist.messageprocessors.LoginFailureMessageProcessor;
import uk.azdev.openfire.friendlist.messageprocessors.LoginSuccessMessageProcessor;
import uk.azdev.openfire.friendlist.messageprocessors.NewVersionAvailableMessageProcessor;
import uk.azdev.openfire.friendlist.messageprocessors.UserSessionIdListMessageProcessor;
import uk.azdev.openfire.net.ConnectionController;
import uk.azdev.openfire.net.ConnectionStateListener;
import uk.azdev.openfire.net.IConnectionController;
import uk.azdev.openfire.net.IMessageSender;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.bidirectional.ChatMessage;
import uk.azdev.openfire.net.messages.bidirectional.ServerRoutedChatMessage;
import uk.azdev.openfire.net.messages.incoming.FriendListMessage;
import uk.azdev.openfire.net.messages.incoming.FriendOfFriendListMessage;
import uk.azdev.openfire.net.messages.incoming.FriendStatusMessage;
import uk.azdev.openfire.net.messages.incoming.LoginChallengeMessage;
import uk.azdev.openfire.net.messages.incoming.LoginFailureMessage;
import uk.azdev.openfire.net.messages.incoming.LoginSuccessMessage;
import uk.azdev.openfire.net.messages.incoming.NewVersionAvailableMessage;
import uk.azdev.openfire.net.messages.incoming.UserSessionIdListMessage;
import uk.azdev.openfire.net.messages.outgoing.ClientInformationMessage;
import uk.azdev.openfire.net.messages.outgoing.ClientVersionMessage;

public class XFireConnection implements IMessageSender, ConnectionStateListener, ConnectionStatusUpdater {

	private OpenFireConfiguration config;
	private IConnectionController controller;
	private KeepaliveTimer timer;
	
	private Map<Integer,IMessageProcessor> processorMap;
	private FriendsList friendList;
	
	private Map<SessionId, Conversation> activeConversations;
	
	private List<ConnectionEventListener> listeners;
	
	
	public XFireConnection(OpenFireConfiguration config) {
		this.config = config;
		
		friendList = new FriendsList(new Friend(config.getUsername()));
		
		controller = new ConnectionController(config.getXfireServerHostName(), config.getXfireServerPortNum());
		controller.addStateListener(this);
		timer = new KeepaliveTimer(this);
		activeConversations = new ConcurrentHashMap<SessionId, Conversation>();
		listeners = new LinkedList<ConnectionEventListener>();
		
		initProcessorMap();
	}
	
	public void connect() throws UnknownHostException, IOException {
		controller.start();
		timer.start();
		sendClientInfo();
	}
	
	public void disconnect() throws InterruptedException, IOException {
		timer.stop();
		controller.stop();
		disconnected();
	}
	
	public FriendsList getFriendList() {
		return friendList;
	}
	
	private void initProcessorMap() {
		processorMap = new HashMap<Integer, IMessageProcessor>();
		processorMap.put(FriendListMessage.FRIEND_LIST_MESSAGE_ID, new FriendListMessageProcessor(this));
		processorMap.put(FriendOfFriendListMessage.FRIEND_OF_FRIEND_LIST_MESSAGE_TYPE, new FriendOfFriendListMessageProcessor(friendList));
		processorMap.put(FriendStatusMessage.FRIEND_STATUS_MESSAGE_ID, new FriendStatusMessageProcessor(friendList));
		processorMap.put(UserSessionIdListMessage.USER_SESSION_ID_LIST_MESSAGE_ID, new UserSessionIdListMessageProcessor(friendList));
		processorMap.put(LoginChallengeMessage.LOGIN_CHALLENGE_MESSAGE_ID, new LoginChallengeMessageProcessor(this, config));
		processorMap.put(LoginSuccessMessage.LOGIN_SUCCESS_MESSAGE_ID, new LoginSuccessMessageProcessor(this, friendList.getSelf(), config));
		processorMap.put(LoginFailureMessage.LOGIN_FAILURE_MESSAGE_ID, new LoginFailureMessageProcessor(this));
		processorMap.put(NewVersionAvailableMessage.TYPE_ID, new NewVersionAvailableMessageProcessor(this, config));
		processorMap.put(ServerRoutedChatMessage.SR_TYPE_ID, new ChatMessageProcessor(this));
		processorMap.put(ChatMessage.TYPE_ID, new ChatMessageProcessor(this));
	}
	
	private void sendClientInfo() {
		ClientInformationMessage clInfo = new ClientInformationMessage();
		clInfo.setVersion(config.getLongVersion());
		clInfo.setSkinList(config.getSkinList());
		sendMessage(clInfo);
		
		ClientVersionMessage clVersion = new ClientVersionMessage();
		clVersion.setVersion(config.getShortVersion());
		sendMessage(clVersion);
	}
	
	public void messageReceived(IMessage message) {
		if(processorMap.containsKey(message.getMessageId())) {
			processorMap.get(message.getMessageId()).processMessage(message);
		}
	}
	
	public void connectionError(Exception e) {
		try {
			disconnect();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(ConnectionEventListener listener : listeners) {
			listener.connectionError();
		}
	}

	public void loginFailed() {
		try {
			disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(ConnectionEventListener listener : listeners) {
			listener.loginFailed();
		}
	}
	
	public void reconnect() throws InterruptedException, IOException {
		disconnect();
		connect();
	}

	public void sendMessage(IMessage message) {
		controller.sendMessage(message);
		timer.resetTimer();
	}

	public Conversation getConversation(SessionId peerSid) {
		
		if(!activeConversations.containsKey(peerSid)) {
			Friend peer = friendList.getOnlineFriend(peerSid);
			Conversation conversation = new Conversation(friendList.getSelf(), peer, this);
			activeConversations.put(peerSid, conversation);
		}
		
		return activeConversations.get(peerSid);
	}
	
	public void addListener(ConnectionEventListener listener) {
		listeners.add(listener);
	}
	
	public void conversationUpdate(SessionId sessionId) {
		for(ConnectionEventListener listener : listeners) {
			listener.conversationUpdate(sessionId);
		}
	}
	
	public void friendsListUpdated() {
		for(ConnectionEventListener listener : listeners) {
			listener.friendsListUpdated();
		}
	}
	
	public void disconnected() {
		for(ConnectionEventListener listener : listeners) {
			listener.disconnected();
		}
	}
}
