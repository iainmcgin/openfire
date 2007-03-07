/*
 * JFire - a Java API to access the XFire instant messaging network.
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
package uk.azdev.openfire.common;

import uk.azdev.openfire.net.util.IOUtil;

public class SessionId {
	
	public static final int SESSION_ID_SIZE = 16;

	private byte[] value;
	
	public SessionId() {
		value = new byte[SESSION_ID_SIZE];
	}
	
	public SessionId(byte[] bytes) {
		this();
		if(bytes.length != SESSION_ID_SIZE) {
			throw new IllegalArgumentException("byte[] provided was not of correct size");
		}
		
		System.arraycopy(bytes, 0, value, 0, bytes.length);
	}
	
	public byte[] getBytes() {
		return value;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof SessionId)) {
			return false;
		}
		
		SessionId sessionId = (SessionId)o;
		for(int i=0; i < SESSION_ID_SIZE; i++) {
			if(value[i] != sessionId.value[i]) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return getInt(value, 0) ^ getInt(value, 4) ^ getInt(value, 8) ^ getInt(value, 12);
	}
	
	private int getInt(byte[] bytes, int offset) {
		return  bytes[offset] << 24
		      | bytes[offset+1] << 16
		      | bytes[offset+2] << 8
		      | bytes[offset+3];
	}
	
	@Override
	public String toString() {
		return IOUtil.printByteArray(value);
	}
}
