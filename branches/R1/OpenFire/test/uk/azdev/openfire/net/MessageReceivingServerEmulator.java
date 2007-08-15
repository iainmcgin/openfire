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

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

import uk.azdev.openfire.net.messages.IMessage;

public class MessageReceivingServerEmulator extends AbstractServerEmulator {

	private List<IMessage> messagesReceived;
	
	public MessageReceivingServerEmulator() throws IOException {
		super();
		messagesReceived = new LinkedList<IMessage>();
	}
	
	@Override
	public void doWork(SocketChannel channel) throws IOException {
		ChannelReader reader = new ChannelReader(channel);
		reader.skipOpeningStatement();
		readMessages(reader);
	}
	
	private void readMessages(ChannelReader reader) throws IOException {
		IMessage readMessage;
		while((readMessage = reader.readMessage()) != null) {
			messagesReceived.add(readMessage);
		}
	}
	
	public List<IMessage> getReceivedMessages() {
		return messagesReceived;
	}

}
