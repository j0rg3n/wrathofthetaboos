package net.hokuspokus.wott.common;

import net.hokuspokus.wott.utils.TextureUtil;

import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.shape.Quad;
import com.jme.system.DisplaySystem;

public class TabooBar {

	private static final int BG_HEIGHT = 171;
	private static final int BG_WIDTH = 748;
	private static final int TABOO_BAR_LEFT = 228;
	private static final int TABOO_BAR_TOP = 57;
	private static final int TABOO_BAR_ICON_SIZE = 57;

	class Icon {
		public Quad inactive, active, hover;
		
		public Icon(String path) {
			inactive = TextureUtil.getTransparentQuad("inactive", 
					path + ".jpg", TABOO_BAR_ICON_SIZE, TABOO_BAR_ICON_SIZE); 
			active = TextureUtil.getTransparentQuad("active", 
					path + "_default.jpg", TABOO_BAR_ICON_SIZE, TABOO_BAR_ICON_SIZE); 
			hover = TextureUtil.getTransparentQuad("hover", 
					path + "_hover.jpg", TABOO_BAR_ICON_SIZE, TABOO_BAR_ICON_SIZE); 
		}
		
		public void attachAll(Node node) {
			node.attachChild(inactive);
			node.attachChild(active);
			node.attachChild(hover);
		}

		public void setOffset(int x, int y) {
			inactive.setLocalTranslation(x, y, 0);
			active.setLocalTranslation(x, y, 0);
			hover.setLocalTranslation(x, y, 0);
		}
	}
	
	private static final String[] ICON = new String[]{
		"/ressources/2d gfx/taboo_1_gay",
		"/ressources/2d gfx/taboo_2_lesbian",
		"/ressources/2d gfx/taboo_3_masturbation",
		"/ressources/2d gfx/taboo_4_menstruation",
		"/ressources/2d gfx/taboo_5_orgy",
		"/ressources/2d gfx/taboo_6_wifebeater",
	};
	
	Quad bg;
	Icon[] icons = new Icon[6];
	Node rootNode;
	
	public TabooBar() {
		rootNode = new Node();
		rootNode.setRenderQueueMode(Renderer.QUEUE_ORTHO);
		
		bg = TextureUtil.getTransparentQuad("bg", 
				"/ressources/2d gfx/taboo_cloud_background.png", BG_WIDTH, BG_HEIGHT);
		rootNode.attachChild(bg);
		
		for (int i = 0; i < icons.length; ++i) {
			icons[i] = new Icon(ICON[i]);
			icons[i].attachAll(rootNode);
			icons[i].setOffset( TABOO_BAR_ICON_SIZE * i, 
					TABOO_BAR_TOP - TABOO_BAR_ICON_SIZE);
		}
	}
	
	public void update() {
		rootNode.setLocalTranslation(
				DisplaySystem.getDisplaySystem().getWidth() * .5f,
				DisplaySystem.getDisplaySystem().getHeight() * .8f, 0);
		rootNode.updateGeometricState(0, false);
	}

	public Node getRootNode() {
		return rootNode;
	}
	
	
}
