package uk.azdev.openfire.common;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;

import uk.azdev.openfire.net.IMessageSender;
import uk.azdev.openfire.net.messages.outgoing.AcceptInvitationMessage;
import uk.azdev.openfire.net.messages.outgoing.RejectInvitationMessage;

public class ReceivedInvitationTest {

	private JUnit4Mockery context;
	private IMessageSender messageSender;
	private ReceivedInvitation invite;
	
	@Before
	public void setUp() throws Exception {
		context = new JUnit4Mockery();
		messageSender = context.mock(IMessageSender.class);
		invite = new ReceivedInvitation(new Invitation("alice", "Alice", "AHOY"), messageSender);
	}

	@Test
	public void testAccept() {
		final AcceptInvitationMessage expectedMessage = new AcceptInvitationMessage();
		expectedMessage.setPeerUserName("alice");
		
		context.checking(new Expectations() {{
			one(messageSender).sendMessage(with(equal(expectedMessage)));
		}});
		
		invite.accept();
	}

	@Test
	public void testReject() {
		final RejectInvitationMessage expectedMessage = new RejectInvitationMessage();
		expectedMessage.setPeerUserName("alice");
		
		context.checking(new Expectations() {{
			one(messageSender).sendMessage(with(equal(expectedMessage)));
		}});
		
		invite.reject();
	}

}
