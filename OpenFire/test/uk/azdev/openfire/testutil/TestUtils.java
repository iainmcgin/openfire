package uk.azdev.openfire.testutil;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

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
	
	private static InputStream getTestResource(Class<?> testClass, String resourceName) {
		return testClass.getResourceAsStream("testResources/" + resourceName);
	}
	
	private static byte[] readAllAvailable(InputStream stream) throws IOException {
		byte[] bytes = new byte[stream.available()];
		stream.read(bytes);
		stream.close();
		return bytes;
	}
}
