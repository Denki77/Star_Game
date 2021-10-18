package com.star.app.game.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.star.app.game.Background;
import com.star.app.game.entity.Bullet;
import com.star.app.game.entity.Hero;
import com.star.app.game.entity.Meteor;
import com.star.app.game.entity.PowerUp;
import com.star.app.screen.ScreenManager;

public class GameController {
    private final Background background;
    private final BulletController bulletController;
    private final MeteorController meteorController;
    private final ParticleController particleController;
    private final PowerUpsController powerUpsController;
    private final Hero hero;
    private final Vector2 tmpVec;
    private final Stage stage;
    private boolean pause;
    private int level;
    private float roundTimer;
    private Music music;

    public int getLevel() {
        return level;
    }

    public Stage getStage() {
        return stage;
    }

    public PowerUpsController getPowerUpsController() {
        return powerUpsController;
    }

    public MeteorController getMeteorController() {
        return meteorController;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public ParticleController getParticleController() {
        return particleController;
    }

    public Hero getHero() {
        return hero;
    }

    public Background getBackground() {
        return background;
    }

    public GameController(SpriteBatch batch) {
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.meteorController = new MeteorController(this);
        this.bulletController = new BulletController(this);
        this.particleController = new ParticleController();
        this.powerUpsController = new PowerUpsController(this);
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.stage.addActor(hero.getShop());
        Gdx.input.setInputProcessor(stage);
        this.tmpVec = new Vector2(0.0f, 0.0f);
        this.level = 1;
        this.roundTimer = 0.0f;
        generateBigAsteroids(1);
    }

    private void generateBigAsteroids(int count) {
        for (int i = 0; i < count; i++) {
            meteorController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                    MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                    MathUtils.random(-200, 200), MathUtils.random(-200, 200), 1.0f);
        }
    }
    public void update(float dt) {
        if (pause) {
            return;
        }
        roundTimer += dt;
        background.update(dt);
        hero.update(dt);
        meteorController.update(dt);
        bulletController.update(dt);
        powerUpsController.update(dt);
        particleController.update(dt);
        checkCollisions();
        if (!hero.isAlive()) {
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME_OVER, hero);
        }
        if (meteorController.getActiveList().size() == 0) {
            level++;
            generateBigAsteroids(Math.min(level, 3));
            roundTimer = 0.0f;
        }
        stage.act(dt);
    }

    public void checkCollisions() {
        for (Bullet b : bulletController.getActiveList()) {
            for (Meteor a : meteorController.getActiveList()) {
                if (a.getHitArea().contains(b.getPosition())) {
                    b.deactivate();
                    if (a.takeDamage(1)) {
                        hero.addScore(a.getHpMax() * 100);
                    }
                    break;
                }
            }
        }
        for (Meteor a : meteorController.getActiveList()) {
            if (a.getHitArea().contains(hero.getPosition())) {
                a.deactivate();
                meteorController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                        MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                        MathUtils.random(-200, 200), MathUtils.random(-200, 200), 1.0f);
                hero.decreaseHp(2);
                break;
            }
        }

        for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {
            PowerUp p = powerUpsController.getActiveList().get(i);
            if (hero.getHitArea().contains(p.getPosition())) {
                hero.consume(p);
                particleController.getEffectBuilder().takePowerUpEffect(
                        p.getPosition().x, p.getPosition().y, p.getType());
                p.deactivate();
            }
        }

        for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {
            PowerUp p = powerUpsController.getActiveList().get(i);
            if (hero.getMagneticField().contains(p.getPosition())) {
                tmpVec.set(hero.getPosition()).sub(p.getPosition()).nor();
                p.getVelocity().mulAdd(tmpVec, 200.0f);
            }

            if (hero.getHitArea().contains(p.getPosition())) {
                hero.consume(p);
                particleController.getEffectBuilder().takePowerUpEffect(
                        p.getPosition().x, p.getPosition().y, p.getType());
                p.deactivate();
            }
        }

    }

    public void dispose() {
        background.dispose();
    }

}
