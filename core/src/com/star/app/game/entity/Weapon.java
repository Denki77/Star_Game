package com.star.app.game.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.star.app.game.controllers.GameController;

public class Weapon {

    private final GameController gc;
    private final Hero hero;

    private final String title;
    private final float firePeriod;
    private final int damage;
    private final float bulletSpeed;
    private final int maxBullets;
    private int curBullets;
    private final Vector3[] slots;

    public String getTitle() {
        return title;
    }

    public int getDamage() {
        return damage;
    }

    public float getFirePeriod() {
        return firePeriod;
    }

    public int getMaxBullets() {
        return maxBullets;
    }

    public int getCurBullets() {
        return curBullets;
    }

    public Weapon(GameController gc, Hero hero, String title,
                  float firePeriod, int damage, float bulletSpeed,
                  int maxBullets, Vector3[] slots) {
        this.gc = gc;
        this.hero = hero;
        this.title = title;
        this.firePeriod = firePeriod;
        this.damage = damage;
        this.bulletSpeed = bulletSpeed;
        this.maxBullets = maxBullets;
        this.curBullets = maxBullets;
        this.slots = slots;
    }

    public void fire() {
        if (curBullets > 0) {
            curBullets--;

            for (Vector3 slot : slots) {
                float x, y, vx, vy;

                x = hero.getPosition().x + MathUtils.cosDeg(hero.getAngle() + slot.y) * slot.x;
                y = hero.getPosition().y + MathUtils.sinDeg(hero.getAngle() + slot.y) * slot.x;

                vx = hero.getVelocity().x + bulletSpeed * MathUtils.cosDeg(hero.getAngle() + slot.z);
                vy = hero.getVelocity().y + bulletSpeed * MathUtils.sinDeg(hero.getAngle() + slot.z);

                gc.getBulletController().setup(x, y, vx, vy);
            }
        }
    }

    public void addAmmos(int amount) {
        curBullets += amount;
        if (curBullets > maxBullets) {
            curBullets = maxBullets;
        }
    }
}
