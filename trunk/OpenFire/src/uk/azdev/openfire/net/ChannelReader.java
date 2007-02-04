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

import static uk.azdev.openfire.net.util.IOUtil.printByteArray;
import static uk.azdev.openfire.net.util.IOUtil.readUnsignedShort;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.MessageFactory;
import uk.azdev.openfire.net.messages.UnknownInt8MapBasedMessage;
import uk.azdev.openfire.net.messages.UnknownStringMapBasedMessage;
import uk.azdev.openfire.net.util.IOUtil;


public class ChannelReader {
	
	private static final int MAX_MESSAGE_SIZE = (1 << 16) - 1;
	private static final int HEADER_SIZE = 4;
	
	private ReadableByteChannel channel;
	private ByteBuffer messageBuffer;
	private Logger logger;
	private MessageFactory messageFactory;
	private List<MessageListener> listenerList;
	
	
	public ChannelReader(ReadableByteChannel channelToRead) {
		this(channelToRead, Logger.getAnonymousLogger());
	}
	
	public ChannelReader(ReadableByteChannel channelToRead, Logger logger) {
		this.channel = channelToRead;
		this.logger = logger;
		messageBuffer = IOUtil.createBuffer(MAX_MESSAGE_SIZE-HEADER_SIZE);
		messageFactory = createMessageFactory();
		listenerList = new LinkedList<MessageListener>();
	}
	
	public void addMessageListener(MessageListener listener) {
		listenerList.add(listener);
	}
	
	public void readChannel() {
		int numMessages = 0;
		int numFailed = 0;
		try {
			int messageSize;
			while((messageSize = readUnsignedShort(channel)) != -1) {
				logger.finer("next message size: " + messageSize);
				
				int messageType = readUnsignedShort(channel);
				if(messageType == -1) {
					logger.warning("not enough bytes in stream to determine message type!");
					break;
				}
				
				logger.finer("next message type: " + messageType);
				
				int messageContentsSize = messageSize - HEADER_SIZE;
				readMessageContents(messageContentsSize);
				
				numMessages++;
				
				if(messageFactory.isKnownMessageType(messageType)) {
					IMessage message = messageFactory.createMessage(messageType);
					message.readMessageContent(messageBuffer);
					logger.info("successfully read message of type \"" + messageType + "\"");
					logger.finest(message.toString());
					dispatchToListeners(message);
				} else {
					// attempt to read as a generic message
					if(!attemptStringBasedMessageRead(messageType) 
					   && !attemptInt8BasedMessageRead(messageType)) {
						logger.warning("Unknown message of type \"" + messageType + "\" which is not string or int8 based found in stream");
						printBuffer(messageBuffer);
						numFailed++;
					}
				}
			}
			
		} catch(IOException e) {
			logger.severe("IO Exception occurred while reading channel");
		}
		
		System.out.println("number of messages: " + numMessages);
		System.out.println("number failed: " + numFailed);
	}

	private void dispatchToListeners(IMessage message) {
		for(MessageListener listener : listenerList) {
			try {
				listener.messageReceived(message);
			} catch(Exception e) {
				logger.severe("message listener threw exception while processing message");
				logger.severe(e.getMessage());
			}
		}
	}

	private boolean attemptStringBasedMessageRead(int messageType) {
		try {
			messageBuffer.rewind();
			UnknownStringMapBasedMessage message = new UnknownStringMapBasedMessage();
			message.readMessageContent(messageBuffer);
			message.setMessageId(messageType);
			logger.info("read unknown string based message: " + message.toString());
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	private boolean attemptInt8BasedMessageRead(int messageType) {
		try {
			messageBuffer.rewind();
			UnknownInt8MapBasedMessage message = new UnknownInt8MapBasedMessage();
			message.readMessageContent(messageBuffer);
			message.setMessageId(messageType);
			logger.info("read unknown int8 based message: " + message.toString());
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	private void readMessageContents(int messageContentsSize) throws IOException {
		messageBuffer.rewind();
		messageBuffer.limit(messageContentsSize);
		int bytesRead = channel.read(messageBuffer);
		if(bytesRead != messageContentsSize) {
			throw new IOException("incomplete message in stream!");
		}
		
		messageBuffer.rewind();
	}
	
	private void printBuffer(ByteBuffer buffer) {
		logger.finest("Message contents:");
		
		// strip the first two bytes, which are the type
		byte[] bytes = new byte[buffer.remaining()];
		buffer.get(bytes);
		logger.finest(printByteArray(bytes));
	}
	
	protected MessageFactory createMessageFactory() {
		return new MessageFactory();
	}

	public static void main(String[] args) throws IOException {
		if(args.length == 0) {
			System.out.println("Missing argument: message data file");
			return;
		}
		
		int numBytesToStrip = 0;
		if(args.length > 1) {
			 numBytesToStrip = Integer.parseInt(args[1]);
		}
		
		Logger logger = Logger.getLogger("myLogger");
		logger.setLevel(Level.FINEST);
		
		try {
			InputStream messageStream = getResource(args[0]);
			ReadableByteChannel channel = Channels.newChannel(messageStream);
			stripBytes(channel, numBytesToStrip);
			ChannelReader reader = new ChannelReader(channel, logger);
			reader.addMessageListener(new MessagePrintingListener());
			reader.readChannel();
		} catch(FileNotFoundException e) {
			System.out.println("Error: unable to file message data file");
		}
	}
	
	private static InputStream getResource(String path) throws FileNotFoundException {
		return new FileInputStream(path);
	}
	
	private static void stripBytes(ReadableByteChannel channel, int numBytesToStrip) throws IOException {
		ByteBuffer buffer = IOUtil.createBuffer(numBytesToStrip);
		int numRead = channel.read(buffer);
		if(numRead != numBytesToStrip) {
			throw new RuntimeException("number of bytes to strip exceeds channel length");
		}
	}
}
