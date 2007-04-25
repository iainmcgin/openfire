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
package uk.azdev.openfire.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;

import uk.azdev.openfire.net.messages.IMessage;

public class ConnectionController implements ConnectionStateListener {

	private SocketChannel channel;
	private IncomingMessagePump inPump;
	private OutgoingMessagePump outPump;
	
	public ConnectionController() {
		inPump = new IncomingMessagePump(this);
	}
	
	public void start() throws UnknownHostException, IOException {
		start(ProtocolConstants.XFIRE_SERVER_NAME, ProtocolConstants.XFIRE_SERVER_PORT);
		
	}
	
	public void start(String hostName, int port) throws UnknownHostException, IOException {
		channel = SocketChannel.open(new InetSocketAddress(hostName, port));
		channel.configureBlocking(true);
		
		ChannelReader reader = new ChannelReader(channel);
		inPump.setReader(reader);
		
		ChannelWriter writer = new ChannelWriter(channel);
		outPump = new OutgoingMessagePump(writer, this);
		
		inPump.start("InPump");
		outPump.start("OutPump");
	}

	public void stop() throws InterruptedException, IOException {
		outPump.waitForEmptyQueue();
		inPump.setPlannedStop();
		outPump.setPlannedStop();
		channel.close();
		inPump.waitForExit();
		outPump.waitForExit();
	}
	
	public void addMessageListener(MessageListener listener) {
		inPump.addListener(listener);
	}
	
	public void removeMessageListener(MessageListener listener) {
		inPump.removeListener(listener);
	}
	
	public void sendMessage(IMessage message) {
		outPump.sendMessage(message);
	}

	public void connectionError(Exception e) {
		System.err.println("Exception occurred on connection");
		e.printStackTrace();
	}
}
