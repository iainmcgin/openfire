package uk.azdev.openfire;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.net.IMessageSender;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.outgoing.KeepaliveMessage;

public class KeepaliveTimerTest {
	
	private FakeMessageSender sender;
	
	@Before
	public void setUp() {
		sender = new FakeMessageSender();
	}
	
	@Test(timeout=500)
	public void testTimer() throws InterruptedException {
		KeepaliveTimer timer = new KeepaliveTimer(sender, 50);
		timer.start();
		Thread.sleep(160);
		timer.stop();
		assertEquals(3, sender.sendCount.get());
	}

	@Test(timeout=500)
	public void testResetTimer() throws InterruptedException {
		KeepaliveTimer timer = new KeepaliveTimer(sender, 100);
		timer.start();
		Thread.sleep(150);
		timer.resetTimer();
		Thread.sleep(50);
		timer.stop();
		assertEquals(1, sender.sendCount.get());
	}
	
	private class FakeMessageSender implements IMessageSender {
		
		public AtomicInteger sendCount = new AtomicInteger();
		
		public void sendMessage(IMessage message) {
			assertTrue(message instanceof KeepaliveMessage);
			sendCount.incrementAndGet();
		}
		
	}

}
