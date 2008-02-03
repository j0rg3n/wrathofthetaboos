package net.hokuspokus.wott.client;

import java.util.ArrayList;
import java.util.List;

import net.hokuspokus.wott.common.HOFEntryGizmo;
import net.hokuspokus.wott.common.Person;
import net.hokuspokus.wott.common.Player;
import net.hokuspokus.wott.common.PlayingMode;
import net.hokuspokus.wott.utils.TextureUtil;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import com.jme.input.InputHandler;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.input.action.InputActionEvent;
import com.jme.input.action.KeyInputAction;
import com.jme.math.FastMath;
import com.jme.math.Vector2f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Cylinder;


public class PukInputHandler extends InputHandler
{
	private PlayingMode game;
	private PukKeyboardHandler p1_keys;
	private PukKeyboardHandler p2_keys;
	
	Vector2f _tmpPuckDiff = new Vector2f();

	public PukInputHandler(PlayingMode game)
	{
		this.game = game;
		
		List<Controller> controllers = new ArrayList<Controller>();
		for (Controller controller : ControllerEnvironment.getDefaultEnvironment().getControllers()) {
			if (!controller.getName().equals("Keyboard") && 
					!controller.getName().equals("Mouse")) {
				controllers.add(controller);
			}
		}
		
		// Setup either keyboard controller or Logitech-controller
		Controller[] logitech_controllers = controllers.toArray(new Controller[0]);
		p1_keys = new PukKeyboardHandler(game.getPlayer(0), game, 
				KeyInput.KEY_UP, KeyInput.KEY_DOWN, KeyInput.KEY_LEFT, KeyInput.KEY_RIGHT, 
				logitech_controllers.length > 0 ? logitech_controllers[0] : null, 
				new Vector2f(0, game.getBoard().getHeight()/2));
		p2_keys = new PukKeyboardHandler(game.getPlayer(1), game, 
				KeyInput.KEY_W, KeyInput.KEY_S, KeyInput.KEY_A, KeyInput.KEY_D, 
				logitech_controllers.length > 1 ? logitech_controllers[1] : null, 
				new Vector2f(game.getBoard().getWidth(), game.getBoard().getHeight()/2));
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
		
		p1_keys.updatePuk(time, game, game.getPlayer(0));
		p2_keys.updatePuk(time, game, game.getPlayer(1));
		
		// Update inter-puk projection
		_tmpPuckDiff.set(p1_keys.puk.getPos()).subtractLocal(p2_keys.puk.getPos());
		float repulsion = PlayerPuk.PUK_RADIUS * 2 - _tmpPuckDiff.length();
		if(repulsion > 0)
		{
			_tmpPuckDiff.normalizeLocal().multLocal(repulsion);

			p1_keys.puk.addRepulsionForce(_tmpPuckDiff);
			_tmpPuckDiff.negateLocal();
			p2_keys.puk.addRepulsionForce(_tmpPuckDiff);
		}
	}
}


/**
 * Puk/Highscore control
 * 
 * @author steam
 */
class PlayerPuk
{
	Spatial puk;
	public static final float PUK_RADIUS = 1.5f;
	private Vector2f pos = new Vector2f();
	PlayingMode game;
	private float xDelta = 0, yDelta = 0;
	Player player;

	public PlayerPuk(PlayingMode game, Player player, Vector2f pos)
	{
		this.player = player;
		this.game = game;
		this.pos.set(pos);
		this.puk = new Node();
		this.puk.getLocalRotation().fromAngles(FastMath.HALF_PI, 0, 0);
		this.puk.getLocalTranslation().set(pos.x, 0.5f, pos.y);
		
		Cylinder cyl = new Cylinder("Puk", 16, 16, 1.5f, 1.5f, true);
		TextureUtil.getInstance().setTexture(cyl, "ressources/2d gfx/puk_"+player.getColor()+".png");
		TextureUtil.getInstance().setAlphaBlending(cyl);
		cyl.getLocalTranslation().set(0, 0, -(0.75f + 0.1f));
		((Node)puk).attachChild(cyl);
		/*
		Cylinder cy2 = new Cylinder("Puk2", 16, 16, 1.5f, 0.f, false);
		TextureUtil.getInstance().setTexture(cyl, "ressources/2d gfx/puk_"+player.getColor()+".png");
		TextureUtil.getInstance().setAlphaBlending(cyl);
		cyl.getLocalTranslation().set(0, 0, -1.5f);
		((Node)puk).attachChild(cyl);
		*/
		
		game.getBoardNode().attachChild(puk);
	}

	public void addRepulsionForce(Vector2f puckDiff) {
		
		if (!game.isGameOver()) {
			pos.addLocal(puckDiff);
		} else {
		}
	}
	
	public void moveRelative(float x, float y) {
		if (!game.isGameOver()) {
			pos.addLocal(x, y);
		} else {
			yDelta += y;
			xDelta += x;

			if (game.getBoard().getWinner() == null) {
				
				// Enough movement goes back to the menu.
				if ((FastMath.abs(yDelta) >= 2.0f) ||
						(FastMath.abs(xDelta) >= 2.0f)) {
					game.setEntryDone(true);
				}
		
			} else if (player == game.getBoard().getWinner()){
				
				HOFEntryGizmo entry = game.getHOFEntry();
				
				if (FastMath.abs(yDelta) >= 1.0f) {
					
					entry.rollActiveLetter((int)Math.signum(yDelta));
					yDelta = 0;
					
				} else if (FastMath.abs(xDelta) >= 2.0f) {
					
					if (xDelta > 0) {
						if (entry.getActiveLetter() < 2) {
							entry.setActiveLetter(entry.getActiveLetter() + 1);
						} else {
							game.setEntryDone(true);
						}
					} else {
						if (entry.getActiveLetter() > 0) {
							entry.setActiveLetter(entry.getActiveLetter() - 1);
						} else {
							//game.setEntryDone(true);
						}
					}
					xDelta = 0;
				}
			}
		}
	}
	
	public Vector2f getPos() {
		return pos;
	}

	public void setPos(Vector2f pos) {
		this.pos = pos;
	}

	public void moveRelative(Vector2f v) {
		moveRelative(v.x, v.y);
	}

	public void update() {
		// Project puk into the board
		pos.x = FastMath.clamp(pos.x, -PlayerPuk.PUK_RADIUS, game.getBoard().getWidth()+PlayerPuk.PUK_RADIUS);
		pos.y = FastMath.clamp(pos.y, -PlayerPuk.PUK_RADIUS, game.getBoard().getHeight()+PlayerPuk.PUK_RADIUS);

		puk.setLocalTranslation(pos.x, 0, pos.y);
	}
}

class PukKeyboardHandler extends InputHandler 
{
	private static final float PUCK_SPEED = 15f;
	PlayerPuk puk;
	private Controller logitech_controller;
	
    public PukKeyboardHandler(Player player, PlayingMode game, int up, int down, int left, int right, Controller logitech_controller, Vector2f pukpos)
    {
        KeyBindingManager keyboard = KeyBindingManager.getKeyBindingManager();

        puk = new PlayerPuk(game, player, pukpos);
        
        if(logitech_controller == null || logitech_controller.getComponents().length < 16)
        {
	        String playerName = player.getColor().name();
	        
			keyboard.set( "up"+playerName, up );
	        keyboard.set( "down"+playerName, down );
	        keyboard.set( "left"+playerName, left );
	        keyboard.set( "right"+playerName, right );
	
	        addAction(new DirectionalAction(new Vector2f(  0,-10), puk), "up"+playerName, true);
	        addAction(new DirectionalAction(new Vector2f(  0, 10), puk), "down"+playerName, true);
	        addAction(new DirectionalAction(new Vector2f(-10,  0), puk), "left"+playerName, true);
	        addAction(new DirectionalAction(new Vector2f( 10,  0), puk), "right"+playerName, true);
        }
        else
        {
            this.logitech_controller = logitech_controller;
        }
    }

	static Vector2f _repulsionVector = new Vector2f();
	public void updatePuk(float dt, PlayingMode game, Player player)
	{
		if(logitech_controller != null)
		{
			if(logitech_controller.poll())
			{
				int xComponentIndex = 14;
				int yComponentIndex = 15;
				for (int i = 0; i < logitech_controller.getComponents().length; ++i) {
					if (logitech_controller.getComponents()[i].getName().equals("X Axis")) {
						xComponentIndex = i;
					}
					if (logitech_controller.getComponents()[i].getName().equals("Y Axis")) {
						yComponentIndex = i;
					}
				}
				puk.moveRelative(
						PUCK_SPEED*dt*logitech_controller.getComponents()[xComponentIndex].getPollData(),
						PUCK_SPEED*dt*logitech_controller.getComponents()[yComponentIndex].getPollData());

				/*
				if(logitech_controller.getComponents()[14].getPollData() != 0.0)
				{
					SoundCenter.getInstance().playSound("ressources/sound/slide"+(1+(Math.abs(FastMath.rand.nextInt())%7))+".wav", null, false);
				}
				*/
				
				/*
				int c = 0;
				for(Component c_sub : logitech_controller.getComponents())
				{
					if(c_sub.getPollData() != 0.0f)
						System.out.println("csub:"+c+":"+c_sub.getName()+":"+c_sub.getPollData());
					c++;
				}
				*/
			}
		}

		puk.update();
		
		for(Person p1 : game.getBoard().getLiving())
		{
			if(p1.getOwner() == player)
			{
				_repulsionVector.set(p1.getPos()).subtractLocal(puk.getPos());
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
    	
    	puk.moveRelative(tempVa.set(dir).multLocal(evt.getTime()));
    }
}
