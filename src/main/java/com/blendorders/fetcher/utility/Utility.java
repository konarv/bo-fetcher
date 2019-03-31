package com.blendorders.fetcher.utility;

public class Utility {

	public static String extract(StringBuffer body, String start, String end) {
		try{
			int startIndex = body.indexOf(start); 
			int endIndex = body.indexOf(end);
			
			if(startIndex < 0 || endIndex < 0)
				throw new Exception("data not found");

			String result = body.substring( startIndex + start.length(), endIndex);
			body.delete(0, endIndex);
			return result;
		} catch (Exception e) {
			return "-1";
		}
	}
	
}
