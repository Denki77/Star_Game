package com.star.app.game.controllers;

import com.badlogic.gdx.math.MathUtils;
import com.star.app.game.Background;
import com.star.app.game.entity.Bullet;
import com.star.app.game.entity.Hero;
import com.star.app.game.entity.Meteor;
import com.star.app.screen.ScreenManager;

public class GameController {
    private Background background;
    private BulletController bulletController;
    private final MeteorController meteorController;
    private Hero hero;

    public BulletController getBulletController() {
        return bulletController;
    }

    public Hero getHero() {
        return hero;
    }

    public Background getBackground() {
        return background;
    }

    public MeteorController getMeteorController() {
        return meteorController;
    }

    public GameController() {
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.bulletController = new BulletController();
        this.meteorController = new MeteorController(this);
        for (int i = 0; i < 3; i++) {
            meteorController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                    MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                    MathUtils.random(-200, 200), MathUtils.random(-200, 200), 1.0f);
        }
    }


    public void update(float dt) {
        background.update(dt);
        meteorController.update(dt);
        hero.update(dt);
        bulletController.update(dt);
        checkCollisions();
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

    }
}
