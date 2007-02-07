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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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

public class MessageFactoryTest {

	private MessageFactory factory;
	
	@Before
	public void setUp() {
		factory = new MessageFactory();
	}
	
	@Test
	public void testIsKnownMessageType() {
		assertTrue(factory.isKnownMessageType(ClientVersionMessage.CLIENT_VERSION_MESSAGE_ID));
		assertTrue(factory.isKnownMessageType(ClientInformationMessage.CLIENT_INFO_MESSAGE_ID));
		assertTrue(factory.isKnownMessageType(LoginRequestMessage.LOGIN_REQUEST_MESSAGE_ID));
		assertTrue(factory.isKnownMessageType(LoginChallengeMessage.LOGIN_CHALLENGE_MESSAGE_ID));
		assertTrue(factory.isKnownMessageType(LoginSuccessMessage.LOGIN_SUCCESS_MESSAGE_ID));
		assertTrue(factory.isKnownMessageType(LoginFailureMessage.LOGIN_FAILURE_MESSAGE_ID));
		assertTrue(factory.isKnownMessageType(BuddyListMessage.BUDDY_LIST_MESSAGE_ID));
		assertTrue(factory.isKnownMessageType(UserSessionIdListMessage.USER_SESSION_ID_LIST_MESSAGE_ID));
		assertTrue(factory.isKnownMessageType(ClientConfigurationMessage.CLIENT_CONFIGURATION_MESSAGE_ID));
		assertTrue(factory.isKnownMessageType(BuddyStatusMessage.BUDDY_STATUS_MESSAGE_ID));
		assertFalse(factory.isKnownMessageType(-1));
	}
	
	@Test
	public void testCreateMessage() {
		assertTrue(factory.createMessage(ClientVersionMessage.CLIENT_VERSION_MESSAGE_ID) instanceof ClientVersionMessage);
		assertTrue(factory.createMessage(ClientInformationMessage.CLIENT_INFO_MESSAGE_ID) instanceof ClientInformationMessage);
		assertTrue(factory.createMessage(LoginRequestMessage.LOGIN_REQUEST_MESSAGE_ID) instanceof LoginRequestMessage);
		assertTrue(factory.createMessage(LoginChallengeMessage.LOGIN_CHALLENGE_MESSAGE_ID) instanceof LoginChallengeMessage);
		assertTrue(factory.createMessage(LoginSuccessMessage.LOGIN_SUCCESS_MESSAGE_ID) instanceof LoginSuccessMessage);
		assertTrue(factory.createMessage(LoginFailureMessage.LOGIN_FAILURE_MESSAGE_ID) instanceof LoginFailureMessage);
		assertTrue(factory.createMessage(BuddyListMessage.BUDDY_LIST_MESSAGE_ID) instanceof BuddyListMessage);
		assertTrue(factory.createMessage(UserSessionIdListMessage.USER_SESSION_ID_LIST_MESSAGE_ID) instanceof UserSessionIdListMessage);
		assertTrue(factory.createMessage(ClientConfigurationMessage.CLIENT_CONFIGURATION_MESSAGE_ID) instanceof ClientConfigurationMessage);
		assertTrue(factory.createMessage(BuddyStatusMessage.BUDDY_STATUS_MESSAGE_ID) instanceof BuddyStatusMessage);
	}
}
