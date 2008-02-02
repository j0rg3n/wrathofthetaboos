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
		WOMAN
	}
	
	List<TabooDisplay> taboos = new ArrayList<TabooDisplay>();
	int current;
	Vector3f pos = new Vector3f();
	
	public TabooSelector() {
		
		current = 0;
		for (TABOO taboo : TABOO.values()) {
			TabooDisplay d = new TabooDisplay(new Vector3f(0, 0, 0), 
					new Sphere(taboo.name(), 10, 10, 1.0f));
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
	
	public void setPos(float x, float y, float z) {
		pos.set(x, y, z);
	}	
}
