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

import static org.junit.Assert.*;
import static uk.azdev.openfire.testutil.TestUtils.*;

import java.nio.ByteBuffer;

import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.net.attrvalues.SessionIdAttributeValue;
import uk.azdev.openfire.net.util.IOUtil;



public class SessionIdAttributeValueTest {

	private static final byte[] INITIAL_SID
		= { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
	
	// SID pre-encoded
	private static final byte[] SAMPLE_ATTR 
		= { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 
			0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F };
	
	private SessionIdAttributeValue value;
	
	@Before
	public void setUp() {
		value = new SessionIdAttributeValue();
	}
	
	@Test
	public void testSetValue() {
		checkArray(INITIAL_SID, value.getSessionId(), "initial value incorrect");
		
		value.setSessionId(SAMPLE_ATTR);
		assertNotSame(value.getSessionId(), SAMPLE_ATTR);
		checkArray(SAMPLE_ATTR, value.getSessionId(), "set value incorrect");
	}

	@Test
	public void testGetSize() {
		assertEquals(16, value.getSize());
	}

	@Test
	public void testGetTypeId() {
		assertEquals(SessionIdAttributeValue.TYPE_ID, value.getTypeId());
	}

	@Test
	public void testReadValue() {
		ByteBuffer buffer = IOUtil.createBuffer(SAMPLE_ATTR.length);
		
		buffer.put(SAMPLE_ATTR);
		buffer.rewind();
		
		value.readValue(buffer);
		checkArray(SAMPLE_ATTR, value.getSessionId(), "read SID does not match");
	}

	@Test
	public void testWriteValue() {
		value.setSessionId(SAMPLE_ATTR);
		ByteBuffer buffer = IOUtil.createBuffer(value.getSize());
		value.writeValue(buffer);
		
		buffer.rewind();
		SessionIdAttributeValue readValue = new SessionIdAttributeValue();
		readValue.readValue(buffer);
		
		assertNotSame(readValue.getSessionId(), SAMPLE_ATTR);
		checkArray(SAMPLE_ATTR, readValue.getSessionId(), "read after write does not match");
	}

	@Test
	public void testNewInstance() {
		assertTrue(value.newInstance() instanceof SessionIdAttributeValue);
	}
	
	@Test
	public void testToString() {
		value.setSessionId(SAMPLE_ATTR);
		assertEquals("SID:<00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F>", value.toString());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWrongSizeSid() {
		value.setSessionId(new byte[25]);
	}
	
}
