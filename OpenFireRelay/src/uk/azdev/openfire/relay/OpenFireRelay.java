package uk.azdev.openfire.relay;

import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import uk.azdev.openfire.ConnectionEventListener;
import uk.azdev.openfire.XFireConnection;
import uk.azdev.openfire.common.InvalidConfigurationException;
import uk.azdev.openfire.common.OpenFireConfiguration;
import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.conversations.Conversation;
import uk.azdev.openfire.friendlist.Friend;
import uk.azdev.openfire.friendlist.FriendsList;

public class OpenFireRelay implements ConnectionEventListener {

	private XFireConnection connection;
	private CountDownLatch exitLatch;
	private Set<String> adminList;
	
	public OpenFireRelay(OpenFireConfiguration config, Set<String> adminList) {
		exitLatch = new CountDownLatch(1);
		connection = new XFireConnection(config);
		this.adminList = adminList;
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
		FriendsList list = connection.getFriendList();
		
		Friend sourceFriend = list.getOnlineFriend(sessionId);
		System.out.println("message received from " + sourceFriend.getUserName() + " (" + sessionId + ")");
		Conversation sourceConv = connection.getConversation(sessionId);
		String message = sourceConv.getLastMessage();
		
		if(adminList.contains(sourceFriend.getUserName())) {
			System.out.println("Routing admin message");
			routeToAll(sourceFriend, message);
		} else {
			System.out.println("Routing user message to admins");
			routeToAdmins(sourceFriend, message);
		}
	}
	
	private void routeToAll(Friend source, String message) {
		FriendsList list = connection.getFriendList();
		Set<Friend> myFriends = list.getMyFriends();
		
		for(Friend f : myFriends) {
			if(!f.isOnline() || f.equals(source)) {
				continue;
			}
			
			System.out.println("Forwarding message to " + f.getDisplayName());
			
			Conversation conv = connection.getConversation(f.getSessionId());
			conv.sendMessage(message);
		}
	}
	
	private void routeToAdmins(Friend source, String message) {
		FriendsList list = connection.getFriendList();
		Set<Friend> myFriends = list.getMyFriends();
		
		for(Friend f : myFriends) {
			if(!f.isOnline() || !adminList.contains(f.getUserName())) {
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
		
		OpenFireConfiguration config;
		try {
			config = readConfig();
		} catch (IOException e) {
			System.err.println("Unable to read configuration file: " + e.getMessage());
			return;
		} catch (InvalidConfigurationException e) {
			System.err.println("Configuration file is invalid: " + e.getMessage());
			return;
		}
		
		Set<String> admins;
		try {
			admins = parseAdminList();
		} catch (IOException e) {
			System.err.println("Unable to read admin list file: " + e.getMessage());
			return;
		}
		
		OpenFireRelay relay = new OpenFireRelay(config, admins);
		try {
			relay.start();
		} catch (UnknownHostException e) {
			System.err.println("Unable to resolve host: " + e.getMessage());
			return;
		} catch (IOException e) {
			System.err.println("I/O error occurred: " + e.getMessage());
			e.printStackTrace();
			return;
		}
		
		try {
			relay.waitForExit();
		} catch (InterruptedException e) {
			System.err.println("Main thread unexpectedly terminated");
		}
	}

	private static OpenFireConfiguration readConfig() throws IOException, InvalidConfigurationException {
		FileReader reader = new FileReader("openfire.cfg");
		return OpenFireConfiguration.readConfig(reader);
	}

	private static Set<String> parseAdminList() throws IOException {
		
		FileReader reader = new FileReader("admin.list");
		Properties admins = new Properties();
		admins.load(reader);
		
		Set<Object> adminUids = admins.keySet();
		Set<String> adminUidSet = new HashSet<String>();
		
		for(Object uid : adminUids) {
			adminUidSet.add((String)uid);
		}
		
		return adminUidSet;
	}
}
