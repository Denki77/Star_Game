package com.star.app.game.controllers;

import com.star.app.game.Background;
import com.star.app.game.entity.Bullet;
import com.star.app.game.entity.Hero;

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
        this.meteorController = new MeteorController();
    }


    public void update(float dt) {
        background.update(dt);
        meteorController.update(dt);
        hero.update(dt);
        bulletController.update(dt);
        checkCollisions();
    }


    public void checkCollisions() {
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);

            if (meteorController.getMeteor().getPosition().dst(b.getPosition()) < 256.0f * 0.55f * meteorController.getMeteor().getScale()) {
                b.deactivate();
                meteorController.getMeteor().deactivate();
            }
        }
    }
}
