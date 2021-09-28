package com.star.app.game.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.screen.ScreenManager;

public class Meteor {
    private final Vector2 position;
    private final Vector2 velocity;
    int x, y, xV, yV;
    private float scale;

    public Meteor() {
        getNewXAndY();
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(xV, yV);
        setScale();
    }

    public void updateMeteorParameters() {
        getNewXAndY();
        this.position.x = x;
        this.position.y = y;
        this.velocity.x = xV;
        this.velocity.y = yV;
        setScale();
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getScale() {
        return scale;
    }

    public void deactivate() {
        position.x = -ScreenManager.POSITION_OUT_OF_SCREEN;
        position.y = -ScreenManager.POSITION_OUT_OF_SCREEN;
    }

    /**
     * Сделал так, чтобы астро всегда появлялся за пределами видимости
     */
    private void getNewXAndY() {
        x = MathUtils.random(-299, ScreenManager.SCREEN_WIDTH + 299);
        if (x > 0 && x < ScreenManager.SCREEN_WIDTH) {
            if (MathUtils.random(0, 1) == 1) {
                y = ScreenManager.SCREEN_WIDTH + MathUtils.random(0, 299);
            } else {
                y = MathUtils.random(-299, 0);
            }
        } else {
            y = MathUtils.random(-299, ScreenManager.SCREEN_HEIGHT + 299);
        }
        getRandomVelocity();
    }

    /**
     * Вектор скорости всегда направлен в сторону экрана
     */
    private void getRandomVelocity() {
        if (x < 0) {
            xV = MathUtils.random(0, 400);
        } else if (x > ScreenManager.SCREEN_WIDTH) {
            xV = MathUtils.random(-400, 0);
        } else {
            xV = MathUtils.random(-400, 400);
        }

        if (y < 0) {
            yV = MathUtils.random(0, 400);
        } else if (y > ScreenManager.SCREEN_HEIGHT) {
            yV = MathUtils.random(-400, 0);
        } else {
            yV = MathUtils.random(-400, 400);
        }

    }

    private void setScale() {
        this.scale = (float) (Math.sqrt(velocity.x * velocity.x + velocity.y * velocity.y) / 256f * 0.8f);
    }
}