package com.demo.springboot.util;

import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonWrapper {
	public static JsonFactory factory = new JsonFactory();
	private ByteArrayOutputStream  baos = null;
	private PrintWriter pw = null;
	private JsonGenerator generator = null;
	private ObjectMapper objectMapper;
	public JsonWrapper() throws Exception {
		baos = new ByteArrayOutputStream();
		pw = new PrintWriter(baos);
		generator = factory.createJsonGenerator(pw);
	}
	
	public JsonWrapper(boolean objMapper) {
		objectMapper = new ObjectMapper();
	}
	
	public void writeStartObject() throws Exception {
		generator.writeStartObject();
	}
	
	public void writeStartArray() throws Exception {
		generator.writeStartArray();
	}
	
	public void writeEndObject() throws Exception {
		generator.writeEndObject();
	}
	
	public void writeEndArray() throws Exception {
		generator.writeEndArray();
	}
	
	public void writeStringField(String fieldName,String value) throws Exception{
		generator.writeStringField(fieldName, value);
	}
	
	public void writeNumberField(String fieldName,Long value) throws Exception{
		generator.writeNumberField(fieldName, value);
	}
	
	public void writeObjectFieldStart(String fieldName) throws Exception{
		generator.writeObjectFieldStart(fieldName);
	}
	
	public void writeArrayFieldStart(String fieldName) throws Exception{
		generator.writeArrayFieldStart(fieldName);
	}
	
	public void writeRaw(String text) throws Exception{
		generator.writeRaw(text);
	}
	
	public void writeRaw(String text,int offset,int len) throws Exception{
		generator.writeRaw(text, offset, len);
	}
	
	public String getJsonValue(){
		return new String(baos.toByteArray());
	}
		
	public void close() throws Exception {
		if(generator != null)
			generator.close();
		if(pw != null) 
			pw.close();
		if(baos != null)
			baos.close();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object readObject(String jsonString, Class clazz) throws Exception {
		ByteArrayInputStream bais = new ByteArrayInputStream(jsonString.getBytes());
		Object obj = objectMapper.readValue(bais, clazz);
		bais.close();
		return obj;
	}
	
	public String writeObject (Object object) throws Exception  {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		objectMapper.writeValue(baos, object);
		baos.close();
		return baos.toString();
	}
}
