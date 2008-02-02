package net.hokuspokus.wott.common;

import com.jme.scene.SceneElement;
import com.jme.scene.Text;
import com.jme.scene.state.LightState;
import com.jme.scene.state.TextureState;

public class SuperDuperAssistants {

	public static Text createText(String name, String text) {
		Text t = Text.createDefaultTextLabel(name);
	    t.setCullMode( SceneElement.CULL_NEVER );
	    t.setTextureCombineMode( TextureState.REPLACE );
	    t.setLightCombineMode(LightState.OFF);
	    t.print(text);
	    return t;
	}

}
