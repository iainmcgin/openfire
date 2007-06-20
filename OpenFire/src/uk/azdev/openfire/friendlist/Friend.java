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

import java.net.InetSocketAddress;

import uk.azdev.openfire.common.SessionId;

public class Friend {

	private long userId;
	private String userName;
	private String displayName;
	private String statusString;
	private SessionId sessionId;
	private InetSocketAddress address;
	
	public Friend(String userName) {
		this(-1, userName, null);
	}
	
	public Friend(long userId) {
		this.userId = userId;
	}
	
	public Friend(long userId, String userName, String displayName) {
		this(userId);
		
		this.userName = userName;
		this.displayName = displayName;
		this.statusString = "";
		this.sessionId = null;
		this.address = null;
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
		if(displayName == null) {
			return userName;
		}
		
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
		this.address = null;
	}
	
	public InetSocketAddress getAddress() {
		return address;
	}
	
	public void setAddress(InetSocketAddress netAddr) {
		this.address = netAddr;
	}
	
	public void update(Friend friend) {
		if(this.userName == null && friend.userName != null) {
			this.userName = friend.userName;
		}
		
		if(friend.displayName != null) {
			this.displayName = friend.displayName;
		}
		
		if(friend.statusString != null) {
			this.statusString = friend.statusString;
		}

		if(friend.sessionId != null) {
			this.sessionId = friend.sessionId;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Friend) {
			Friend f = (Friend)obj;
			return f.getUserId() == this.getUserId();
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
		buffer.append("Friend[uid=");
		if(userId == -1) {
			buffer.append("???");
		} else {
			buffer.append(userId);
		}
		buffer.append(", uName=");
		if(userName == null) {
			buffer.append("???");
		} else {
			buffer.append(userName);
		}

		buffer.append(", display=");
		if(displayName == null) {
			buffer.append("???");
		} else {
			buffer.append('\"');
			buffer.append(displayName);
			buffer.append('\"');
		}
		
		buffer.append(']');
		
		return buffer.toString();
	}
}
