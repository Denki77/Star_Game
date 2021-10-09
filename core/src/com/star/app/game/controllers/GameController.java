package com.star.app.game.controllers;

import com.badlogic.gdx.Gdx;
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
    private Background background;
    private BulletController bulletController;
    private final MeteorController meteorController;
    private Hero hero;
    private BulletController bulletController;
    private ParticleController particleController;
    private PowerUpsController powerUpsController;
    private Hero hero;
    private Vector2 tmpVec;
    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public PowerUpsController getPowerUpsController() {
        return powerUpsController;
    }

    public MeteorController getAsteroidController() {
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
        for (int i = 0; i < 3; i++) {
            meteorController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                    MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                    MathUtils.random(-200, 200), MathUtils.random(-200, 200), 1.0f);
        }
    }

    public void update(float dt) {
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
                hero.decreaseHp();
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

    }

    public void dispose() {
        background.dispose();
    }

}
