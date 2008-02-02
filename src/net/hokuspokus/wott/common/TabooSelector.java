package net.hokuspokus.wott.common;

import java.util.ArrayList;
import java.util.List;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.SceneElement;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.scene.shape.Arrow;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;

public class TabooSelector {

	enum TABOO {
		MIX,
		MEN,
		WOMEN,
		MANWOMAN,
		MAN,
		WOMAN
	}
	
	List<TabooDisplay> taboos = new ArrayList<TabooDisplay>();
	int current;
	Vector3f pos = new Vector3f();
	
	Text marker;
	Node rootNode;
	
	public Node getRootNode() {
		return rootNode;
	}

	public TabooSelector() {
		
		rootNode = new Node();
				
		current = 0;
		for (TABOO taboo : TABOO.values()) {
			Text tabooText;
			
			tabooText = Text.createDefaultTextLabel( taboo.name() + " label" );
		    tabooText.setCullMode( SceneElement.CULL_NEVER );
		    tabooText.setTextureCombineMode( TextureState.REPLACE );
	        tabooText.print( taboo.name() );

	        rootNode.attachChild(tabooText);
			
			TabooDisplay d = new TabooDisplay(taboo, tabooText);
			taboos.add(d);
		}
		
		marker = Text.createDefaultTextLabel( "Taboo marker");
	    marker.setCullMode( SceneElement.CULL_NEVER );
	    marker.setTextureCombineMode( TextureState.REPLACE );
        marker.print( ">>" );

        rootNode.attachChild(marker);
        
        rootNode.setRenderState( marker.getRenderState( RenderState.RS_ALPHA ) );
        rootNode.setRenderState( marker.getRenderState( RenderState.RS_TEXTURE ) );
        rootNode.setCullMode( SceneElement.CULL_NEVER );
	}

	public List<TabooDisplay> getDisplays() {
		return taboos;
	}

	public void setDisplays(List<TabooDisplay> taboos) {
		this.taboos = taboos;
	}

	public int getCurrentIndex() {
		return current;
	}
	
	public TABOO getCurrent() {
		return taboos.get(current).getTaboo();
	}

	public void setCurrent(int current) {
		this.current = current;
	}
	
	public void setPos(float x, float y, float z) {
		pos.set(x, y, z);
	}

	public void update() {

		float y = 0;
		
		for (int i = 0; i < taboos.size(); ++i) {
			TabooDisplay d = taboos.get(i);
			
			y += 100.0f;
			d.getGeometry().setLocalTranslation(marker.getWidth(), 750.0f - y, 0.0f);
			
			if (i == current) {
				marker.setLocalTranslation(0.0f, 750.0f - y, 0.0f);
			}
		}
	}

	public void next() {

		++current;
		if (current >= taboos.size()) {
			current = 0;
		}
	}
}
