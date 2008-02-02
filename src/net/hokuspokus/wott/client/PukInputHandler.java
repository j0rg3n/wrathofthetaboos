package net.hokuspokus.wott.client;

import net.hokuspokus.wott.common.Person;
import net.hokuspokus.wott.utils.TextureUtil;

import com.jme.input.InputHandler;
import com.jme.input.MouseInput;
import com.jme.math.FastMath;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Cylinder;


public class PukInputHandler extends InputHandler
{
	private WrathOfTaboo game;
	private Spatial puk;
	private static final float PUK_RADIUS = 1.5f;

	public PukInputHandler(WrathOfTaboo game)
	{
		this.game = game;
		this.puk = new Cylinder("Puk", 16, 16, 1.5f, 0.4f, true);
		this.puk.getLocalRotation().fromAngles(FastMath.HALF_PI, 0, 0);
		TextureUtil.getInstance().setTexture(puk, "/ressources/2d gfx/citroen_logo_jo.jpg");
		game.getBoardNode().attachChild(puk);
	}
	
	static Vector2f _screenPos = new Vector2f();
	static Vector2f _repulsionVector = new Vector2f();
	static Vector3f _store = new Vector3f();
	
	@Override
	public void update(float time)
	{
		MouseInput.get().setCursorVisible(true);
		super.update(time);
		
		int mouse_x = MouseInput.get().getXAbsolute();
		int mouse_y = MouseInput.get().getYAbsolute();
		
		_store = game.getCamera().getWorldCoordinates(_screenPos.set(mouse_x, mouse_y), 1);
		_store.subtractLocal(game.getCamera().getLocation()).normalizeLocal();
		float dist = game.getCamera().getLocation().y/_store.y;
		_store.multLocal(-dist).addLocal(game.getCamera().getLocation());
		_screenPos.set(_store.x, _store.z);
		//System.out.println("_screenPos:"+_screenPos);
		//System.out.println("_store:"+_store);
		puk.setLocalTranslation(puk.getLocalTranslation().set(_store));
		
		for(Person p1 : game.board.getLiving())
		{
			if(p1.getOwner() == game.p1)
			{
				_repulsionVector.set(p1.getPos()).subtractLocal(_screenPos);
				float repulsion = Person.ZONE + PUK_RADIUS - _repulsionVector.length();
				if (repulsion > 0) {
					_repulsionVector.normalizeLocal().multLocal(repulsion);
					//p1.setPos(p1.getPos().add(repulsionVector));
					System.out.println("_repulsionVector:"+_repulsionVector+", "+p1.getPos()+").subtractLocal("+_screenPos);
					p1.setVelocity(_repulsionVector);
				}
			}
		}
	}
}
