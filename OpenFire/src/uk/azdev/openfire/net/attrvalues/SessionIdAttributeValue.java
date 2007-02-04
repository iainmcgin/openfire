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
package uk.azdev.openfire.net.attrvalues;

import java.nio.ByteBuffer;

import static uk.azdev.openfire.net.util.IOUtil.*;

public class SessionIdAttributeValue implements AttributeValue {

	public static final int TYPE_ID = 3;
	public static final int SESSION_ID_SIZE = 16;

	private byte[] sessionId;
	
	public SessionIdAttributeValue() {
		sessionId = new byte[SESSION_ID_SIZE];
	}
	
	public SessionIdAttributeValue(byte[] sessionId) {
		this();
		setSessionId(sessionId);
	}

	public byte[] getSessionId() {
		return sessionId;
	}
	
	public void setSessionId(byte[] sessionId) {
		if(sessionId.length != SESSION_ID_SIZE) {
			throw new IllegalArgumentException("wrong session id size");
		}
		System.arraycopy(sessionId, 0, this.sessionId, 0, SESSION_ID_SIZE);
	}
	
	public int getSize() {
		return SESSION_ID_SIZE;
	}

	public int getTypeId() {
		return TYPE_ID;
	}

	public AttributeValue newInstance() {
		return new SessionIdAttributeValue();
	}

	public void readValue(ByteBuffer buffer) {
		buffer.get(sessionId);
	}

	public void writeValue(ByteBuffer buffer) {
		buffer.put(sessionId);
	}

	@Override
	public String toString() {
		return "SID:<" + printByteArray(sessionId) + ">";
	}
	
}
