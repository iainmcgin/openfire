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
package uk.azdev.openfire.net.messages;

import java.util.HashMap;
import java.util.Map;

import uk.azdev.openfire.net.messages.incoming.BuddyListMessage;
import uk.azdev.openfire.net.messages.incoming.BuddyStatusMessage;
import uk.azdev.openfire.net.messages.incoming.LoginChallengeMessage;
import uk.azdev.openfire.net.messages.incoming.LoginFailureMessage;
import uk.azdev.openfire.net.messages.incoming.LoginSuccessMessage;
import uk.azdev.openfire.net.messages.incoming.UserSessionIdListMessage;
import uk.azdev.openfire.net.messages.outgoing.ClientConfigurationMessage;
import uk.azdev.openfire.net.messages.outgoing.ClientInformationMessage;
import uk.azdev.openfire.net.messages.outgoing.ClientVersionMessage;
import uk.azdev.openfire.net.messages.outgoing.LoginRequestMessage;


public class MessageFactory {
	
	private Map<Integer, IMessage> messageMap;
	
	public MessageFactory() {
		messageMap = new HashMap<Integer, IMessage>();
		
		addMessageType(new LoginRequestMessage());
		addMessageType(new ClientInformationMessage());
		addMessageType(new ClientVersionMessage());
		addMessageType(new LoginChallengeMessage());
		addMessageType(new LoginSuccessMessage());
		addMessageType(new LoginFailureMessage());
		addMessageType(new BuddyListMessage());
		addMessageType(new UserSessionIdListMessage());
		addMessageType(new ClientConfigurationMessage());
		addMessageType(new BuddyStatusMessage());
	}

	private void addMessageType(IMessage message) {
		messageMap.put(message.getMessageId(), message);
	}
	
	public boolean isKnownMessageType(int type) {
		return messageMap.containsKey(type);
	}
	
	public IMessage createMessage(int type) {
		IMessage messageType = messageMap.get(type);
		if(messageType == null) {
			throw new UnknownMessageTypeException(type);
		}
		
		return messageType.newInstance();
	}

}