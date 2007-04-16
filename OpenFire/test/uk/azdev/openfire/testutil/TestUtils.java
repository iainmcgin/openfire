package uk.azdev.openfire.testutil;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.util.IOUtil;


public class TestUtils {
	
	public static void checkArray(byte[] expected, byte[] actual, String failMessage) {
		assertEquals(failMessage + ": different lengths", expected.length, actual.length);
		
		for(int i=0; i < expected.length; i++) {
			assertEquals(failMessage + ": arrays differ at position " + i, expected[i], actual[i]);
		}
	}
	
	public static void checkArray(byte[] expected, byte[] actual) {
		checkArray(expected, actual, "arrays do not match");
	}
	
	public static void checkBytes(byte[] expected, ByteBuffer actual) {
		byte[] actualBytes = new byte[actual.limit()];
		actual.rewind();
		actual.get(actualBytes);
		
		checkArray(expected, actualBytes);
	}
	
	public static ByteBuffer createBufferFromArray(byte[] bytes) {
		ByteBuffer buffer = IOUtil.createBuffer(bytes.length);
		buffer.put(bytes);
		buffer.rewind();
		
		return buffer;
	}
	
	public static ByteBuffer getByteBufferForResource(Class<?> testClass, String resourceName) throws IOException {
		InputStream resourceStream = getTestResource(testClass, resourceName);
		int resourceSize = resourceStream.available();
		
		ReadableByteChannel resourceChannel = Channels.newChannel(resourceStream);
		ByteBuffer buffer = IOUtil.createBuffer(resourceSize);
		resourceChannel.read(buffer);
		resourceChannel.close();
		buffer.rewind();
		return buffer;
	}
	
	public static byte[] getByteArrayForResource(Class<?> testClass, String resourceName) throws IOException {
		InputStream resourceStream = getTestResource(testClass, resourceName);
		return readAllAvailable(resourceStream);
	}
	
	public static Inet4Address createInet4Address(String addr) throws UnknownHostException {
		return (Inet4Address)InetAddress.getByName(addr);
	}
	
	public static InputStream getTestResource(Class<?> testClass, String resourceName) {
		return testClass.getResourceAsStream("testResources/" + resourceName);
	}
	
	public static ReadableByteChannel getTestResourceAsChannel(Class<?> testClass, String resourceName) throws IOException {
		return Channels.newChannel(getTestResource(testClass, resourceName));
	}
	
	public static byte[] readAllAvailable(InputStream stream) throws IOException {
		byte[] bytes = new byte[stream.available()];
		stream.read(bytes);
		stream.close();
		return bytes;
	}
	
	public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
    
        int length = (int)file.length();
        
        byte[] bytes = new byte[length];
    
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
    
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
    
        is.close();
        return bytes;
    }

	public static void checkMessageOutput(IMessage message, Class<?> testClass, String resourceName) throws IOException {
		ByteBuffer buffer = IOUtil.createBuffer(message.getMessageContentSize());
		message.writeMessageContent(buffer);
		buffer.rewind();
		
		byte[] expectedBytes = TestUtils.getByteArrayForResource(testClass, resourceName);
		checkBytes(expectedBytes, buffer);
	}
}
