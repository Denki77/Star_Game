package com.star.app.game.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.controllers.GameController;
import com.star.app.game.helpers.Interface.Poolable;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Meteor implements Poolable {
    private final TextureRegion texture;
    private final GameController gc;
    private final Vector2 position;
    private final Vector2 velocity;
    private int hp;
    private int hpMax;
    private float angle;//угол показа изображения
    private float rotationSpeed;//скорость вращения
    private float scale;//масштаб
    private boolean active;
    private Circle hitArea;

    private final float BASE_SIZE = 256.0f;
    private final float BASE_RADIUS = BASE_SIZE / 2;

    public float getScale() {
        return scale;
    }

    public int getHpMax() {
        return hpMax;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public Vector2 getPosition() {
        return position;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public Meteor(GameController gc) {
        this.gc = gc;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.hitArea = new Circle(0, 0, 0);
        this.active = false;
        this.texture = Assets.getInstance().getAtlas().findRegion("asteroid");
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 128, position.y - 128, 128, 128,
                256, 256, scale, scale, angle);
    }

    public void deactivate() {
        active = false;
    }

    public void activate(float x, float y, float vx, float vy, float scale) {
        this.position.set(x, y);
        this.velocity.set(vx, vy);
        this.hpMax = (int) ((5 + gc.getLevel() * 2) * scale);
        this.hp = hpMax;
        this.angle = MathUtils.random(0.0f, 360.0f);
        this.rotationSpeed = MathUtils.random(-180.0f, 180.0f);
        this.hitArea.setPosition(position);
        this.scale = scale;
        this.active = true;
        this.hitArea.setRadius(BASE_RADIUS * scale * 0.9f);
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        angle += rotationSpeed * dt;

        if (position.x < -ScreenManager.POSITION_OUT_OF_SCREEN) {
            position.x = ScreenManager.SCREEN_WIDTH + ScreenManager.POSITION_OUT_OF_SCREEN;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH + ScreenManager.POSITION_OUT_OF_SCREEN) {
            position.x = -ScreenManager.POSITION_OUT_OF_SCREEN;
        }
        if (position.y < -ScreenManager.POSITION_OUT_OF_SCREEN) {
            position.y = ScreenManager.SCREEN_HEIGHT + ScreenManager.POSITION_OUT_OF_SCREEN;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT + ScreenManager.POSITION_OUT_OF_SCREEN) {
            position.y = -ScreenManager.POSITION_OUT_OF_SCREEN;
        }
        hitArea.setPosition(position);
    }


    public boolean takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            deactivate();
            if (scale > 0.3) {
                gc.getMeteorController().setup(position.x, position.y,
                        MathUtils.random(-200, 200), MathUtils.random(-200, 200), scale - 0.2f);

                gc.getMeteorController().setup(position.x, position.y,
                        MathUtils.random(-200, 200), MathUtils.random(-200, 200), scale - 0.2f);

                gc.getMeteorController().setup(position.x, position.y,
                        MathUtils.random(-200, 200), MathUtils.random(-200, 200), scale - 0.2f);
            }
            return true;
        }
        return false;
    }
}