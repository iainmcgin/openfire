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

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import uk.azdev.openfire.ConnectionManipulator;
import uk.azdev.openfire.common.OpenFireConfiguration;
import uk.azdev.openfire.common.XFireUpdate;
import uk.azdev.openfire.net.messages.incoming.NewVersionAvailableMessage;


public class NewVersionAvailableMessageProcessorTest {

	JUnit4Mockery context = new JUnit4Mockery();
	
	@Test
	public void testProcessMessage() throws IOException, InterruptedException {
		OpenFireConfiguration config = new OpenFireConfiguration();
		final ConnectionManipulator manipulator = context.mock(ConnectionManipulator.class);
		context.checking(new Expectations() {{
			one(manipulator).reconnect();
		}});
		
		NewVersionAvailableMessageProcessor processor = new NewVersionAvailableMessageProcessor(manipulator, config);
		
		NewVersionAvailableMessage message = new NewVersionAvailableMessage();
		message.addNewVersion(new XFireUpdate(100L, "getItHere", 0L, 0L));
		message.addNewVersion(new XFireUpdate(70L, "getItHere", 0L, 0L));
		message.addNewVersion(new XFireUpdate(130L, "getItHere", 0L, 0L));
		message.addNewVersion(new XFireUpdate(110L, "getItHere", 0L, 0L));
		
		processor.processMessage(message);
		
		assertEquals(130L, config.getShortVersion());
		context.assertIsSatisfied();
	}
}
