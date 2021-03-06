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

import uk.azdev.openfire.net.messages.bidirectional.ChatMessage;
import uk.azdev.openfire.net.messages.incoming.DIdMessage;
import uk.azdev.openfire.net.messages.incoming.FriendGameInfoMessage;
import uk.azdev.openfire.net.messages.incoming.FriendListMessage;
import uk.azdev.openfire.net.messages.incoming.FriendOfFriendListMessage;
import uk.azdev.openfire.net.messages.incoming.FriendStatusMessage;
import uk.azdev.openfire.net.messages.incoming.IncomingInvitationMessage;
import uk.azdev.openfire.net.messages.incoming.LoginChallengeMessage;
import uk.azdev.openfire.net.messages.incoming.LoginFailureMessage;
import uk.azdev.openfire.net.messages.incoming.LoginSuccessMessage;
import uk.azdev.openfire.net.messages.incoming.NewVersionAvailableMessage;
import uk.azdev.openfire.net.messages.incoming.UserSessionIdListMessage;
import uk.azdev.openfire.net.messages.outgoing.AcceptInvitationMessage;
import uk.azdev.openfire.net.messages.outgoing.ClientConfigurationMessage;
import uk.azdev.openfire.net.messages.outgoing.ClientInformationMessage;
import uk.azdev.openfire.net.messages.outgoing.ClientVersionMessage;
import uk.azdev.openfire.net.messages.outgoing.KeepaliveMessage;
import uk.azdev.openfire.net.messages.outgoing.LoginRequestMessage;
import uk.azdev.openfire.net.messages.outgoing.OutgoingInvitationMessage;
import uk.azdev.openfire.net.messages.outgoing.RejectInvitationMessage;

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
		assertTrue(factory.isKnownMessageType(FriendListMessage.FRIEND_LIST_MESSAGE_ID));
		assertTrue(factory.isKnownMessageType(UserSessionIdListMessage.USER_SESSION_ID_LIST_MESSAGE_ID));
		assertTrue(factory.isKnownMessageType(ClientConfigurationMessage.CLIENT_CONFIGURATION_MESSAGE_ID));
		assertTrue(factory.isKnownMessageType(FriendStatusMessage.FRIEND_STATUS_MESSAGE_ID));
		assertTrue(factory.isKnownMessageType(KeepaliveMessage.KEEP_ALIVE_MESSAGE_ID));
		assertTrue(factory.isKnownMessageType(FriendGameInfoMessage.FRIEND_GAME_INFO_MESSAGE_ID));
		assertTrue(factory.isKnownMessageType(DIdMessage.DID_MESSAGE_TYPE_ID));
		assertTrue(factory.isKnownMessageType(FriendOfFriendListMessage.FRIEND_OF_FRIEND_LIST_MESSAGE_TYPE));
		assertTrue(factory.isKnownMessageType(NewVersionAvailableMessage.TYPE_ID));
		assertTrue(factory.isKnownMessageType(ChatMessage.TYPE_ID));
		assertTrue(factory.isKnownMessageType(IncomingInvitationMessage.TYPE_ID));
		assertTrue(factory.isKnownMessageType(AcceptInvitationMessage.TYPE_ID));
		assertTrue(factory.isKnownMessageType(RejectInvitationMessage.TYPE_ID));
		assertTrue(factory.isKnownMessageType(OutgoingInvitationMessage.TYPE_ID));
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
		assertTrue(factory.createMessage(FriendListMessage.FRIEND_LIST_MESSAGE_ID) instanceof FriendListMessage);
		assertTrue(factory.createMessage(UserSessionIdListMessage.USER_SESSION_ID_LIST_MESSAGE_ID) instanceof UserSessionIdListMessage);
		assertTrue(factory.createMessage(ClientConfigurationMessage.CLIENT_CONFIGURATION_MESSAGE_ID) instanceof ClientConfigurationMessage);
		assertTrue(factory.createMessage(FriendStatusMessage.FRIEND_STATUS_MESSAGE_ID) instanceof FriendStatusMessage);
		assertTrue(factory.createMessage(KeepaliveMessage.KEEP_ALIVE_MESSAGE_ID) instanceof KeepaliveMessage);
		assertTrue(factory.createMessage(FriendGameInfoMessage.FRIEND_GAME_INFO_MESSAGE_ID) instanceof FriendGameInfoMessage);
		assertTrue(factory.createMessage(DIdMessage.DID_MESSAGE_TYPE_ID) instanceof DIdMessage);
		assertTrue(factory.createMessage(FriendOfFriendListMessage.FRIEND_OF_FRIEND_LIST_MESSAGE_TYPE) instanceof FriendOfFriendListMessage);
		assertTrue(factory.createMessage(NewVersionAvailableMessage.TYPE_ID) instanceof NewVersionAvailableMessage);
		assertTrue(factory.createMessage(ChatMessage.TYPE_ID) instanceof ChatMessage);
		assertTrue(factory.createMessage(IncomingInvitationMessage.TYPE_ID) instanceof IncomingInvitationMessage);
		assertTrue(factory.createMessage(AcceptInvitationMessage.TYPE_ID) instanceof AcceptInvitationMessage);
		assertTrue(factory.createMessage(RejectInvitationMessage.TYPE_ID) instanceof RejectInvitationMessage);
		assertTrue(factory.createMessage(OutgoingInvitationMessage.TYPE_ID) instanceof OutgoingInvitationMessage);
	}
	
	@Test(expected=UnknownMessageTypeException.class)
	public void testCreateUnknownMessageType() {
		factory.createMessage(-1);
	}
}
