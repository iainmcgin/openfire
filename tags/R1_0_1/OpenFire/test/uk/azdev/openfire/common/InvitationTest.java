package uk.azdev.openfire.common;

import static org.junit.Assert.*;

import org.junit.Test;

public class InvitationTest {

	@Test
	public void testEqualsObject() {
		Invitation a = new Invitation("alice", "Alice", "hello!");
		Invitation b = new Invitation("alice", "Alice", "hello!");
		Invitation c = new Invitation("diffuser", "Alice", "hello!");
		Invitation d = new Invitation("alice", "DifferentDisplay", "hello!");
		Invitation e = new Invitation("alice", "Alice", "different message");
		
		assertTrue(a.equals(b));
		assertTrue(b.equals(a));
		assertTrue(a.hashCode() == b.hashCode());
		assertFalse(a.equals(c));
		assertFalse(a.equals(d));
		assertFalse(a.equals(e));
		assertFalse(a.equals(new Object()));
	}

}
