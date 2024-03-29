/*
 * Copyright (c) 2003-2006 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jmex.game.state;

import com.jme.scene.Node;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;

/**
 * <code>BasicGameStateNode</code> this is identical to BasicGameState except it allows
 * you to add additional GameStates as children beneath it.
 * 
 * @author Matthew D. Hicks
 */
public class BasicGameStateNode extends GameStateNode<BasicGameState> {
	
	/** The root of this GameStates scenegraph. */
	protected Node rootNode;

	/**
	 * Creates a new BasicGameStateNode with a given name.
	 * 
	 * @param name The name of this GameState.
	 */
	public BasicGameStateNode(String name) {
		super(name);
		rootNode = new Node(name + ": RootNode");
		ZBufferState buf = DisplaySystem.getDisplaySystem().getRenderer().createZBufferState();
        buf.setEnabled(true);
        buf.setFunction(ZBufferState.CF_LEQUAL);
        rootNode.setRenderState(buf);
	}
	
	/**
	 * Updates the rootNode.
	 * 
	 * @see GameState#update(float)
	 */
	public void update(float tpf) {
		super.update(tpf);
		rootNode.updateGeometricState(tpf, true);
	}

	/**
	 * Draws the rootNode.
	 * 
	 * @see GameState#render(float)
	 */
	public void render(float tpf) {
		super.render(tpf);
		DisplaySystem.getDisplaySystem().getRenderer().draw(rootNode);
	}
	
	/**
	 * Empty.
	 * 
	 * @see GameState#cleanup()
	 */
	public void cleanup() {
		super.cleanup();
        //do nothing
	}
	
	public Node getRootNode() {
		return rootNode;
	}
}
