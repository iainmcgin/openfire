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

import org.junit.Before;

import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.friendlist.Friend;
import uk.azdev.openfire.friendlist.FriendsList;
import uk.azdev.openfire.friendlist.Self;

public abstract class AbstractFriendsListTest {

	protected Self self;
	
	protected Friend alice;
	protected Friend bob;
	protected Friend carol;
	
	protected SessionId aliceSid;
	protected SessionId bobSid;
	protected SessionId carolSid;
	
	protected FriendsList friendsList;
	
	@Before
	public void setUp() {
		self = new Self("me");
		
		alice = new Friend(100, "alice", "Alice");
		bob = new Friend(101, "bob", "Bob");
		carol = new Friend(102, "carol", "Carol");
		
		aliceSid = new SessionId(1000);
		bobSid = new SessionId(2000);
		carolSid = new SessionId(3000);
		
		friendsList = new FriendsList(self);
		friendsList.addDirectFriend(alice);
		friendsList.addDirectFriend(bob);
		friendsList.addDirectFriend(carol);
	}
	
	public void setFriendsLoggedIn() {
		friendsList.setFriendOnline(alice.getUserId(), aliceSid);
		friendsList.setFriendOnline(bob.getUserId(), bobSid);
		friendsList.setFriendOnline(carol.getUserId(), carolSid);
	}
	
}
