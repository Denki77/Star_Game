package com.star.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Background {
    private class Star {
        private final Vector2 position;
        private final Vector2 velocity;
        private float scale;

        public Star() {
            this.position = new Vector2(MathUtils.random(-200, ScreenManager.SCREEN_WIDTH + 200),
                    MathUtils.random(-200, ScreenManager.SCREEN_HEIGHT + 200));
            this.velocity = new Vector2(MathUtils.random(-40, -5), 0);
            this.scale = Math.abs(velocity.x) / 40f * 0.8f;
        }

        public void update(float dt) {
            position.x += (velocity.x - game.getHero().getLastDisplacement().x * 15) * dt;
            position.y += (velocity.y - game.getHero().getLastDisplacement().y * 15) * dt;

            if (position.x < -200) {
                position.x = ScreenManager.SCREEN_WIDTH + 200;
                position.y = MathUtils.random(-200, ScreenManager.SCREEN_HEIGHT + 200);
                scale = Math.abs(velocity.x) / 40f * 0.8f;
            }
        }
    }

    private class Meteor {
        private final Vector2 position;
        int x, y, xV, yV;
        private final Vector2 velocity;
        private float scale;

        public Meteor() {
            getNewXAndY();
            this.position = new Vector2(x, y);
            this.velocity = new Vector2(xV, yV);
            setScale();
        }

        public void update(float dt) {
            position.x += (velocity.x) * dt;
            position.y += (velocity.y) * dt;

            if (position.x < -300 ||
                    position.y < -300 ||
                    position.x > ScreenManager.SCREEN_WIDTH + 300 ||
                    position.y > ScreenManager.SCREEN_HEIGHT + 300
            ) {
                getNewXAndY();
                position.x = x;
                position.y = y;
                velocity.x = xV;
                velocity.y = yV;
                setScale();
            }
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

    private final int STAR_COUNT = 1000;
    private StarGame game;
    private Texture textureCosmos;
    private Texture textureStar;
    private Texture textureMeteor;
    private Star[] stars;
    private final Meteor meteor;

    public Background(StarGame game) {
        this.textureCosmos = new Texture("bg.png");
        this.textureStar = new Texture("star16.png");
        this.textureMeteor = new Texture("asteroid.png");
        this.game = game;
        this.meteor = new Meteor();
        this.stars = new Star[STAR_COUNT];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star();
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(textureCosmos, 0, 0);

        for (Star star : stars) {
            batch.draw(textureStar, star.position.x - 8, star.position.y - 8, 8, 8, 16, 16,
                    star.scale, star.scale, 0, 0, 0, 16, 16, false, false);

            if (MathUtils.random(0, 300) < 1) {
                batch.draw(textureStar, star.position.x - 8, star.position.y - 8, 8, 8, 16, 16,
                        star.scale * 2, star.scale * 2, 0, 0, 0, 16, 16, false, false);
            }
        }
        batch.draw(textureMeteor, meteor.position.x - 150, meteor.position.y - 150, 150, 150, 256, 256,
                meteor.scale, meteor.scale, 0, 0, 0, 256, 256, false, false);
    }

    public void update(float dt) {
        meteor.update(dt);
        for (Star star : stars) {
            star.update(dt);
        }
    }
}
