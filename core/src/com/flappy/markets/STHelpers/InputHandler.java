package com.flappy.markets.STHelpers;

import com.badlogic.gdx.InputProcessor;
import com.flappy.markets.GameObjects.Bird;
import com.flappy.markets.GameWorld.GameWorld;

public class InputHandler implements InputProcessor {

    private Bird myBird;
    private GameWorld myWorld;
ke

    public InputHandler(GameWorld myWorld) {
        this.myWorld = myWorld;
        myBird = myWorld.getBird();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if (!myWorld.isGameOver()) {
            myWorld.touching = true;

            if (myWorld.isReady()) {
                myWorld.start();
            }
            myBird.onClick();
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if (!myWorld.isGameOver()) {
            myWorld.touching = false;
        }

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        // TODO Auto-generated method stub
        return false;
    }

}
