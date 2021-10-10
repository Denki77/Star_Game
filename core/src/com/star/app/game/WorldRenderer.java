package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.app.game.controllers.GameController;
import com.star.app.screen.utils.Assets;

public class WorldRenderer {
    private SpriteBatch batch;
    private GameController gc;
    private BitmapFont font32;
    private StringBuilder stringBuilder;

    public WorldRenderer(GameController gc, SpriteBatch batch) {
        this.gc = gc;
        this.batch = batch;
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf", BitmapFont.class);
        this.stringBuilder = new StringBuilder();
    }

    public void render() {
        ScreenUtils.clear(0, 0.2f, 0.5f, 1);
        batch.begin();
        gc.getBackground().render(batch);
        gc.getMeteorController().render(batch);
        gc.getHero().render(batch);
        gc.getBulletController().render(batch);
        stringBuilder.clear();
        stringBuilder.append("SCORE: ").append(gc.getHero().getScore());
        font32.draw(batch, stringBuilder,20,700);
        stringBuilder.clear();
        stringBuilder.append("HP: ").append(gc.getHero().getHp());
        font32.draw(batch, stringBuilder,20,600);
        batch.end();
    }
}
