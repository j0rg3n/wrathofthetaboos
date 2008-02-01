/**
 * fx_pipeline_stage_common.java
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

import com.jmex.xml.types.SchemaString;

public class fx_pipeline_stage_common extends SchemaString {
	public static final int EVERTEXPROGRAM = 0; /* VERTEXPROGRAM */
	public static final int EFRAGMENTPROGRAM = 1; /* FRAGMENTPROGRAM */
	public static final int EVERTEXSHADER = 2; /* VERTEXSHADER */
	public static final int EPIXELSHADER = 3; /* PIXELSHADER */

	public static String[] sEnumValues = {
		"VERTEXPROGRAM",
		"FRAGMENTPROGRAM",
		"VERTEXSHADER",
		"PIXELSHADER",
	};

	public fx_pipeline_stage_common() {
		super();
	}

	public fx_pipeline_stage_common(String newValue) {
		super(newValue);
		validate();
	}

	public fx_pipeline_stage_common(SchemaString newValue) {
		super(newValue);
		validate();
	}

	public static int getEnumerationCount() {
		return sEnumValues.length;
	}

	public static String getEnumerationValue(int index) {
		return sEnumValues[index];
	}

	public static boolean isValidEnumerationValue(String val) {
		for (int i = 0; i < sEnumValues.length; i++) {
			if (val.equals(sEnumValues[i]))
				return true;
		}
		return false;
	}

	public void validate() {

		if (!isValidEnumerationValue(toString()))
			throw new com.jmex.xml.xml.XmlException("Value of fx_pipeline_stage_common is invalid.");
	}
}
