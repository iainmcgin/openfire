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

import java.util.Set;

import uk.azdev.openfire.friendlist.FriendsList;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.incoming.UserSessionIdListMessage;

public class UserSessionIdListMessageProcessor implements IMessageProcessor {

	private FriendsList friendsList;

	public UserSessionIdListMessageProcessor(FriendsList friendsList) {
		this.friendsList = friendsList;
	}
	
	public void processMessage(IMessage msg) {
		UserSessionIdListMessage message = (UserSessionIdListMessage)msg;
		Set<Long> userIds = message.getUserIdList();
		
		for(long userId : userIds) {
			if(message.getSessionIdForUser(userId).isZero()) {
				friendsList.setFriendOffline(userId);
			} else {
				friendsList.setFriendOnline(userId, message.getSessionIdForUser(userId));
			}
		}
	}
	
}
