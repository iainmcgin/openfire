/*
 * Created by JFormDesigner on Fri May 25 22:14:49 BST 2007
 */

package uk.azdev.openfire.client;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import uk.azdev.openfire.ConnectionEventListener;
import uk.azdev.openfire.XFireConnection;
import uk.azdev.openfire.common.OpenFireConfiguration;
import uk.azdev.openfire.common.SessionId;
import uk.azdev.openfire.conversations.Conversation;
import uk.azdev.openfire.friendlist.Friend;
import uk.azdev.openfire.friendlist.FriendsList;

public class OpenFireClient extends JFrame implements ConnectionEventListener {
	
	private static final long serialVersionUID = 1L;
	
	private OpenFireConfiguration config;
	private XFireConnection connection;
	
	private FriendListModel onlineFriendsModel;
	private FriendListModel offlineFriendsModel;
	private FriendListModel friendsOfFriendsModel;
	
	private Map<SessionId, ConversationWindow> openConversations;
	
	private CountDownLatch exitLatch;
	
	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JMenuBar menuBar;
	private JMenu actionMenu;
	private JMenuItem connectionOption;
	private JMenuItem disconnectOption;
	private JLabel onlineFriendsLabel;
	private JScrollPane onlineFriendsContainer;
	private JList onlineFriendsList;
	private JLabel friendsOfFriendsLabel;
	private JScrollPane fofContainer;
	private JList fofList;
	private JLabel offlineFriendsLabel;
	private JScrollPane offlineFriendsContainer;
	private JList offlineFriendsList;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	
	public OpenFireClient() {
		config = new OpenFireConfiguration();
		exitLatch = new CountDownLatch(1);
		initComponents();
		
		onlineFriendsModel = new FriendListModel();
		onlineFriendsList.setModel(onlineFriendsModel);
		
		offlineFriendsModel = new FriendListModel();
		offlineFriendsList.setModel(offlineFriendsModel);
		
		friendsOfFriendsModel = new FriendListModel();
		fofList.setModel(friendsOfFriendsModel);
		
		openConversations = new HashMap<SessionId, ConversationWindow>();
		
		this.addWindowListener(new ExitListener());
		
		this.pack();
		this.setVisible(true);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		menuBar = new JMenuBar();
		actionMenu = new JMenu();
		connectionOption = new JMenuItem();
		disconnectOption = new JMenuItem();
		onlineFriendsLabel = new JLabel();
		onlineFriendsContainer = new JScrollPane();
		onlineFriendsList = new JList();
		friendsOfFriendsLabel = new JLabel();
		fofContainer = new JScrollPane();
		fofList = new JList();
		offlineFriendsLabel = new JLabel();
		offlineFriendsContainer = new JScrollPane();
		offlineFriendsList = new JList();

		//======== this ========
		setTitle("OpenFire");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setName("this");
		Container contentPane = getContentPane();
		contentPane.setLayout(new GridBagLayout());
		((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 0};
		((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0};
		((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
		((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 1.0E-4};

		//======== menuBar ========
		menuBar.setName("menuBar");

		//======== actionMenu ========
		actionMenu.setText("Action");
		actionMenu.setName("actionMenu");

		//---- connectionOption ----
		connectionOption.setText("Connect");
		connectionOption.setName("connectionOption");
		connectionOption.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				connect(e);
			}
		});
		actionMenu.add(connectionOption);

		//---- disconnectOption ----
		disconnectOption.setText("Disconnect");
		disconnectOption.setName("disconnectOption");
		disconnectOption.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				disconnect(e);
			}
		});
		actionMenu.add(disconnectOption);
		menuBar.add(actionMenu);
		setJMenuBar(menuBar);

		//---- onlineFriendsLabel ----
		onlineFriendsLabel.setText("Online Friends:");
		onlineFriendsLabel.setName("onlineFriendsLabel");
		contentPane.add(onlineFriendsLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//======== onlineFriendsContainer ========
		onlineFriendsContainer.setName("onlineFriendsContainer");

		//---- onlineFriendsList ----
		onlineFriendsList.setName("onlineFriendsList");
		onlineFriendsList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onlineFriendClicked(e);
			}
		});
		onlineFriendsContainer.setViewportView(onlineFriendsList);
		contentPane.add(onlineFriendsContainer, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//---- friendsOfFriendsLabel ----
		friendsOfFriendsLabel.setText("Friends of Friends:");
		friendsOfFriendsLabel.setName("friendsOfFriendsLabel");
		contentPane.add(friendsOfFriendsLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//======== fofContainer ========
		fofContainer.setName("fofContainer");

		//---- fofList ----
		fofList.setName("fofList");
		fofContainer.setViewportView(fofList);
		contentPane.add(fofContainer, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//---- offlineFriendsLabel ----
		offlineFriendsLabel.setText("Offline Friends:");
		offlineFriendsLabel.setName("offlineFriendsLabel");
		contentPane.add(offlineFriendsLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 5, 0), 0, 0));

		//======== offlineFriendsContainer ========
		offlineFriendsContainer.setName("offlineFriendsContainer");

		//---- offlineFriendsList ----
		offlineFriendsList.setName("offlineFriendsList");
		offlineFriendsContainer.setViewportView(offlineFriendsList);
		contentPane.add(offlineFriendsContainer, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH,
			new Insets(0, 0, 0, 0), 0, 0));
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}
	
	private void connect(ActionEvent e) {
		LoginDialog dialog = new LoginDialog(this);
		dialog.setVisible(true);
		
		config.setUsername(dialog.getUserName());
		config.setPassword(dialog.getPassword());
		
		connection = new XFireConnection(config);
		connection.addListener(this);
		try {
			connection.connect();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(this, "Error occurred while attempting to connect", "Connection Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				disconnectOption.setEnabled(true);
				connectionOption.setEnabled(false);
			}
		});
	}
	
	private void disconnect(ActionEvent e) {
		try {
			connection.disconnect();
			connectionOption.setEnabled(true);
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(this, "Error occurred while attempting to disconnect", "Connection Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				disconnectOption.setEnabled(false);
				connectionOption.setEnabled(true);
			}
		});
	}
	
	public void disconnected() {
		onlineFriendsModel.clear();
		offlineFriendsModel.clear();
		friendsOfFriendsModel.clear();
	}
	
	private void onlineFriendClicked(MouseEvent e) {
		if(e.getClickCount() != 2) {
			return;
		}
		
		int selectedFriendIndex = onlineFriendsList.getSelectedIndex();
		Friend selectedFriend = onlineFriendsModel.getFriendAt(selectedFriendIndex);
		createConversationWindow(selectedFriend.getSessionId());
	}

	private void createConversationWindow(SessionId friendSid) {
		Conversation conversation = connection.getConversation(friendSid);
		ConversationWindow convWindow = new ConversationWindow(this, conversation);
		openConversations.put(friendSid, convWindow);
		convWindow.setVisible(true);
	}
	
	public void waitForExit() throws InterruptedException {
		exitLatch.await();
	}
	
	

	public void conversationUpdate(final SessionId sessionId) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if(!openConversations.containsKey(sessionId)) {
					createConversationWindow(sessionId);
				}
			}
		});
	}

	public void friendsListUpdated() {
		FriendsList friendsList = connection.getFriendList();
		SwingUtilities.invokeLater(new UpdateFriendList(friendsList));
	}

	public void loginFailed() {
		JOptionPane.showMessageDialog(this, "Incorrect login details", "Login failed", JOptionPane.ERROR_MESSAGE);
	}
	
	public void connectionError() {
		JOptionPane.showMessageDialog(this, "An error occurred which required your connection with the XFire server to be closed", "Connection error", JOptionPane.ERROR_MESSAGE);
	}

	public void conversationClosed(SessionId sessionId) {
		openConversations.remove(sessionId);
	}
	
	private final class ExitListener extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {
			if(connection != null) {
				try {
					connection.disconnect();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(OpenFireClient.this, "Error occurred while attempting to disconnect", "Connection Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			exitLatch.countDown();
		}
	}

	private class UpdateFriendList implements Runnable {

		private FriendsList friendsList;
		
		public UpdateFriendList(FriendsList friendsList) {
			this.friendsList = friendsList;
		}
		
		public void run() {
			ArrayList<Friend> onlineFriends = new ArrayList<Friend>();
			ArrayList<Friend> offlineFriends = new ArrayList<Friend>();
			ArrayList<Friend> onlineFriendsOfFriends = new ArrayList<Friend>();

			for(Friend f : friendsList.getAllFriends()) {
				if(friendsList.areConnected(friendsList.getSelf(), f)) {
					if(f.isOnline()) {
						onlineFriends.add(f);
					} else {
						offlineFriends.add(f);
					}
				} else {
					if(f.isOnline()) {
						onlineFriendsOfFriends.add(f);
					}
				}
			}
			
			onlineFriendsModel.updateList(onlineFriends);
			offlineFriendsModel.updateList(offlineFriends);
			friendsOfFriendsModel.updateList(onlineFriendsOfFriends);
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		OpenFireClient client = new OpenFireClient();
		client.waitForExit();
	}
}
