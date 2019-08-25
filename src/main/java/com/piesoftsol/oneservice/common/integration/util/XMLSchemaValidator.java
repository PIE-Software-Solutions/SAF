package com.piesoftsol.oneservice.common.integration.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;


public class XMLSchemaValidator {
	
	private static final AppLogger LOGGER = new AppLogger(XMLSchemaValidator.class.getName());
	
	/**
	 * Validates the JSON input against the JSON schema
	 * 
	 * @param inputBytes First param to ProcessingReport
	 * @param schemaStream Second param to ProcessingReport
	 * @return ProcessingReport Returns the report
	 */
	
	@SuppressWarnings("deprecation")
	public static String validate(byte[] inputBytes, InputStream schemaStream) {
		final String methodName = "validate";
		LOGGER.startMethod(methodName);
		String Message = "Success";
		try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(schemaStream));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(new String(inputBytes))));
        } catch (IOException e) {
            // handle exception while reading source
        } catch (SAXException e) {
            Message = e.getMessage();
            LOGGER.error(methodName, "Message: " + e.getMessage());
        }
		return Message;
	}

}
