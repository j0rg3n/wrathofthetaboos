package net.hokuspokus.wott.common;

import java.util.ArrayList;
import java.util.List;

import com.jme.math.Vector3f;
import com.jme.scene.shape.Sphere;

public class TabooSelector {

	enum TABOO {
		PLENTY,
		MANMAN,
		WOMANWOMAN,
		MANWOMAN,
		MAN,
		WOMAN,
		MAX
	}
	
	List<TabooDisplay> taboos = new ArrayList<TabooDisplay>();
	int current;
	
	public TabooSelector() {
		
		current = 0;
		for (TABOO taboo : TABOO.values()) {
			TabooDisplay d = new TabooDisplay(new Vector3f(0, 0, 0), 
					new Sphere(taboo.name()));
			taboos.add(d);
		}
	}

	public List<TabooDisplay> getDisplays() {
		return taboos;
	}

	public void setDisplays(List<TabooDisplay> taboos) {
		this.taboos = taboos;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}
	
	
}
