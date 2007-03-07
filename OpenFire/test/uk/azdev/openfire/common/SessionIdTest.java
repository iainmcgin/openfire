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

import static org.junit.Assert.*;

import org.junit.Test;

public class SessionIdTest {

	private static final byte[] TWO_TO_THE_POWER_127_ARRAY;
	private static final byte[] COUNTED_ARRAY;
	
	static {
		TWO_TO_THE_POWER_127_ARRAY = new byte[SessionId.SESSION_ID_SIZE];
		TWO_TO_THE_POWER_127_ARRAY[0] = (byte)0x80;
		
		COUNTED_ARRAY = new byte[SessionId.SESSION_ID_SIZE];
		for(int i=0; i < SessionId.SESSION_ID_SIZE; i++) {
			COUNTED_ARRAY[i] = (byte)i;
		}
	}
	
	
	@Test
	public void testCreateDefaultSessionId() {
		SessionId sessionId = new SessionId();
		byte[] bytes = sessionId.getBytes();
		assertEquals(SessionId.SESSION_ID_SIZE, bytes.length);
		for(int i=0; i < SessionId.SESSION_ID_SIZE; i++) {
			assertEquals((byte)0, bytes[i]);
		}
	}
	
	@Test
	public void testGetBytes() {
		SessionId sessionId = new SessionId(COUNTED_ARRAY);
		byte[] bytes = sessionId.getBytes();
		assertEquals(SessionId.SESSION_ID_SIZE, bytes.length);
		for(int i=0; i < SessionId.SESSION_ID_SIZE; i++) {
			assertEquals((byte)i, bytes[i]);
		}
	}
	
	@Test
	public void testEquals() {
		SessionId sid1 = new SessionId(TWO_TO_THE_POWER_127_ARRAY);
		SessionId sid2 = new SessionId(TWO_TO_THE_POWER_127_ARRAY);
		SessionId sid3 = new SessionId(COUNTED_ARRAY);
		
		assertTrue(sid1.equals(sid2));
		assertTrue(sid2.equals(sid1));
		assertFalse(sid1.equals(sid3));
		assertFalse(sid1.equals(sid3));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateWithLargeArray() {
		byte[] bytes = new byte[SessionId.SESSION_ID_SIZE+1];
		new SessionId(bytes);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testCreateWithSmallArray() {
		byte[] bytes = new byte[SessionId.SESSION_ID_SIZE-1];
		new SessionId(bytes);
	}

}
