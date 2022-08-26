package it.unibs.pajc.nieels.hive;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

public class Base64SerializationUtility {
	
	/**
	 * Serializes a serializable object and converts its serialization in a base64 String.
	 * @param obj the object to be serialized, Serializable.
	 * @return a base64 String containing the serialized object (null if the serialization couldn't complete), String.
	 *///https://www.baeldung.com/java-serial-version-uid
	public static String serializeObjectToString(Serializable obj) {
		String serialized = null;
		
		try(
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		){
		    objectOutputStream.writeObject(obj);
			objectOutputStream.flush();
	        
	        serialized = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
	      
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return serialized;
    }
	
	public static Object deserializeObjectFromString(String serialized){
		Object object = null; 
		byte[] data = Base64.getDecoder().decode(serialized);
		        
		try(ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data))){
			object = objectInputStream.readObject();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return object;
	}

}
