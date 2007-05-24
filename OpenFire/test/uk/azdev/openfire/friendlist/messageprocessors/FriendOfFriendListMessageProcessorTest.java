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

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import uk.azdev.openfire.friendlist.Friend;
import uk.azdev.openfire.friendlist.FriendsList;
import uk.azdev.openfire.net.messages.incoming.FriendOfFriendListMessage;

public class FriendOfFriendListMessageProcessorTest {

	@Test
	public void testProcessMessage() {
		Friend self = new Friend("me");
		FriendsList list = new FriendsList(self);
		
		Friend alice = new Friend(100L, "alice", "Alice");
		Friend bob = new Friend(101L, "bob", "Bob");
		
		list.addFriend(alice);
		list.addFriend(bob);
		list.connect(self, alice.getUserId());
		list.connect(self, bob.getUserId());
		
		
		FriendOfFriendListMessage message = new FriendOfFriendListMessage();
		
		Friend carol = new Friend(102L, "carol", "Carol");
		List<Long> carolsFriends = new LinkedList<Long>();
		carolsFriends.add(100L);
		carolsFriends.add(101L);
		carolsFriends.add(103L);
		
		Friend dave = new Friend(103L, "dave", "Dave");
		List<Long> davesFriends = new LinkedList<Long>();
		davesFriends.add(101L);
		davesFriends.add(102L);
		
		message.addFriend(carol, carolsFriends);
		message.addFriend(dave, davesFriends);
		
		FriendOfFriendListMessageProcessor processor = new FriendOfFriendListMessageProcessor();
		processor.processMessage(list, message);
		
		assertTrue(list.areConnected(alice, carol));
		assertTrue(list.areConnected(bob, carol));
		assertTrue(list.areConnected(dave, carol));
		assertTrue(list.areConnected(dave, bob));
		assertFalse(list.areConnected(dave, self));
		assertFalse(list.areConnected(dave, alice));
	}

}
