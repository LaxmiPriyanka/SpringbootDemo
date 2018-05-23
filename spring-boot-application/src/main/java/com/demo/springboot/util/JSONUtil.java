package com.demo.springboot.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JSONUtil {
	
	public static Object parseJSON(String jsonString,String paramName, String paramType){
		return parseJSON(jsonString, paramName, paramType, true);
	}
	
	
	public static Object parseJSON(String jsonString,String paramName, String paramType, boolean removeQuotes) {
		String[] params = paramName.split("\\.");
		Object paramValue = null;
		JsonNode tempValue = null;
		try{
			ObjectMapper mapper = new ObjectMapper();
			for(String param:params){
				JsonNode origNode = mapper.readTree(jsonString);
				
				tempValue = origNode.path(param);
				jsonString = tempValue.toString();
			}	
			if(removeQuotes){
				paramValue = tempValue.toString().replace("\"","");
			}
			else{
				paramValue = tempValue;
			}
			if(paramType.equalsIgnoreCase("long")){
				paramValue = Long.parseLong(paramValue.toString());
			}
			if(paramType.equalsIgnoreCase("double")){
				paramValue = Double.parseDouble(paramValue.toString());
			}
			else{
				return paramValue;
			}
		}catch(Exception e){
			paramValue = null;
		}
		return paramValue;
	}
		
}
