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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import uk.azdev.openfire.friendlist.Friend;
import uk.azdev.openfire.friendlist.FriendsList;
import uk.azdev.openfire.net.messages.incoming.FriendListMessage;

public class FriendListMessageProcessorTest {
	
	@Test
	public void testProcessMessage() {
		Friend self = new Friend("testUser");
		FriendsList friendsList = new FriendsList(self);
		FriendListMessage message = new FriendListMessage();
		message.addUser(100L, "friend1", "Friend 1");
		message.addUser(101L, "friend2", "Friend 2");
		message.addUser(102L, "friend3", "Friend 3");
		
		FriendListMessageProcessor processor = new FriendListMessageProcessor(friendsList);
		processor.processMessage(message);
		
		checkForFriend(friendsList, 100L, "friend1", "Friend 1");
		checkForFriend(friendsList, 101L, "friend2", "Friend 2");
		checkForFriend(friendsList, 102L, "friend3", "Friend 3");
	}

	private void checkForFriend(FriendsList friendsList, long userId, String userName, String displayName) {
		assertTrue("list does not contain friend \"" + userId + "\"", friendsList.containsFriend(userId));
		Friend friend = friendsList.getFriend(userId);
		assertEquals(userId, friend.getUserId());
		assertEquals(userName, friend.getUserName());
		assertEquals(displayName, friend.getDisplayName());
		
		Friend self = friendsList.getSelf();
		assertTrue(friendsList.areConnected(self, friend));
		assertTrue(friendsList.areConnected(friend, self));
	}

}
