package com.mygdx.game.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

//controls the view of the camera
public class CameraHelper 
{
	private static final String TAG = CameraHelper.class.getName();
	
	private final float MAX_ZOOM_IN = 0.25f;
	private final float MAX_ZOOM_OUT = 10.0f;
	
	private Vector2 position;
	private float zoom;
	private Sprite target;
	
	/**
	 * Creates the target with a default position and zoom level
	 */
	public CameraHelper ()
	{
		position = new Vector2();
		zoom = 4.0f;
	}
	
	public void update (float deltaTime)
	{
		if (!hasTarget()) return;
		
		position.x = target.getX() + target.getOriginX();
		position.y = target.getY() + target.getOriginY();
	}
	
	/**
	 * sets a new position for the camera
	 * @param x
	 * @param y
	 */
	public void setPosition (float x, float y) 
	{
		this.position.set(x,y);
	}
	
	/**
	 * Returns the current position
	 * @return
	 */
	public Vector2 getPosition()
	{
		return position;
	}
	
	/**
	 * Zooms the camera in further
	 * @param amt
	 */
	public void addZoom(float amt)
	{
		setZoom(zoom+amt);
	}
	
	/**
	 * Sets the camera to a specific zoom 
	 * @param zoom
	 */
	public void setZoom(float zoom)
	{
		this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);
	}

	/**
	 * Returns the zoom leverl
	 * @return
	 */
	public float getZoom()
	{
		return zoom;
	}
	
	/**
	 * Sets the cameras target
	 * @param target
	 */
	public void setTarget (Sprite target)
	{
		this.target = target;
	}
	
	/**
	 * Returns the camera's current target
	 * @return
	 */
	public Sprite getTarget()
	{
		return target;
	}
	
	/**
	 * Returns a boolean for if the camera currently has a target object
	 * @return
	 */
	public boolean hasTarget()
	{
		return target!=null;
	}
	
	/**
	 * Checks to see if the camera has the parameter object as its target
	 * @param target
	 * @return
	 */
	public boolean hasTarget(Sprite target)
	{
		return hasTarget() && this.target.equals(target);
	}
	
	public void applyTo(OrthographicCamera camera)
	{
		camera.position.x = position.x;
		camera.position.y = position.y;
		camera.zoom = zoom;
		camera.update();
	}
}
