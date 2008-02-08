package net.hokuspokus.wott.common;

import java.util.ArrayList;
import java.util.List;

import net.hokuspokus.wott.utils.TextureUtil;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.SceneElement;
import com.jme.scene.Text;
import com.jme.scene.state.RenderState;

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
	Vector3f pos = new Vector3f();
	
	Text marker;
	Node rootNode;
	private TABOO current_taboo = TABOO.MIX;
	
	public Node getRootNode() {
		return rootNode;
	}

	public TabooSelector() {
		
		rootNode = new Node();
				
		for (TABOO taboo : TABOO.values()) {
			Text tabooText;
			
			tabooText = TextureUtil.createText( taboo.name() + " label", taboo.name() );

	        rootNode.attachChild(tabooText);
			
			TabooDisplay d = new TabooDisplay(taboo, tabooText);
			taboos.add(d);
		}
		
		marker = TextureUtil.createText("Taboo marker", ">>");

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

	public TABOO getCurrent() {
		return current_taboo ;
	}

	public void nextTaboo(Board board) {
    	int max=0;
    	for(TABOO taboo : TABOO.values())
    	{
    		int violator_count = board.getTabooViolators(taboo).size();
    		if(violator_count > max 
    				|| violator_count == max && taboo != this.current_taboo)
    		{
    			max = violator_count;
    			this.current_taboo = taboo;
    		}
    	}
	}
	
	public void setPos(float x, float y, float z) {
		pos.set(x, y, z);
	}

	public void update() {

		float y = 0;
		
		for (int i = 0; i < taboos.size(); ++i) {
			TabooDisplay d = taboos.get(i);
			
			d.getGeometry().setLocalTranslation(marker.getWidth(), -y, 0.0f);
			
			if (TABOO.values()[i] == current_taboo) {
				marker.setLocalTranslation(0.0f, -y, 0.0f);
			}

			y += marker.getHeight();
		}
	}

	/*
	public void next() {

		++current;
		if (current >= taboos.size()) {
			current = 0;
		}
	}
	*/

	public float getHeight() {
		return marker.getHeight() * taboos.size();
	}
}
