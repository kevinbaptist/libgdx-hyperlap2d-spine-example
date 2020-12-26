package com.kbaptista.example.character;

import com.badlogic.ashley.core.Component;

public class CharacterComponent implements Component {
	public int jumps = 1;

	public void reset() {
		jumps = 1;
	}
}
