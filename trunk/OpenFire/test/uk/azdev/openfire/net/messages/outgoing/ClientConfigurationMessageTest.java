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
package uk.azdev.openfire.net.messages.outgoing;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.testutil.TestUtils;

public class ClientConfigurationMessageTest {

	private ClientConfigurationMessage message;
	
	@Before
	public void setUp() throws Exception {
		message = new ClientConfigurationMessage();
	}

	@Test
	public void testGetMessageId() {
		assertEquals(ClientConfigurationMessage.CLIENT_CONFIGURATION_MESSAGE_ID, message.getMessageId());
	}

	@Test
	public void testNewInstance() {
		assertTrue(message.newInstance() instanceof ClientConfigurationMessage);
	}

	@Test
	public void testReadMessageContent() throws IOException {
		ByteBuffer buffer = TestUtils.getByteBufferForResource(this.getClass(), "clientconfiguration.sampledata");
		message.readMessageContent(buffer);
		
		assertEquals("us", message.getLanguage());
		assertEquals("Xfire", message.getActiveSkin());
		assertEquals("default", message.getActiveTheme());
		assertEquals("", message.getPartner());
	}

	@Test
	public void testWriteMessageContent() throws IOException {
		message.setLanguage("us");
		message.setActiveSkin("Xfire");
		message.setActiveTheme("default");
		message.setPartner("");
		
		TestUtils.checkMessageOutput(message, this.getClass(), "clientconfiguration.sampledata");
	}
	
	private static final String EXPECTED_TOSTRING
		= "Client configuration message\n"
		+ "\tLanguage: us\n"
		+ "\tActive skin: Xfire\n"
		+ "\tActive theme: default\n"
		+ "\tPartner: testpartner";
	
	@Test
	public void testToString() {
		message.setLanguage("us");
		message.setActiveSkin("Xfire");
		message.setActiveTheme("default");
		message.setPartner("testpartner");
		
		assertEquals(EXPECTED_TOSTRING, message.toString());
	}

}
