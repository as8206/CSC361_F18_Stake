/**
 * Holds the chests and the logic to open them
 * @author Andrew Stake
 */
package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Chest extends AbstractGameObject
{

	public Chest(TextureRegion img)
	{
		super(img);
		
		CircleShape circle = new CircleShape();
		circle.setRadius(1.5f); //TODO radius value needs testing
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.isSensor = true;
		
		body.createFixture(fixtureDef);
		
		circle.dispose();
	}

}
