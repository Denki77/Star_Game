package com.star.app.game.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.controllers.GameController;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Hero {
    private GameController gc;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private float angel;
    private float enginePower;
    private float fireTimer;
    private int score;
    private int hp, hpMax = 1000;
    private int scoreView;
    private Polygon hitArea;


    public int getScore() {
        return score;
    }

    public int getHp() {
        return hp;
    }

    public void decreaseHp() {
        hp -= 100;
    }

    public int getScoreView() {
        return scoreView;
    }

    public void addScore(int amount) {
        this.score += amount;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Hero(GameController gc) {
        this.gc = gc;
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.position = new Vector2(640, 360);
        this.velocity = new Vector2(0, 0);
        this.angel = 0.0f;
        this.enginePower = 500.0f;
        this.hitArea = new Polygon(
                new float[]{-32.0f, -32f, -32f, -32f, -32f, -32f}
        );
        this.hp = hpMax;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64, 1, 1,
                angel);
    }

    public void update(float dt) {
        fireTimer += dt;
        if (scoreView < score) {
            scoreView += 1000 * dt;
            if (scoreView > score) {
                scoreView = score;
            }
        }
        if (hp < hpMax) {
            hp += 100 * dt;
            if (hp > hpMax) {
                hp = hpMax;
            }
        }
        if (hp < 0) {
            throw new NullPointerException();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            if (fireTimer > 0.2) {
                fireTimer = 0.0f;
                float wx;
                float wy;

                wx = position.x + MathUtils.cosDeg(angel + 90) * 20;
                wy = position.y + MathUtils.sinDeg(angel + 90) * 20;
                gc.getBulletController().setup(wx, wy,
                        MathUtils.cosDeg(angel) * 500 + velocity.x, MathUtils.sinDeg(angel) * 500 + velocity.y);

                wx = position.x + MathUtils.cosDeg(angel - 90) * 20;
                wy = position.y + MathUtils.sinDeg(angel - 90) * 20;
                gc.getBulletController().setup(wx, wy,
                        MathUtils.cosDeg(angel) * 500 + velocity.x, MathUtils.sinDeg(angel) * 500 + velocity.y);

            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angel += 180.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angel -= 180.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.x += MathUtils.cosDeg(angel) * enginePower * dt;
            velocity.y += MathUtils.sinDeg(angel) * enginePower * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            position.x -= MathUtils.cosDeg(angel) * 80.0f * dt;
            position.y -= MathUtils.sinDeg(angel) * 80.0f * dt;
            velocity.x += MathUtils.cosDeg(angel) * dt * -enginePower / 3;
            velocity.y += MathUtils.sinDeg(angel) * dt * -enginePower / 3;
        }
        position.mulAdd(velocity, dt);
        float stopKoef = 1.0f - 1.0f * dt;
        if (stopKoef < 0) {
            stopKoef = 0;
        }
        velocity.scl(stopKoef);
        if (position.x < 32f) {
            position.x = 32f;
            velocity.x *= -1;
        }
        if (position.x > ScreenManager.SCREEN_WIDTH - 32f) {
            position.x = ScreenManager.SCREEN_WIDTH - 32f;
            velocity.x *= -1;
        }
        if (position.y < 32f) {
            position.y = 32f;
            velocity.y *= -1;
        }
        if (position.y > ScreenManager.SCREEN_HEIGHT - 32f) {
            position.y = ScreenManager.SCREEN_HEIGHT - 32f;
            velocity.y *= -1;
        }

    }
}
