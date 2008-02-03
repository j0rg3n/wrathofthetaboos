package net.hokuspokus.wott.common;

import java.util.Arrays;
import java.util.Collections;

import org.omg.PortableInterceptor.INACTIVE;

import net.hokuspokus.wott.common.Player.PlayerColor;
import net.hokuspokus.wott.common.TabooBar.Icon.Mode;
import net.hokuspokus.wott.common.TabooSelector.TABOO;
import net.hokuspokus.wott.utils.SpriteQuad;
import net.hokuspokus.wott.utils.TextureUtil;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Text;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;

public class TabooBar {

	private static final String RES_PATH = "/ressources/2d gfx/";
	
	public static final int TABOO_BAR_HEIGHT = 128;
	public static final int TABOO_BAR_WIDTH = 512;
	public static final int TABOO_BAR_LEFT = 64;
	public static final int TABOO_BAR_TOP = 16;
	public static final int TABOO_BAR_ICON_SIZE = 64;

	private static final int MESSAGE_WIDTH = 512;
	private static final int MESSAGE_HEIGHT = 64;
	private static final int BIG_MESSAGE_HEIGHT = 512;

	static class Icon extends Node {
		public SpriteQuad active, hover;
		
		enum Mode {
			INACTIVE, ACTIVE
		}
		
		float x, y;
		Mode mode;
		
		public Icon(String path) {
			//inactive = TextureUtil.getTransparentQuad("inactive", 
			//		path + ".png", TABOO_BAR_ICON_SIZE, TABOO_BAR_ICON_SIZE); 
			active = TextureUtil.getTransparentQuad("active", 
					path + "_default.png", TABOO_BAR_ICON_SIZE, TABOO_BAR_ICON_SIZE); 
			hover = TextureUtil.getTransparentQuad("hover", 
					path + "_hover.png", TABOO_BAR_ICON_SIZE, TABOO_BAR_ICON_SIZE);
			
			setMode(Mode.INACTIVE);
		}
		
		public Mode getMode() {
			return mode;
		}

		public void setMode(Mode mode) {
			this.mode = mode;

			detachAllChildren();
			
			switch (mode) {
			case INACTIVE:
				attachChild(active);
				break;
			case ACTIVE:
				attachChild(hover);
				break;
			}
		}

		public void setOffset(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public void update() {
			setLocalTranslation(x, y, 0);
		}
	}
	
	private static final String[] IMAGE_FILENAMES = new String[]{
		
		"taboo_1_gay",
		"taboo_2_lesbian",
		"taboo_3_masturbation",
		"taboo_4_menstruation",
		"taboo_5_orgy",
		"taboo_6_wifebeater",
		
		"won_red",
		"won_green",
		"won_none",
	};
	
	private static final TABOO[] TABOO_MAPPING = new TABOO[]{
		TABOO.MEN,
		TABOO.WOMEN,
		TABOO.MAN,
		TABOO.WOMAN,
		TABOO.MIX,
		TABOO.MANWOMAN,
	};
	
	private static final PlayerColor[] COLOR_MAPPING = new PlayerColor[] {
		PlayerColor.RED,
		PlayerColor.GREEN,
	};
	
	SpriteQuad bg;
	Icon[] icons = new Icon[6];
	Node rootNode;
	
	SpriteQuad[] messages = new SpriteQuad[IMAGE_FILENAMES.length];
	Node messageNode = new Node();
	
	public TabooBar() {
		rootNode = new Node();
		rootNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		
		bg = TextureUtil.getTransparentQuad("bg", 
				RES_PATH + "taboo_cloud_background.png", TABOO_BAR_WIDTH, TABOO_BAR_HEIGHT);
		rootNode.attachChild(bg);
		
		for (int i = 0; i < icons.length; ++i) {
			icons[i] = new Icon(RES_PATH + IMAGE_FILENAMES[i]);
			icons[i].setOffset(TABOO_BAR_LEFT + TABOO_BAR_ICON_SIZE * i, -TABOO_BAR_TOP);
			rootNode.attachChild(icons[i]);
		}

		for (int i = 0; i < IMAGE_FILENAMES.length; ++i) {
			messages[i] = TextureUtil.getTransparentQuad("message" + i, RES_PATH + "text_" + IMAGE_FILENAMES[i] + ".png", 
					MESSAGE_WIDTH, i < icons.length ? MESSAGE_HEIGHT : BIG_MESSAGE_HEIGHT);
		}
		
		rootNode.attachChild(messageNode);
		messageNode.setLocalTranslation(
				(TABOO_BAR_WIDTH - MESSAGE_WIDTH) * .5f,
				-(TABOO_BAR_TOP + TABOO_BAR_ICON_SIZE * 1.1f), 0);
	}
	
	public void setActiveTaboo(TABOO taboo) {
		
		for (int i = 0; i < icons.length; ++i) {
			
			icons[i].setMode(TABOO_MAPPING[i] == taboo ? Mode.ACTIVE : Mode.INACTIVE);
			
			if (TABOO_MAPPING[i] == taboo) {
				setMessage(i);
			}
		}
	}
	
	public void setWinner(PlayerColor color) {
		for (int i = 0; i < COLOR_MAPPING.length; ++i) {
			if (COLOR_MAPPING[i] == color) {
				setWinnerMessage(i);
			}
		}
	}

	public void setNoWinner() {
		setWinnerMessage(COLOR_MAPPING.length);
	}
	
	protected void setWinnerMessage(int index) {
		for (Icon icon : icons) {
			icon.setMode(Mode.INACTIVE);
		}

		setMessage(icons.length + index);
	}
	
	private void setMessage(int i) {
		messageNode.detachAllChildren();
		messageNode.attachChild(messages[i]);
	}

	public void update() {
		rootNode.setLocalTranslation(
				(DisplaySystem.getDisplaySystem().getWidth() - TABOO_BAR_WIDTH) * .5f,
				DisplaySystem.getDisplaySystem().getHeight() - TABOO_BAR_HEIGHT * .01f, 0);
		for (Icon icon : icons) {
			icon.update();
		}
		rootNode.updateGeometricState(0, false);
	}

	public Node getRootNode() {
		return rootNode;
	}


}
