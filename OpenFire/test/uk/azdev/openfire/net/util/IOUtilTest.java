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

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.junit.Test;

import uk.azdev.openfire.net.util.IOUtil;



public class IOUtilTest {
	
	@Test(expected=RuntimeException.class)
	public void testCannotCreateInstance() {
		new IOUtil();
	}
	
	@Test
	public void testReadUnsignedByte() {
		ByteBuffer buffer = IOUtil.createBuffer(1);
		buffer.put((byte)250);
		buffer.flip();
		assertEquals(250, IOUtil.readUnsignedByte(buffer));
	}
	
	@Test
	public void testReadUnsignedShort() {
		ByteBuffer buffer = IOUtil.createBuffer(2);
		buffer.putShort((short)40000);
		buffer.flip();
		assertEquals(40000, IOUtil.readUnsignedShort(buffer));
	}
	
	@Test
	public void testReadUnsignedShortFromChannel() throws IOException {
		byte[] testBytes = { 0x10, 0x00 };
		ReadableByteChannel channel = createChannel(testBytes);
		assertEquals(16, IOUtil.readUnsignedShort(channel));
	}
	
	@Test
	public void testReadUnsignedShort_tooFewBytes() throws IOException {
		byte[] testBytes = { 0x10 };
		ReadableByteChannel channel = createChannel(testBytes);
		assertEquals(-1, IOUtil.readUnsignedShort(channel));
	}
	
	@Test
	public void testReadUnsignedShort_noBytes() throws IOException {
		byte[] testBytes = { };
		ReadableByteChannel channel = createChannel(testBytes);
		assertEquals(-1, IOUtil.readUnsignedShort(channel));
	}

	private ReadableByteChannel createChannel(byte[] testBytes) {
		ByteArrayInputStream in = new ByteArrayInputStream(testBytes);
		return Channels.newChannel(in);
	}
	
	@Test
	public void testReadUnsignedInt() {
		ByteBuffer buffer = IOUtil.createBuffer(4);
		buffer.putInt((int)3000000000L);
		buffer.flip();
		assertEquals(3000000000L, IOUtil.readUnsignedInt(buffer));
	}
	
	@Test
	public void testEncodeAndDecodeString() {
		byte[] encoded = IOUtil.encodeString("testing 1 2 3");
		assertEquals("testing 1 2 3", IOUtil.decodeString(encoded));
	}
	
	@Test
	public void testNextBytesMatchArray() {
		byte[] testArray = new byte[] { 0x01, 0x02, 0x03, 0x04 };
		ByteBuffer buffer = IOUtil.createBuffer(testArray.length);
		buffer.put(testArray);
		buffer.flip();
		
		assertTrue(IOUtil.nextBytesMatchArray(buffer, testArray));
	}
	
	@Test
	public void testNextBytesMatchArray_noMatch() {
		byte[] expectedBytes = new byte[] { 0x04, 0x03, 0x02, 0x01 };
		byte[] testArray = new byte[] { 0x01, 0x02, 0x03, 0x04 };
		ByteBuffer buffer = IOUtil.createBuffer(testArray.length);
		buffer.put(testArray);
		buffer.flip();
		
		assertFalse(IOUtil.nextBytesMatchArray(buffer, expectedBytes));
	}
	
	@Test
	public void testNextBytesMatchArray_withTooFewBytesLeft() {
		byte[] expectedBytes = new byte[] { 0x01, 0x02, 0x03, 0x04 };
		byte[] testArray = new byte[] { 0x01, 0x02, 0x03 };
		ByteBuffer buffer = IOUtil.createBuffer(testArray.length);
		buffer.put(testArray);
		buffer.flip();
		
		assertFalse(IOUtil.nextBytesMatchArray(buffer, expectedBytes));
	}

}
