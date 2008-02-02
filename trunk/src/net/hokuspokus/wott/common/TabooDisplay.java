package net.hokuspokus.wott.common;

import net.hokuspokus.wott.common.TabooSelector.TABOO;
import com.jme.scene.Spatial;

public class TabooDisplay {

	Spatial shape;
	TABOO taboo;

	public TabooDisplay(TABOO taboo, Spatial shape) {

		this.taboo = taboo;
		this.shape = shape;
	}

	public Spatial getGeometry() {
		return shape;
	}

	public TABOO getTaboo() {
		return taboo;
	}

	
	
}
