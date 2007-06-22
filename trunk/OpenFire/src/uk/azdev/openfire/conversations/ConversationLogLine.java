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
package uk.azdev.openfire.conversations;

import uk.azdev.openfire.friendlist.Friend;

public class ConversationLogLine {

	private Friend originator;
	private String message;
	
	public ConversationLogLine(Friend originator, String message) {
		this.originator = originator;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Friend getOriginator() {
		return originator;
	}

	public void setOriginator(Friend originator) {
		this.originator = originator;
	}
	
	@Override
	public String toString() {
		StringBuffer logLine = new StringBuffer();
		logLine.append(originator.getDisplayName());
		logLine.append(": ");
		logLine.append(message);
		logLine.append("\n");
		
		return logLine.toString();
	}
}
