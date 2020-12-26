package com.kbaptista.example.character;

public enum AnimationState {
	RUN("run"),
	RUN_TO_IDLE("run-to-idle"),
	IDLE("idle"),
	JUMP("jump");

	private final String action;

	public String getAction() {
		return this.action;
	}

	AnimationState(String action) {
		this.action = action;
	}
}
