package com.mygdx.ui.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.logic.helper.Constants;
import com.mygdx.logic.service.iterator.IteratorHelper;
import com.mygdx.ui.command.clickables.ClickableLabel;
import com.mygdx.ui.command.Invoker;

public class ScreenBodyHelper {
    public void handleButton(ClickableLabel opt, Invoker invoker, Batch batch){
        try {
            if (Gdx.input.getX() > opt.getX() && Gdx.input.getX() < opt.getX() + opt.getWidth() && Gdx.input.getY() > opt.getY() && Gdx.input.getY() < opt.getHeight() + opt.getY()) {
                batch.draw(opt.getTexture(), opt.getX(), Gdx.graphics.getHeight() - (opt.getHeight() + opt.getY()), opt.getWidth(), opt.getHeight());
                if (Gdx.input.justTouched()) {
                    invoker.placeActions();
                }
            } else {
                batch.draw(opt.getInactiveTexture(), opt.getX(), Gdx.graphics.getHeight() - (opt.getHeight() + opt.getY()), opt.getWidth(), opt.getHeight());
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public Shape createPolygonShape(PolygonMapObject polygonMapObject) {
        float[] vertices = polygonMapObject.getPolygon().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; i++) {
            Vector2 current = new Vector2(vertices[i * 2] / Constants.PPM, vertices[i * 2 + 1] / Constants.PPM);
            worldVertices[i] = current;
        }

        PolygonShape shape = new PolygonShape();
        shape.set(worldVertices);
        return shape;
    }

    public void createStaticBody(PolygonMapObject polygonMapObject, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bodyDef);
        Shape shape = createPolygonShape(polygonMapObject);
        body.createFixture(shape, 1000);
        shape.dispose();
    }
}