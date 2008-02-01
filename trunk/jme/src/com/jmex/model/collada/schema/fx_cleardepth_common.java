/**
 * fx_cleardepth_common.java
 *
 * This file was generated by XMLSpy 2007sp2 Enterprise Edition.
 *
 * YOU SHOULD NOT MODIFY THIS FILE, BECAUSE IT WILL BE
 * OVERWRITTEN WHEN YOU RE-RUN CODE GENERATION.
 *
 * Refer to the XMLSpy Documentation for further details.
 * http://www.altova.com/xmlspy
 */


package com.jmex.model.collada.schema;

import com.jmex.xml.types.SchemaInteger;
import com.jmex.xml.types.SchemaType;

public class fx_cleardepth_common extends com.jmex.xml.xml.Node {

	public fx_cleardepth_common(fx_cleardepth_common node) {
		super(node);
	}

	public fx_cleardepth_common(org.w3c.dom.Node node) {
		super(node);
	}

	public fx_cleardepth_common(org.w3c.dom.Document doc) {
		super(doc);
	}

	public fx_cleardepth_common(com.jmex.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
		super(doc, namespaceURI, prefix, name);
	}
	

	public float2 getValue() {
		return new float2(getDomNodeValue(domNode));
	}

	public void setValue(SchemaType value) {
		setDomNodeValue(domNode, value.toString());
	}

	public void assign(SchemaType value) {
		setValue(value);
	}

	public void adjustPrefix() {
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Attribute, null, "index" );
				tmpNode != null;
				tmpNode = getDomNextChild( Attribute, null, "index", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, false);
		}
	}
	public void setXsiType() {
 		org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
		el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "fx_cleardepth_common");
	}

	public static int getindexMinCount() {
		return 0;
	}

	public static int getindexMaxCount() {
		return 1;
	}

	public int getindexCount() {
		return getDomChildCount(Attribute, null, "index");
	}

	public boolean hasindex() {
		return hasDomChild(Attribute, null, "index");
	}

	public SchemaInteger newindex() {
		return new SchemaInteger();
	}

	public SchemaInteger getindexAt(int index) throws Exception {
		return new SchemaInteger(getDomNodeValue(getDomChildAt(Attribute, null, "index", index)));
	}

	public org.w3c.dom.Node getStartingindexCursor() throws Exception {
		return getDomFirstChild(Attribute, null, "index" );
	}

	public org.w3c.dom.Node getAdvancedindexCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Attribute, null, "index", curNode );
	}

	public SchemaInteger getindexValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new SchemaInteger(getDomNodeValue(curNode));
	}

	public SchemaInteger getindex() throws Exception 
 {
		return getindexAt(0);
	}

	public void removeindexAt(int index) {
		removeDomChildAt(Attribute, null, "index", index);
	}

	public void removeindex() {
		removeindexAt(0);
	}

	public org.w3c.dom.Node addindex(SchemaInteger value) {
		if( value.isNull() )
			return null;

		return  appendDomChild(Attribute, null, "index", value.toString());
	}

	public org.w3c.dom.Node addindex(String value) throws Exception {
		return addindex(new SchemaInteger(value));
	}

	public void insertindexAt(SchemaInteger value, int index) {
		insertDomChildAt(Attribute, null, "index", index, value.toString());
	}

	public void insertindexAt(String value, int index) throws Exception {
		insertindexAt(new SchemaInteger(value), index);
	}

	public void replaceindexAt(SchemaInteger value, int index) {
		replaceDomChildAt(Attribute, null, "index", index, value.toString());
	}

	public void replaceindexAt(String value, int index) throws Exception {
		replaceindexAt(new SchemaInteger(value), index);
	}

}
