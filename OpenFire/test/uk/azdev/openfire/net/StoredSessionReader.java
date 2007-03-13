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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import uk.azdev.openfire.net.util.IOUtil;

/**
 * Standalone test class that can read stored sessions with the xfire server
 * captured using a tool like ethereal.
 */
public class StoredSessionReader {

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
