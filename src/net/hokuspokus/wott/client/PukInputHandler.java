package net.hokuspokus.wott.client;

import net.hokuspokus.wott.common.Person;
import net.hokuspokus.wott.common.Player;
import net.hokuspokus.wott.utils.TextureUtil;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyBackwardAction;
import com.jme.input.action.KeyForwardAction;
import com.jme.input.action.KeyInputAction;
import com.jme.input.action.KeyLookDownAction;
import com.jme.input.action.KeyLookUpAction;
import com.jme.input.action.KeyRotateLeftAction;
import com.jme.input.action.KeyRotateRightAction;
import com.jme.input.action.KeyStrafeDownAction;
import com.jme.input.action.KeyStrafeLeftAction;
import com.jme.input.action.KeyStrafeRightAction;
import com.jme.input.action.KeyStrafeUpAction;
import com.jme.input.joystick.JoystickInput;
import com.jme.math.FastMath;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Cylinder;


public class PukInputHandler extends InputHandler
{
	private WrathOfTaboo game;
	private PukKeyboardHandler p1_keys;
	private PukKeyboardHandler p2_keys;
	
	Vector2f _tmpPuckDiff = new Vector2f();

	public PukInputHandler(WrathOfTaboo game)
	{
		this.game = game;
		
		// Setup either keyboard controller or Logitech-controller
		Controller[] logitech_controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
		p1_keys = new PukKeyboardHandler(game.p1, game, KeyInput.KEY_UP, KeyInput.KEY_DOWN, KeyInput.KEY_LEFT, KeyInput.KEY_RIGHT, logitech_controllers.length > 0 ? logitech_controllers[0] : null);
		p2_keys = new PukKeyboardHandler(game.p2, game, KeyInput.KEY_W, KeyInput.KEY_S, KeyInput.KEY_A, KeyInput.KEY_D, logitech_controllers.length > 1 ? logitech_controllers[1] : null);
		addToAttachedHandlers(p1_keys);
		addToAttachedHandlers(p2_keys);
	}
	
	@Override
	public void update(float time)
	{
		MouseInput.get().setCursorVisible(true);
		super.update(time);
		/*
		
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
		
		*/
		
		//System.out.println("JoystickInput.get().getDefaultJoystick():"+JoystickInput.get().getDefaultJoystick().getAxisCount());
		//
		
		p1_keys.updatePuk(time, game, game.p1);
		p2_keys.updatePuk(time, game, game.p2);
		
		// Update inter-puk projection
		_tmpPuckDiff.set(p1_keys.puk.pos).subtractLocal(p2_keys.puk.pos);
		float repulsion = PlayerPuk.PUK_RADIUS * 2 - _tmpPuckDiff.length();
		if(repulsion > 0)
		{
			_tmpPuckDiff.normalizeLocal().multLocal(repulsion);
			p1_keys.puk.pos.addLocal(_tmpPuckDiff);
			p2_keys.puk.pos.subtractLocal(_tmpPuckDiff);
		}
	}
}


class PlayerPuk
{
	Spatial puk;
	public static final float PUK_RADIUS = 1.5f;
	Vector2f pos = new Vector2f();

	public PlayerPuk(WrathOfTaboo game, Player player)
	{
		this.puk = new Cylinder("Puk", 16, 16, 1.5f, 0.4f, true);
		this.puk.getLocalRotation().fromAngles(FastMath.HALF_PI, 0, 0);
		TextureUtil.getInstance().setTexture(puk, "2d gfx/player_"+player.getColor()+".jpg");
		game.getBoardNode().attachChild(puk);
	}
}

class PukKeyboardHandler extends InputHandler 
{
	PlayerPuk puk;
	private Controller logitech_controller;
	
    public PukKeyboardHandler(Player player, WrathOfTaboo game, int up, int down, int left, int right, Controller logitech_controller)
    {
        KeyBindingManager keyboard = KeyBindingManager.getKeyBindingManager();

        puk = new PlayerPuk(game, player);
        
        if(logitech_controller == null || logitech_controller.getComponents().length < 16)
        {
	        keyboard.set( "up"+up, up );
	        keyboard.set( "down"+down, down );
	        keyboard.set( "left"+left, left );
	        keyboard.set( "right"+right, right );
	
	        addAction(new DirectionalAction(new Vector2f(  0,-10), puk), "up"+up, true);
	        addAction(new DirectionalAction(new Vector2f(  0, 10), puk), "down"+down, true);
	        addAction(new DirectionalAction(new Vector2f(-10,  0), puk), "left"+left, true);
	        addAction(new DirectionalAction(new Vector2f( 10,  0), puk), "right"+right, true);
        }
        else
        {
            this.logitech_controller = logitech_controller;
        }
    }

	static Vector2f _repulsionVector = new Vector2f();
	public void updatePuk(float dt, WrathOfTaboo game, Player player)
	{
		if(logitech_controller != null)
		{
			if(logitech_controller.poll())
			{
				int c = 0;
				puk.pos.addLocal(
						15*dt*logitech_controller.getComponents()[14].getPollData(),
						15*dt*logitech_controller.getComponents()[15].getPollData());
				/*
				for(Component c_sub : logitech_controller.getComponents())
				{
					if(c_sub.getPollData() != 0.0f)
						System.out.println("csub:"+c+":"+c_sub.getName()+":"+c_sub.getPollData());
					c++;
				}
				*/
			}
		}
		puk.puk.setLocalTranslation(puk.pos.x, 0, puk.pos.y);
		
		for(Person p1 : game.board.getLiving())
		{
			if(p1.getOwner() == player)
			{
				_repulsionVector.set(p1.getPos()).subtractLocal(puk.pos);
				float repulsion = Person.ZONE + PlayerPuk.PUK_RADIUS - _repulsionVector.length();
				if (repulsion > 0)
				{
					_repulsionVector.normalizeLocal().multLocal(repulsion);
					//p1.setPos(p1.getPos().add(repulsionVector));
					//System.out.println("_repulsionVector:"+_repulsionVector+", "+p1.getPos()+").subtractLocal("+_screenPos);
					p1.setVelocity(_repulsionVector);
				}
			}
		}
	}
}

class DirectionalAction extends KeyInputAction
{
    //temp holder for the multiplication of the direction and time
    private static final Vector2f tempVa = new Vector2f();
    private final Vector2f dir;
	private PlayerPuk puk;

    public DirectionalAction(Vector2f dir, PlayerPuk puk)
    {
        this.dir = dir;
        this.puk = puk;
    }

    public void performAction(InputActionEvent evt)
    {
    	puk.pos.addLocal(tempVa.set(dir).multLocal(evt.getTime()));
    }
}
