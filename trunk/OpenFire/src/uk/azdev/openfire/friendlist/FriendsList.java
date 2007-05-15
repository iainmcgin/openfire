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
package uk.azdev.openfire.friendlist;

import java.util.HashMap;
import java.util.Map;

import uk.azdev.openfire.common.SessionId;

public class FriendsList {

	private Friend self;
	
	private Map<Long, Friend> friendsById;
	private Map<SessionId, Friend> onlineFriends;
	
	public FriendsList(Friend self) {
		this.self = self;
		friendsById = new HashMap<Long, Friend>();
		onlineFriends = new HashMap<SessionId, Friend>();
	}

	public boolean containsFriend(long userId) {
		return friendsById.containsKey(userId);
	}

	public void addFriend(Friend newFriend, Friend connectedFriend) {
		friendsById.put(newFriend.getUserId(), newFriend);
		connectedFriend.addFriend(newFriend);
	}
	
	public void addDirectFriend(Friend friend) {
		addFriend(friend, self);
	}

	public Friend getFriend(long userId) {
		return friendsById.get(userId);
	}

	public Friend getSelf() {
		return self;
	}

	public void setFriendOnline(long userId, SessionId sessionIdForUser) {
		Friend friend = getFriend(userId);
		friend.setOnline(sessionIdForUser);
		onlineFriends.put(sessionIdForUser, friend);
	}

	public Friend getOnlineFriend(SessionId sessionId) {
		return onlineFriends.get(sessionId);
	}

	public void setFriendOffline(long userId) {
		Friend friend = getFriend(userId);
		SessionId oldSid = friend.getSessionId();
		onlineFriends.remove(oldSid);
		friend.setOffline();
	}
	
}
