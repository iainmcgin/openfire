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
package uk.azdev.openfire.friendlist.messageprocessors;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.azdev.openfire.ConnectionEventDispatcher;
import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.friendlist.Friend;
import uk.azdev.openfire.net.messages.incoming.UserSessionIdListMessage;

public class UserSessionIdListMessageProcessorTest extends AbstractFriendsListTest {


	@Test
	public void testProcessMessage() {
		UserSessionIdListMessage message = new UserSessionIdListMessage();
		
		message.addUserMapping(alice.getUserId(), aliceSid);
		message.addUserMapping(bob.getUserId(), bobSid);
		message.addUserMapping(carol.getUserId(), carolSid);
		
		UserSessionIdListMessageProcessor processor = new UserSessionIdListMessageProcessor(friendsList, new ConnectionEventDispatcher());
		processor.processMessage(message);
		
		checkOnline(friendsList.getFriend(alice.getUserId()), aliceSid);
		checkOnline(friendsList.getFriend(bob.getUserId()), bobSid);
		checkOnline(friendsList.getFriend(carol.getUserId()), carolSid);
	}

	private void checkOnline(Friend f, SessionId expectedSid) {
		assertEquals(expectedSid, f.getSessionId());
		assertTrue(f.isOnline());
	}
	
	@Test
	public void testProcessMessage_withOfflineNotification() {
		setFriendsLoggedIn();
		
		UserSessionIdListMessage message = new UserSessionIdListMessage();
		message.addUserMapping(alice.getUserId(), new SessionId());
		
		UserSessionIdListMessageProcessor processor = new UserSessionIdListMessageProcessor(friendsList, new ConnectionEventDispatcher());
		processor.processMessage(message);
		
		assertFalse(alice.isOnline());
	}
}
