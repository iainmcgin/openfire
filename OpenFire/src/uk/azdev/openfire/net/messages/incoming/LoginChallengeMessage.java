package uk.azdev.openfire.net.messages.incoming;

import uk.azdev.openfire.net.attrvalues.StringKeyedAttributeMap;
import uk.azdev.openfire.net.messages.IMessage;
import uk.azdev.openfire.net.messages.StringMapBasedMessage;

public class LoginChallengeMessage extends StringMapBasedMessage {

	public static final int LOGIN_CHALLENGE_MESSAGE_ID = 128;

	private String salt;
	
	@Override
	protected void interpretAttributeMap(StringKeyedAttributeMap map) {
		salt = map.getStringAttributeValue("salt");
	}

	@Override
	protected void populateAttributeMap(StringKeyedAttributeMap map) {
		map.addAttribute("salt", salt);
	}

	public int getMessageId() {
		return LOGIN_CHALLENGE_MESSAGE_ID;
	}

	public IMessage newInstance() {
		return new LoginChallengeMessage();
	}

	public String getSalt() {
		return salt;
	}
	
	public void setSalt(String salt) {
		if(salt == null || salt.length() != 40) {
			throw new IllegalArgumentException("invalid salt provided");
		}
		this.salt = salt;
	}
}
