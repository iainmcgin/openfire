package uk.azdev.openfire.client;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import uk.azdev.openfire.friendlist.Friend;

public class FriendListModel extends AbstractListModel {

	private static final long serialVersionUID = 1L;
	private List<Friend> friends;
	
	public FriendListModel() {
		this.friends = new ArrayList<Friend>();
	}
	
	public void updateList(List<Friend> newFriends) {
		final int oldSize = friends.size();
		friends.clear();
		friends.addAll(newFriends);
		fireContentsChanged(this, 0, Math.max(oldSize, friends.size()) - 1);
	}
	
	public void clear() {
		final int oldSize = friends.size();
		friends.clear();
		fireIntervalRemoved(this, 0, oldSize);
	}
	
	public Object getElementAt(int index) {
		return friends.get(index).getDisplayName();
	}
	
	public Friend getFriendAt(int index) {
		return friends.get(index);
	}

	public int getSize() {
		return friends.size();
	}

}
