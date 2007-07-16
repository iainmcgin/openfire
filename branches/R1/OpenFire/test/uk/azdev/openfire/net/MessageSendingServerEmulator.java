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

public class MessageSendingServerEmulator extends AbstractServerEmulator {

	private List<IMessage> messagesToSend;
	
	public MessageSendingServerEmulator() throws IOException {
		super();
		messagesToSend = new LinkedList<IMessage>();
	}
	
	public void addMessageToSend(IMessage message) {
		messagesToSend.add(message);
	}
	
	@Override
	public void doWork(SocketChannel channel) throws IOException {
		ChannelReader reader = new ChannelReader(channel);
		reader.skipOpeningStatement();
		
		ChannelWriter writer = new ChannelWriter(channel);
		writeMessages(writer);
	}
	
	private void writeMessages(ChannelWriter writer) throws IOException {
		for(IMessage message : messagesToSend) {
			writer.writeMessage(message);
		}
	}

}
