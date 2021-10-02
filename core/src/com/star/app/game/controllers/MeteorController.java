package com.star.app.game.controllers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.game.entity.Meteor;
import com.star.app.game.helpers.ObjectPool;

public class MeteorController extends ObjectPool<Meteor> {
    private GameController gc;

    @Override
    protected Meteor newObject() {
        return new Meteor(gc);
    }

    public MeteorController(GameController gc) {
        this.gc = gc;
    }

    public void render(SpriteBatch batch) {
        for (Meteor a : activeList) {
            a.render(batch);
        }
    }

    public void setup(float x, float y, float vx, float vy, float scale) {
        getActiveElement().activate(x, y, vx, vy, scale);
    }

    public void update(float dt) {
        for (Meteor value : activeList) {
            value.update(dt);
        }
        checkPool();
    }

}
