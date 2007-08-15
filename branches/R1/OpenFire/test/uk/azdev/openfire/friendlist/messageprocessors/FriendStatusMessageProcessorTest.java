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

import uk.azdev.openfire.net.messages.incoming.FriendStatusMessage;


public class FriendStatusMessageProcessorTest extends AbstractFriendsListTest {

	@Test
	public void testProcessMessage() {
		
		setFriendsLoggedIn();
		
		FriendStatusMessage message = new FriendStatusMessage();
		message.addStatus(aliceSid, "AFK");
		message.addStatus(bobSid, "At work");
		message.addStatus(carolSid, "Out to Lunch");
		
		FriendStatusMessageProcessor processor = new FriendStatusMessageProcessor(friendsList);
		processor.processMessage(message);
		
		assertEquals("AFK", friendsList.getFriend(alice.getUserId()).getStatus());
		assertEquals("At work", friendsList.getFriend(bob.getUserId()).getStatus());
		assertEquals("Out to Lunch", friendsList.getFriend(carol.getUserId()).getStatus());
	}
	
}
