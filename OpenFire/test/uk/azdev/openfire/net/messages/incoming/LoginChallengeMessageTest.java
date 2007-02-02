package uk.azdev.openfire.net.messages.incoming;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.net.util.IOUtil;
import uk.azdev.openfire.testutil.TestUtils;

public class LoginChallengeMessageTest {

	private static String EXPECTED_SALT = "cfb67a640fc290e41ce5e41bc6b5ad22847af2a3";
	
	private LoginChallengeMessage message;

	@Before
	public void setUp() {
		message = new LoginChallengeMessage();
	}
	
	@Test
	public void testNewInstance() {
		assertTrue(message.newInstance() instanceof LoginChallengeMessage);
	}

	@Test
	public void testReadMessageContent() throws IOException {
		ByteBuffer buffer = TestUtils.getByteBufferForResource(this.getClass(), "loginchallenge.sampledata");
		message.readMessageContent(buffer);
		assertEquals(EXPECTED_SALT, message.getSalt());
	}

	@Test
	public void testWriteMessageContent() throws IOException {
		message.setSalt(EXPECTED_SALT);
		ByteBuffer buffer = IOUtil.createBuffer(message.getMessageContentSize());
		message.writeMessageContent(buffer);
		buffer.rewind();
		
		byte[] expectedBytes = TestUtils.getByteArrayForResource(this.getClass(), "loginchallenge.sampledata");
		TestUtils.checkBytes(expectedBytes, buffer);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetSaltOfWrongSize() {
		message.setSalt("tooshort");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetNullSalt() {
		message.setSalt(null);
	}

}
