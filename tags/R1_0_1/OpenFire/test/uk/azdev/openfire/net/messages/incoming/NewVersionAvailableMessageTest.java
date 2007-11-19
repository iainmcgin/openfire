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

import uk.azdev.openfire.common.XFireUpdate;
import uk.azdev.openfire.testutil.TestUtils;

public class NewVersionAvailableMessageTest {

	private NewVersionAvailableMessage message;
	
	@Before
	public void setUp() throws Exception {
		message = new NewVersionAvailableMessage();
	}

	@Test
	public void testReadMessageContent() throws IOException {
		ByteBuffer buffer = TestUtils.getByteBufferForResource(this.getClass(), "newversion.sampledata");
		message.readMessageContent(buffer);
		
		assertEquals(1, message.getNewVersionsList().size());
		XFireUpdate update = message.getNewVersionsList().get(0);
		assertEquals(75L, update.getVersionNum());
		assertEquals("http://test.com/abc/", update.getUpdateDownloadUrl());
		assertEquals(1L, update.getCommand());
		assertEquals(2L, update.getFileId());
		assertEquals(0x78563412L, message.getFlags());
	}

	@Test
	public void testWriteMessageContent() throws IOException {
		message.addNewVersion(new XFireUpdate(75L, "http://test.com/abc/", 1L, 2L));
		message.setFlags(0x78563412L);
		TestUtils.checkMessageOutput(message, this.getClass(), "newversion.sampledata");
	}

}
