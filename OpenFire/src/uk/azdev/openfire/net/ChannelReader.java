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

import static uk.azdev.openfire.net.util.IOUtil.readUnsignedShort;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.MessageFactory;
import uk.azdev.openfire.net.messages.UnknownInt8MapBasedMessage;
import uk.azdev.openfire.net.messages.UnknownStringMapBasedMessage;
import uk.azdev.openfire.net.util.IOUtil;

public class ChannelReader {
	
	private ByteBuffer messageBuffer;
	private ReadableByteChannel channel;
	private MessageFactory messageFactory;

	public ChannelReader(ReadableByteChannel inputChannel) {
		this.channel = inputChannel;
		messageBuffer = IOUtil.createBuffer(ProtocolConstants.MAX_MESSAGE_SIZE - ProtocolConstants.HEADER_SIZE);
		messageFactory = createMessageFactory();
	}
	
	public void skipOpeningStatement() throws IOException {
		
		messageBuffer.clear();
		messageBuffer.limit(4);
		int numRead = channel.read(messageBuffer);
		if(numRead != 4) {
			throw new IOException("Not enough bytes in stream for opening statement");
		}
		
		messageBuffer.flip();
		
		if(!IOUtil.nextBytesMatchArray(messageBuffer, ProtocolConstants.CLIENT_OPENING_STATEMENT)) {
			throw new IOException("Opening statement did not match expected");
		}
		
	}
	
	public IMessage readMessage() throws IOException {
		
		int messageSize = readUnsignedShort(channel);
		int messageType = readUnsignedShort(channel);
		if(messageSize == -1 || messageType == -1) {
			return null;
		}
		
		int messageContentsSize = messageSize - ProtocolConstants.HEADER_SIZE;
		
		if(!readMessageContents(messageContentsSize)) {
			return null;
		}
		
		if(messageFactory.isKnownMessageType(messageType)) {
			IMessage message = messageFactory.createMessage(messageType);
			message.readMessageContent(messageBuffer);
			return message;
		}
		
		// attempt to read as a generic message
		IMessage unknownMessage = attemptUnknownMessageRead(messageType);
		if(unknownMessage == null) {
			throw new IOException("Unparseable message found on stream");
		}
		
		return unknownMessage;
	}
	
	private boolean readMessageContents(int messageContentsSize) throws IOException {
		messageBuffer.rewind();
		messageBuffer.limit(messageContentsSize);
		int bytesRead = channel.read(messageBuffer);
		if(bytesRead != messageContentsSize) {
			return false;
		}
		
		messageBuffer.rewind();
		return true;
	}
	
	private IMessage attemptUnknownMessageRead(int messageType) {
		IMessage message = attemptStringBasedMessageRead(messageType);
		if(message == null) {
			message = attemptInt8BasedMessageRead(messageType);
		}
		
		return message;
	}
	
	private UnknownStringMapBasedMessage attemptStringBasedMessageRead(int messageType) {
		try {
			messageBuffer.rewind();
			UnknownStringMapBasedMessage message = new UnknownStringMapBasedMessage();
			message.readMessageContent(messageBuffer);
			message.setMessageId(messageType);
			return message;
		} catch(Exception e) {
			return null;
		}
	}
	
	private UnknownInt8MapBasedMessage attemptInt8BasedMessageRead(int messageType) {
		try {
			messageBuffer.rewind();
			UnknownInt8MapBasedMessage message = new UnknownInt8MapBasedMessage();
			message.readMessageContent(messageBuffer);
			message.setMessageId(messageType);
			return message;
		} catch(Exception e) {
			return null;
		}
	}
	
	public void close() throws IOException {
		channel.close();
	}
	
	protected MessageFactory createMessageFactory() {
		return new MessageFactory();
	}
}
