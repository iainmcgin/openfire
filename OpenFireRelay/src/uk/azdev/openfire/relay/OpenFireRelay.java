package uk.azdev.openfire.relay;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import uk.azdev.openfire.ConnectionEventListener;
import uk.azdev.openfire.XFireConnection;
import uk.azdev.openfire.common.OpenFireConfiguration;
import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.conversations.Conversation;
import uk.azdev.openfire.friendlist.Friend;
import uk.azdev.openfire.friendlist.FriendsList;

public class OpenFireRelay implements ConnectionEventListener {

	private XFireConnection connection;
	private CountDownLatch exitLatch;
	
	public OpenFireRelay(OpenFireConfiguration config) {
		exitLatch = new CountDownLatch(1);
		connection = new XFireConnection(config);
	}
	
	private void start() throws UnknownHostException, IOException {
		connection.addListener(this);
		connection.blockingConnect();
	}
	
	public void waitForExit() throws InterruptedException {
		exitLatch.await();
	}

	public void connectionError() {
		System.err.println("A connection error occurred. The program will now terminate");
		exitLatch.countDown();
	}

	public void conversationUpdate(SessionId sessionId) {
		System.out.println("message received from " + sessionId);
		FriendsList list = connection.getFriendList();
		Set<Friend> myFriends = list.getMyFriends();
		
		System.out.println("message was from " + connection.getFriendList().getOnlineFriend(sessionId).getDisplayName());
		
		Conversation sourceConv = connection.getConversation(sessionId);
		String message = sourceConv.getLastMessage();
		
		for(Friend f : myFriends) {
			if(!f.isOnline() || f.getSessionId().equals(sessionId)) {
				continue;
			}
			
			System.out.println("Forwarding message to " + f.getDisplayName());
			
			Conversation conv = connection.getConversation(f.getSessionId());
			conv.sendMessage(message);
		}
	}

	public void disconnected() {
		System.out.println("Client disconnected");
	}

	public void friendsListUpdated() {
		// don't need to do anything
	}

	public void internalError(Exception e) {
		System.err.println("An internal error occurred. The program will now terminate");
		e.printStackTrace();
		exitLatch.countDown();
	}

	public void loginFailed() {
		System.err.println("Incorrect authentication details provided.");
		exitLatch.countDown();
	}
	
	public static void main(String[] args) {
		OpenFireConfiguration config = new OpenFireConfiguration();
		
		if(args.length < 2) {
			System.out.println("Usage: OpenFireRelay <username> <password>");
			return;
		}
		
		config.setUsername(args[0]);
		config.setPassword(args[1]);
		
		OpenFireRelay relay = new OpenFireRelay(config);
		try {
			relay.start();
		} catch (UnknownHostException e) {
			System.err.println("Unable to resolve host: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("I/O error occurred: " + e.getMessage());
			e.printStackTrace();
		}
		
		try {
			relay.waitForExit();
		} catch (InterruptedException e) {
			System.err.println("Main thread unexpectedly terminated");
		}
	}
}
