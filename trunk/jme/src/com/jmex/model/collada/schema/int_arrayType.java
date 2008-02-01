/**
 * int_arrayType.java
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

import com.jmex.xml.types.SchemaID;
import com.jmex.xml.types.SchemaInteger;
import com.jmex.xml.types.SchemaNCName;
import com.jmex.xml.types.SchemaType;

public class int_arrayType extends com.jmex.xml.xml.Node {

	public int_arrayType(int_arrayType node) {
		super(node);
	}

	public int_arrayType(org.w3c.dom.Node node) {
		super(node);
	}

	public int_arrayType(org.w3c.dom.Document doc) {
		super(doc);
	}

	public int_arrayType(com.jmex.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
		super(doc, namespaceURI, prefix, name);
	}
	

	public ListOfInts getValue() {
		return new ListOfInts(getDomNodeValue(domNode));
	}

	public void setValue(SchemaType value) {
		setDomNodeValue(domNode, value.toString());
	}

	public void assign(SchemaType value) {
		setValue(value);
	}

	public void adjustPrefix() {
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Attribute, null, "id" );
				tmpNode != null;
				tmpNode = getDomNextChild( Attribute, null, "id", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, false);
		}
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Attribute, null, "name" );
				tmpNode != null;
				tmpNode = getDomNextChild( Attribute, null, "name", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, false);
		}
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Attribute, null, "count" );
				tmpNode != null;
				tmpNode = getDomNextChild( Attribute, null, "count", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, false);
		}
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Attribute, null, "minInclusive" );
				tmpNode != null;
				tmpNode = getDomNextChild( Attribute, null, "minInclusive", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, false);
		}
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Attribute, null, "maxInclusive" );
				tmpNode != null;
				tmpNode = getDomNextChild( Attribute, null, "maxInclusive", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, false);
		}
	}
	public void setXsiType() {
 		org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
		el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "int_array");
	}

	public static int getidMinCount() {
		return 0;
	}

	public static int getidMaxCount() {
		return 1;
	}

	public int getidCount() {
		return getDomChildCount(Attribute, null, "id");
	}

	public boolean hasid() {
		return hasDomChild(Attribute, null, "id");
	}

	public SchemaID newid() {
		return new SchemaID();
	}

	public SchemaID getidAt(int index) throws Exception {
		return new SchemaID(getDomNodeValue(getDomChildAt(Attribute, null, "id", index)));
	}

	public org.w3c.dom.Node getStartingidCursor() throws Exception {
		return getDomFirstChild(Attribute, null, "id" );
	}

	public org.w3c.dom.Node getAdvancedidCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Attribute, null, "id", curNode );
	}

	public SchemaID getidValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new SchemaID(getDomNodeValue(curNode));
	}

	public SchemaID getid() throws Exception 
 {
		return getidAt(0);
	}

	public void removeidAt(int index) {
		removeDomChildAt(Attribute, null, "id", index);
	}

	public void removeid() {
		removeidAt(0);
	}

	public org.w3c.dom.Node addid(SchemaID value) {
		if( value.isNull() )
			return null;

		return  appendDomChild(Attribute, null, "id", value.toString());
	}

	public org.w3c.dom.Node addid(String value) throws Exception {
		return addid(new SchemaID(value));
	}

	public void insertidAt(SchemaID value, int index) {
		insertDomChildAt(Attribute, null, "id", index, value.toString());
	}

	public void insertidAt(String value, int index) throws Exception {
		insertidAt(new SchemaID(value), index);
	}

	public void replaceidAt(SchemaID value, int index) {
		replaceDomChildAt(Attribute, null, "id", index, value.toString());
	}

	public void replaceidAt(String value, int index) throws Exception {
		replaceidAt(new SchemaID(value), index);
	}

	public static int getnameMinCount() {
		return 0;
	}

	public static int getnameMaxCount() {
		return 1;
	}

	public int getnameCount() {
		return getDomChildCount(Attribute, null, "name");
	}

	public boolean hasname() {
		return hasDomChild(Attribute, null, "name");
	}

	public SchemaNCName newname() {
		return new SchemaNCName();
	}

	public SchemaNCName getnameAt(int index) throws Exception {
		return new SchemaNCName(getDomNodeValue(getDomChildAt(Attribute, null, "name", index)));
	}

	public org.w3c.dom.Node getStartingnameCursor() throws Exception {
		return getDomFirstChild(Attribute, null, "name" );
	}

	public org.w3c.dom.Node getAdvancednameCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Attribute, null, "name", curNode );
	}

	public SchemaNCName getnameValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new SchemaNCName(getDomNodeValue(curNode));
	}

	public SchemaNCName getname() throws Exception 
 {
		return getnameAt(0);
	}

	public void removenameAt(int index) {
		removeDomChildAt(Attribute, null, "name", index);
	}

	public void removename() {
		removenameAt(0);
	}

	public org.w3c.dom.Node addname(SchemaNCName value) {
		if( value.isNull() )
			return null;

		return  appendDomChild(Attribute, null, "name", value.toString());
	}

	public org.w3c.dom.Node addname(String value) throws Exception {
		return addname(new SchemaNCName(value));
	}

	public void insertnameAt(SchemaNCName value, int index) {
		insertDomChildAt(Attribute, null, "name", index, value.toString());
	}

	public void insertnameAt(String value, int index) throws Exception {
		insertnameAt(new SchemaNCName(value), index);
	}

	public void replacenameAt(SchemaNCName value, int index) {
		replaceDomChildAt(Attribute, null, "name", index, value.toString());
	}

	public void replacenameAt(String value, int index) throws Exception {
		replacenameAt(new SchemaNCName(value), index);
	}

	public static int getcountMinCount() {
		return 1;
	}

	public static int getcountMaxCount() {
		return 1;
	}

	public int getcountCount() {
		return getDomChildCount(Attribute, null, "count");
	}

	public boolean hascount() {
		return hasDomChild(Attribute, null, "count");
	}

	public uint newcount() {
		return new uint();
	}

	public uint getcountAt(int index) throws Exception {
		return new uint(getDomNodeValue(getDomChildAt(Attribute, null, "count", index)));
	}

	public org.w3c.dom.Node getStartingcountCursor() throws Exception {
		return getDomFirstChild(Attribute, null, "count" );
	}

	public org.w3c.dom.Node getAdvancedcountCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Attribute, null, "count", curNode );
	}

	public uint getcountValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new uint(getDomNodeValue(curNode));
	}

	public uint getcount() throws Exception 
 {
		return getcountAt(0);
	}

	public void removecountAt(int index) {
		removeDomChildAt(Attribute, null, "count", index);
	}

	public void removecount() {
		removecountAt(0);
	}

	public org.w3c.dom.Node addcount(uint value) {
		if( value.isNull() )
			return null;

		return  appendDomChild(Attribute, null, "count", value.toString());
	}

	public org.w3c.dom.Node addcount(String value) throws Exception {
		return addcount(new uint(value));
	}

	public void insertcountAt(uint value, int index) {
		insertDomChildAt(Attribute, null, "count", index, value.toString());
	}

	public void insertcountAt(String value, int index) throws Exception {
		insertcountAt(new uint(value), index);
	}

	public void replacecountAt(uint value, int index) {
		replaceDomChildAt(Attribute, null, "count", index, value.toString());
	}

	public void replacecountAt(String value, int index) throws Exception {
		replacecountAt(new uint(value), index);
	}

	public static int getminInclusiveMinCount() {
		return 0;
	}

	public static int getminInclusiveMaxCount() {
		return 1;
	}

	public int getminInclusiveCount() {
		return getDomChildCount(Attribute, null, "minInclusive");
	}

	public boolean hasminInclusive() {
		return hasDomChild(Attribute, null, "minInclusive");
	}

	public SchemaInteger newminInclusive() {
		return new SchemaInteger();
	}

	public SchemaInteger getminInclusiveAt(int index) throws Exception {
		return new SchemaInteger(getDomNodeValue(getDomChildAt(Attribute, null, "minInclusive", index)));
	}

	public org.w3c.dom.Node getStartingminInclusiveCursor() throws Exception {
		return getDomFirstChild(Attribute, null, "minInclusive" );
	}

	public org.w3c.dom.Node getAdvancedminInclusiveCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Attribute, null, "minInclusive", curNode );
	}

	public SchemaInteger getminInclusiveValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new SchemaInteger(getDomNodeValue(curNode));
	}

	public SchemaInteger getminInclusive() throws Exception 
 {
		return getminInclusiveAt(0);
	}

	public void removeminInclusiveAt(int index) {
		removeDomChildAt(Attribute, null, "minInclusive", index);
	}

	public void removeminInclusive() {
		removeminInclusiveAt(0);
	}

	public org.w3c.dom.Node addminInclusive(SchemaInteger value) {
		if( value.isNull() )
			return null;

		return  appendDomChild(Attribute, null, "minInclusive", value.toString());
	}

	public org.w3c.dom.Node addminInclusive(String value) throws Exception {
		return addminInclusive(new SchemaInteger(value));
	}

	public void insertminInclusiveAt(SchemaInteger value, int index) {
		insertDomChildAt(Attribute, null, "minInclusive", index, value.toString());
	}

	public void insertminInclusiveAt(String value, int index) throws Exception {
		insertminInclusiveAt(new SchemaInteger(value), index);
	}

	public void replaceminInclusiveAt(SchemaInteger value, int index) {
		replaceDomChildAt(Attribute, null, "minInclusive", index, value.toString());
	}

	public void replaceminInclusiveAt(String value, int index) throws Exception {
		replaceminInclusiveAt(new SchemaInteger(value), index);
	}

	public static int getmaxInclusiveMinCount() {
		return 0;
	}

	public static int getmaxInclusiveMaxCount() {
		return 1;
	}

	public int getmaxInclusiveCount() {
		return getDomChildCount(Attribute, null, "maxInclusive");
	}

	public boolean hasmaxInclusive() {
		return hasDomChild(Attribute, null, "maxInclusive");
	}

	public SchemaInteger newmaxInclusive() {
		return new SchemaInteger();
	}

	public SchemaInteger getmaxInclusiveAt(int index) throws Exception {
		return new SchemaInteger(getDomNodeValue(getDomChildAt(Attribute, null, "maxInclusive", index)));
	}

	public org.w3c.dom.Node getStartingmaxInclusiveCursor() throws Exception {
		return getDomFirstChild(Attribute, null, "maxInclusive" );
	}

	public org.w3c.dom.Node getAdvancedmaxInclusiveCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Attribute, null, "maxInclusive", curNode );
	}

	public SchemaInteger getmaxInclusiveValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new SchemaInteger(getDomNodeValue(curNode));
	}

	public SchemaInteger getmaxInclusive() throws Exception 
 {
		return getmaxInclusiveAt(0);
	}

	public void removemaxInclusiveAt(int index) {
		removeDomChildAt(Attribute, null, "maxInclusive", index);
	}

	public void removemaxInclusive() {
		removemaxInclusiveAt(0);
	}

	public org.w3c.dom.Node addmaxInclusive(SchemaInteger value) {
		if( value.isNull() )
			return null;

		return  appendDomChild(Attribute, null, "maxInclusive", value.toString());
	}

	public org.w3c.dom.Node addmaxInclusive(String value) throws Exception {
		return addmaxInclusive(new SchemaInteger(value));
	}

	public void insertmaxInclusiveAt(SchemaInteger value, int index) {
		insertDomChildAt(Attribute, null, "maxInclusive", index, value.toString());
	}

	public void insertmaxInclusiveAt(String value, int index) throws Exception {
		insertmaxInclusiveAt(new SchemaInteger(value), index);
	}

	public void replacemaxInclusiveAt(SchemaInteger value, int index) {
		replaceDomChildAt(Attribute, null, "maxInclusive", index, value.toString());
	}

	public void replacemaxInclusiveAt(String value, int index) throws Exception {
		replacemaxInclusiveAt(new SchemaInteger(value), index);
	}

}
