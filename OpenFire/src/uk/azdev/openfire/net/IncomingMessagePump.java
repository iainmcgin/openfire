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
import java.nio.channels.AsynchronousCloseException;
import java.util.LinkedList;
import java.util.List;

import uk.azdev.openfire.net.messages.IMessage;

public class IncomingMessagePump extends ConnectionThread {

	private List<MessageListener> messageListeners;
	private ChannelReader reader;
	
	public IncomingMessagePump(ConnectionStateListener listener) {
		super(listener);
		
		messageListeners = new LinkedList<MessageListener>();
	}
	
	public void setReader(ChannelReader reader) {
		this.reader = reader;
	}
	
	@Override
	public void doProcessing() throws IOException {
		try {
			IMessage message;
			while((message = reader.readMessage()) != null) {
				dispatchToListeners(message);
			}
		} catch(AsynchronousCloseException e) {
			if(!plannedStop) {
				throw e;
			}
		}
	}
	
	private void dispatchToListeners(IMessage message) {
		synchronized(messageListeners) {
			for(MessageListener messageListener : messageListeners) {
				messageListener.messageReceived(message);
			}
		}
	}
	
	public void addListener(MessageListener messageListener) {
		synchronized(messageListeners) {
			messageListeners.add(messageListener);
		}
	}
	
	public void removeListener(MessageListener messageListener) {
		synchronized(messageListeners) {
			messageListeners.remove(messageListener);
		}
	}
}
