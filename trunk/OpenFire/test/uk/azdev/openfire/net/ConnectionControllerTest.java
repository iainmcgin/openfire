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
package uk.azdev.openfire.net;


import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.incoming.LoginChallengeMessage;
import uk.azdev.openfire.net.messages.incoming.LoginChallengeMessageTest;
import uk.azdev.openfire.net.messages.incoming.LoginSuccessMessage;
import uk.azdev.openfire.net.messages.incoming.LoginSuccessMessageTest;
import uk.azdev.openfire.net.messages.incoming.UserSessionIdListMessage;
import uk.azdev.openfire.net.messages.incoming.UserSessionIdListMessageTest;
import uk.azdev.openfire.net.messages.outgoing.ClientInformationMessage;
import uk.azdev.openfire.net.messages.outgoing.ClientInformationMessageTest;
import uk.azdev.openfire.net.messages.outgoing.ClientVersionMessage;
import uk.azdev.openfire.net.messages.outgoing.ClientVersionMessageTest;
import uk.azdev.openfire.net.messages.outgoing.LoginRequestMessage;
import uk.azdev.openfire.net.messages.outgoing.LoginRequestMessageTest;

@RunWith(JMock.class)
public class ConnectionControllerTest {

	Mockery mockery = new JUnit4Mockery();
	ConnectionController controller;
	
	@Test
	public void testSendMessages() throws IOException, InterruptedException {
		MessageReceivingServerEmulator emulator = new MessageReceivingServerEmulator();
		int boundPort = emulator.getBoundPort();
		
		emulator.start();
		
		controller = new ConnectionController("127.0.0.1", boundPort);
		controller.start();
		
		controller.sendMessage(ClientInformationMessageTest.createTestMessage());
		controller.sendMessage(ClientVersionMessageTest.createTestMessage());
		controller.sendMessage(LoginRequestMessageTest.createTestMessage());
		
		controller.stop();
		emulator.stop();
		
		List<IMessage> receivedMessages = emulator.getReceivedMessages();
		assertEquals(3, receivedMessages.size());
		assertTrue(receivedMessages.get(0) instanceof ClientInformationMessage);
		ClientInformationMessageTest.verifyMessage((ClientInformationMessage) receivedMessages.get(0));
		
		assertTrue(receivedMessages.get(1) instanceof ClientVersionMessage);
		ClientVersionMessageTest.verifyMessage((ClientVersionMessage) receivedMessages.get(1));
		
		assertTrue(receivedMessages.get(2) instanceof LoginRequestMessage);
		LoginRequestMessageTest.verifyMessage((LoginRequestMessage) receivedMessages.get(2));
		
		assertFalse(emulator.didErrorOccur());
	}
	
	@Test
	public void testReceiveMessages() throws UnknownHostException, IOException, InterruptedException {
		MessageSendingServerEmulator emulator = new MessageSendingServerEmulator();
		int boundPort = emulator.getBoundPort();
		
		emulator.addMessageToSend(LoginChallengeMessageTest.createTestMessage());
		emulator.addMessageToSend(LoginSuccessMessageTest.createTestMessage());
		emulator.addMessageToSend(UserSessionIdListMessageTest.createTestMessage());
		
		emulator.start();
		
		Object messageMutex = new Object();
		
		TestMessageListener listener = new TestMessageListener();
		listener.latch = new CountDownLatch(3);
		listener.notifyTarget = messageMutex;
		
		controller = new ConnectionController("127.0.0.1", boundPort);
		
		controller.addStateListener(listener);
		controller.start();
		
		listener.latch.await();
		controller.stop();
		emulator.stop();
		
		assertEquals(3, listener.receivedMessages.size());
		assertTrue(listener.receivedMessages.get(0) instanceof LoginChallengeMessage);
		LoginChallengeMessageTest.verifyMessage((LoginChallengeMessage) listener.receivedMessages.get(0));
		assertTrue(listener.receivedMessages.get(1) instanceof LoginSuccessMessage);
		LoginSuccessMessageTest.verifyMessage((LoginSuccessMessage) listener.receivedMessages.get(1));
		assertTrue(listener.receivedMessages.get(2) instanceof UserSessionIdListMessage);
		UserSessionIdListMessageTest.verifyMessage((UserSessionIdListMessage) listener.receivedMessages.get(2));
		
		
		
		assertFalse(emulator.didErrorOccur());
	}
	
	private class TestMessageListener implements ConnectionStateListener {

		public CountDownLatch latch;
		public List<IMessage> receivedMessages = new ArrayList<IMessage>();
		public Object notifyTarget;
		
		public void messageReceived(IMessage message) {
			synchronized (receivedMessages) {
				receivedMessages.add(message);
				latch.countDown();
			}
			
		}

		public void connectionError(Exception e) {
			// not relevant for this test
		}
		
		public void loginFailed() {
			// not relevant for this test
		}
		
	}
}
