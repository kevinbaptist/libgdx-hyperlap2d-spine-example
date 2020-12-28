package com.kbaptista.example.listener;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.kbaptista.example.character.CharacterComponent;

public class PlayerInputHandler extends InputAdapter {
	private CharacterComponent characterComponent;


	@Override
	public boolean keyDown(int keycode) {
		if (characterComponent.isLocked) {
			return true;
		}
		switch (keycode) {
			case Input.Keys.LEFT:
				characterComponent.isMovingLeft = true;
				break;
			case Input.Keys.RIGHT:
				characterComponent.isMovingRight = true;
				break;
			case Input.Keys.UP:
				characterComponent.isMovingUp = true;
				break;
			case Input.Keys.SPACE:
				characterComponent.isHoverboard = true;
				break;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (characterComponent.isLocked) {
			return true;
		}
		switch (keycode) {
			case Input.Keys.LEFT:
				characterComponent.isMovingLeft = false;
				break;
			case Input.Keys.RIGHT:
				characterComponent.isMovingRight = false;
				break;
			case Input.Keys.UP:
				characterComponent.isMovingUp = false;
				break;
			case Input.Keys.SPACE:
				characterComponent.isHoverboard = false;
				break;
		}
		return true;
	}

	public void setCharacterComponent(CharacterComponent characterComponent) {
		this.characterComponent = characterComponent;
	}
}
