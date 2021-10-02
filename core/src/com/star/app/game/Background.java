package com.star.app.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.controllers.GameController;
import com.star.app.game.controllers.MeteorController;
import com.star.app.game.entity.Meteor;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Background {
    private class Star {
        private Vector2 position;
        private Vector2 velocity;
        private float scale;

        public Star() {
            this.position = new Vector2(MathUtils.random(-200, ScreenManager.SCREEN_WIDTH + 200),
                    MathUtils.random(-200, ScreenManager.SCREEN_HEIGHT + 200));
            this.velocity = new Vector2(MathUtils.random(-40, -5), 0);
            this.scale = Math.abs(velocity.x) / 40f * 0.8f;
        }

        public void update(float dt) {
            position.x += (velocity.x - gc.getHero().getVelocity().x * 0.1) * dt;
            position.y += (velocity.y - gc.getHero().getVelocity().y * 0.1) * dt;

            if (position.x < -200) {
                position.x = ScreenManager.SCREEN_WIDTH + 200;
                position.y = MathUtils.random(-200, ScreenManager.SCREEN_HEIGHT + 200);
                scale = Math.abs(velocity.x) / 40f * 0.8f;
            }
        }
    }

    private final int STAR_COUNT = 1000;
    private GameController gc;
    private final Texture textureCosmos;
    private final TextureRegion textureStar;
    private final Star[] stars;

    public Background(GameController gc) {
        this.textureCosmos = new Texture("images/bg.png");
        this.textureStar = Assets.getInstance().getAtlas().findRegion("star16");
        this.gc = gc;
        this.stars = new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star();
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(textureCosmos, 0, 0);

        for (Star star : stars) {
            batch.draw(textureStar, star.position.x - 8, star.position.y - 8, 8, 8, 16, 16,
                    star.scale, star.scale, 0);

            if (MathUtils.random(0, 300) < 1) {
                batch.draw(textureStar, star.position.x - 8, star.position.y - 8, 8, 8, 16, 16,
                        star.scale * 2, star.scale * 2, 0);
            }
        }
    }

    public void update(float dt) {
        for (Star star : stars) {
            star.update(dt);
        }
    }
}
