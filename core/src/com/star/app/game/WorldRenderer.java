package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.star.app.game.controllers.GameController;

public class WorldRenderer {
    private SpriteBatch batch;
    private GameController gc;

    public WorldRenderer(GameController gc, SpriteBatch batch) {
        this.gc = gc;
        this.batch = batch;
    }

    public void render() {
        ScreenUtils.clear(0, 0.2f, 0.5f, 1);
        batch.begin();
        gc.getBackground().render(batch);
        gc.getMeteorController().render(batch);
        gc.getHero().render(batch);
        gc.getBulletController().render(batch);
        batch.end();
    }
}
