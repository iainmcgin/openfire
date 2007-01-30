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
package uk.azdev.openfire.net.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;

public class IOUtil {

	private static final Charset CHARACTER_ENCODING = Charset.forName("UTF-8");
	private static ByteBuffer shortBuffer = createBuffer(2);
	
	IOUtil() {
		throw new RuntimeException("IOUtil is not meant to be instantiated");
	}
	
	public static int readUnsignedByte(ByteBuffer buffer) {
		return convertByteToUnsigned(buffer.get());
	}
	
	public static int convertByteToUnsigned(byte b) {
		return b & 0xFF;
	}
	
	public static int readUnsignedShort(ReadableByteChannel channel) throws IOException {
		shortBuffer.rewind();
		int numRead = channel.read(shortBuffer);
		
		if(numRead != 2) {
			return -1;
		}
		
		shortBuffer.rewind();
		return readUnsignedShort(shortBuffer);
	}
	
	public static int readUnsignedShort(ByteBuffer buffer) {
		return buffer.getShort() & 0xFFFF;
	}
	
	public static long readUnsignedInt(ByteBuffer buffer) {
		return buffer.getInt() & 0xFFFFFFFFL;
	}
	
	public static int getEncodedStringSize(String str) {
		return encodeString(str).length;
	}
	
	public static byte[] encodeString(String str) {
		return str.getBytes(CHARACTER_ENCODING);
	}
	
	public static String decodeString(byte[] stringBytes) {
		return new String(stringBytes, CHARACTER_ENCODING);
	}
	
	public static String printByteArray(byte[] bytes) {
		return printByteArray(bytes, true, true);
	}
	
	public static String printByteArray(byte[] bytes, boolean spacesBetweenBytes, boolean upperCase) {
		StringBuffer buffer = new StringBuffer(bytes.length * 3);
		
		for(int i=0; i < bytes.length; i++) {
			int asUnsigned= convertByteToUnsigned(bytes[i]);
			String hexString = Integer.toHexString(asUnsigned);
			if(hexString.length() == 1) {
				hexString = "0" + hexString;
			}
			
			if(upperCase) {
				hexString = hexString.toUpperCase();
			}
			buffer.append(hexString);
			
			if(spacesBetweenBytes && i < bytes.length - 1) {
				buffer.append(' ');
			}
		}
		
		return buffer.toString();
	}
	
	public static ByteBuffer createBuffer(int size) {
		ByteBuffer buffer = ByteBuffer.allocate(size);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		
		return buffer;
	}
	
}
