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

import java.util.ArrayList;
import java.util.List;

import uk.azdev.openfire.common.SessionId;

public class Friend {

	private long userId;
	private String userName;
	private String displayName;
	private String statusString;
	private SessionId sessionId;
	
	private List<Friend> friends;
	
	public Friend(String userName) {
		this(-1, userName, "");
	}
	
	public Friend(long userId, String userName, String displayName) {
		this.userId = userId;
		this.userName = userName;
		this.displayName = displayName;
		friends = new ArrayList<Friend>();
	}

	public long getUserId() {
		return userId;
	}
	
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getStatus() {
		return statusString;
	}

	public void setStatus(String statusString) {
		this.statusString = statusString;
	}
	
	public List<Friend> getFriends() {
		return friends;
	}

	public boolean isFriend(Friend friend) {
		return friends.contains(friend);
	}
	
	public void addFriend(Friend friend) {
		if(friends.contains(friend)) {
			return;
		}
		friends.add(friend);
		friend.addFriend(this);
	}
	
	public boolean isOnline() {
		return sessionId != null;
	}

	public SessionId getSessionId() {
		return sessionId;
	}
	
	public void setOnline(SessionId sessionId) {
		this.sessionId = sessionId;
	}
	
	public void setOffline() {
		this.sessionId = null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Friend) {
			Friend f = (Friend)obj;
			return f.getUserName().equals(this.getUserName());
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return (int)getUserId();
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Friend ");
		buffer.append(userId);
		if(userName != null) {
			buffer.append(" ");
			buffer.append(userName);
		}

		if(userName != null) {
			buffer.append(" ");
			buffer.append(displayName);
		}
		
		return buffer.toString();
	}
}
