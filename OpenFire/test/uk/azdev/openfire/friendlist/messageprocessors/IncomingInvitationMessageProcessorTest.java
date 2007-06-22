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

import static org.junit.Assert.*;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import uk.azdev.openfire.ConnectionEventListener;
import uk.azdev.openfire.common.Invitation;
import uk.azdev.openfire.net.messages.incoming.IncomingInvitationMessage;

public class IncomingInvitationMessageProcessorTest {
	
	@Test
	public void testProcessMessage() {
		JUnit4Mockery context = new JUnit4Mockery();
		final ConnectionEventListener listener = context.mock(ConnectionEventListener.class);
		
		final Invitation inv1 = new Invitation("alice", "Alice", "Hello! Let me talk to you");
		final Invitation inv2 = new Invitation("bob", "Bob", "Ahoy there!");
		final Invitation inv3 = new Invitation("carol", "Carol", "Y HELO THAR");
		IncomingInvitationMessage message = new IncomingInvitationMessage();
		message.addInvite(inv1);
		message.addInvite(inv2);
		message.addInvite(inv3);
		
		context.checking(new Expectations() {{
			one(listener).inviteReceived(with(same(inv1)));
			one(listener).inviteReceived(with(same(inv2)));
			one(listener).inviteReceived(with(same(inv3)));
		}});
		
		IncomingInvitationMessageProcessor processor = new IncomingInvitationMessageProcessor(listener);
		processor.processMessage(message);
		context.assertIsSatisfied();
	}

}
