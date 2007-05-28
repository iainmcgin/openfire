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
import java.util.Map;

import uk.azdev.openfire.common.OpenFireConfiguration;
import uk.azdev.openfire.friendlist.Friend;
import uk.azdev.openfire.friendlist.FriendsList;
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
import uk.azdev.openfire.net.MessageListener;
import uk.azdev.openfire.net.messages.IMessage;
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

public class XFireConnection implements MessageListener {

	OpenFireConfiguration config;
	ConnectionController controller;
	
	private Map<Integer,IMessageProcessor> processorMap;
	private FriendsList friendList;
	
	public XFireConnection(OpenFireConfiguration config) {
		this.config = config;
		
		friendList = new FriendsList(new Friend(config.getUsername()));
		initProcessorMap();
		
		controller = new ConnectionController();
		controller.addMessageListener(this);
	}
	
	public void connect() throws UnknownHostException, IOException {
		controller.start();
		sendClientInfo();
	}
	
	public void disconnect() throws InterruptedException, IOException {
		controller.stop();
	}
	
	public FriendsList getFriendList() {
		return friendList;
	}
	
	private void initProcessorMap() {
		processorMap = new HashMap<Integer, IMessageProcessor>();
		processorMap.put(FriendListMessage.FRIEND_LIST_MESSAGE_ID, new FriendListMessageProcessor(friendList));
		processorMap.put(FriendOfFriendListMessage.FRIEND_OF_FRIEND_LIST_MESSAGE_TYPE, new FriendOfFriendListMessageProcessor(friendList));
		processorMap.put(FriendStatusMessage.FRIEND_STATUS_MESSAGE_ID, new FriendStatusMessageProcessor(friendList));
		processorMap.put(UserSessionIdListMessage.USER_SESSION_ID_LIST_MESSAGE_ID, new UserSessionIdListMessageProcessor(friendList));
		processorMap.put(LoginChallengeMessage.LOGIN_CHALLENGE_MESSAGE_ID, new LoginChallengeMessageProcessor(controller, config));
		processorMap.put(LoginSuccessMessage.LOGIN_SUCCESS_MESSAGE_ID, new LoginSuccessMessageProcessor(controller, config));
		processorMap.put(LoginFailureMessage.LOGIN_FAILURE_MESSAGE_ID, new LoginFailureMessageProcessor(this));
		processorMap.put(NewVersionAvailableMessage.TYPE_ID, new NewVersionAvailableMessageProcessor(this, config));
	}
	
	private void sendClientInfo() {
		ClientInformationMessage clInfo = new ClientInformationMessage();
		clInfo.setVersion(config.getLongVersion());
		clInfo.setSkinList(config.getSkinList());
		controller.sendMessage(clInfo);
		
		ClientVersionMessage clVersion = new ClientVersionMessage();
		clVersion.setVersion(config.getShortVersion());
		controller.sendMessage(clVersion);
	}
	
	public void messageReceived(IMessage message) {
		if(processorMap.containsKey(message.getMessageId())) {
			processorMap.get(message.getMessageId()).processMessage(message);
		}
	}

	public void addMessageListener(MessageListener listener) {
		controller.addMessageListener(listener);
	}

	public void loginFailed() throws InterruptedException, IOException {
		controller.stop();
	}
	
	public void reconnect() throws InterruptedException, IOException {
		controller.stop();
		controller.start();
	}
}
