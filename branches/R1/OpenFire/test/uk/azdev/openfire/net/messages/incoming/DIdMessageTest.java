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
package uk.azdev.openfire.net.messages.incoming;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.common.DId;
import uk.azdev.openfire.testutil.TestUtils;

public class DIdMessageTest {

	private DIdMessage message;
	private static final byte[] EXPECTED_DID_BYTES = { 0x00, 0x01, 0x02, 0x03, 0x04, 
		                                               0x05, 0x06, 0x07, 0x08, 0x09, 
		                                               0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 
		                                               0x0F, 0x10, 0x11, 0x12, 0x13, 
		                                               0x14 };
	
	@Before
	public void setUp() throws Exception {
		message = new DIdMessage();
	}

	@Test
	public void testGetMessageId() {
		assertEquals(DIdMessage.DID_MESSAGE_TYPE_ID, message.getMessageId());
	}

	/**
	 * Test method for {@link uk.azdev.openfire.net.messages.incoming.DIdMessage#newInstance()}.
	 */
	@Test
	public void testNewInstance() {
		assertTrue(message.newInstance().getClass() == DIdMessage.class);
	}

	@Test
	public void testReadMessageContent() throws IOException {
		ByteBuffer buffer = TestUtils.getByteBufferForResource(this.getClass(), "did.sampledata");
		message.readMessageContent(buffer);
		assertEquals(new DId(EXPECTED_DID_BYTES), message.getDId());
	}

	@Test
	public void testWriteMessageContent() throws IOException {
		message.setDId(new DId(EXPECTED_DID_BYTES));
		TestUtils.checkMessageOutput(message, DIdMessageTest.class, "did.sampledata");
	}
	
	@Test
	public void testToString() {
		message.setDId(new DId(EXPECTED_DID_BYTES));
		
		assertEquals("DID message\n\tDID:<00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F 10 11 12 13 14>", message.toString());
	}

}
