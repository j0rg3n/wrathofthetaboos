package net.hokuspokus.wott.client;

import net.hokuspokus.wott.common.Person;

import com.jme.input.InputHandler;
import com.jme.input.MouseInput;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Sphere;

public class PukInputHandler extends InputHandler
{
	private WrathOfTaboo game;
	private Sphere puk;

	public PukInputHandler(WrathOfTaboo game)
	{
		this.game = game;
		this.puk = new Sphere("Puk", 16, 16, 1);
		game.getBoardNode().attachChild(puk);
	}
	
	static Vector2f _screenPos = new Vector2f();
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
			float repulsion = Person.ZONE * 3 - p1.getPos().subtract(_screenPos).length();
			if (repulsion > 0) {
				Vector2f repulsionVector = 
					p1.getPos()
					.subtract(_screenPos)
					.normalize()
					.mult(repulsion);
				//p1.setPos(p1.getPos().add(repulsionVector));
				p1.setVelocity(repulsionVector);
			}
		}
	}
}
