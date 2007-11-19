package uk.azdev.openfire.net;

import org.junit.Test;


public class ProtocolConstantsTest {

	@Test(expected=RuntimeException.class)
	public void testCannotCreateInstance() {
		new ProtocolConstants();
	}
	
}
