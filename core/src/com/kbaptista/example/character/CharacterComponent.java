package com.kbaptista.example.character;

import com.badlogic.ashley.core.Component;

public class CharacterComponent implements Component {
	public static final int MAX_JUMPS = 1;
	public int jumps = MAX_JUMPS;

	public void reset() {
		jumps = MAX_JUMPS;
	}

	public void jump() {
		jumps--;
	}
}
