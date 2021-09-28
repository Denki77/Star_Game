package com.star.app.game.controllers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.entity.Meteor;
import com.star.app.screen.ScreenManager;

public class MeteorController {
    private final Texture textureMeteor;
    private final Meteor meteor;
    private Vector2 position;
    private Vector2 velocity;

    public MeteorController() {
        this.textureMeteor = new Texture("asteroid.png");
        this.meteor = new Meteor();
        position = meteor.getPosition();
        velocity = meteor.getVelocity();
    }

    public Meteor getMeteor() {
        return meteor;
    }

    public void render(SpriteBatch batch) {
        batch.draw(textureMeteor, meteor.getPosition().x - 150, meteor.getPosition().y - 150, 150, 150, 256, 256,
                meteor.getScale(), meteor.getScale(), 0, 0, 0, 256, 256, false, false);
    }

    public void update(float dt) {
        position = meteor.getPosition();
        velocity = meteor.getVelocity();

        position.x += (velocity.x) * dt;
        position.y += (velocity.y) * dt;

        if (position.x < -ScreenManager.POSITION_OUT_OF_SCREEN ||
                position.y < -ScreenManager.POSITION_OUT_OF_SCREEN ||
                position.x > ScreenManager.SCREEN_WIDTH + ScreenManager.POSITION_OUT_OF_SCREEN ||
                position.y > ScreenManager.SCREEN_HEIGHT + ScreenManager.POSITION_OUT_OF_SCREEN
        ) {
            meteor.updateMeteorParameters();
        }
    }

}
