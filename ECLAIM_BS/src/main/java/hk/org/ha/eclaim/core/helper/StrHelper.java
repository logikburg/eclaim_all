package hk.org.ha.eclaim.core.helper;

import java.util.ArrayList;
import java.util.List;

public class StrHelper {

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0 ? true : false;		
	}
	
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
	
	public static String format(String inStr) {
		if (inStr == null) {
			return inStr;
		}
		
		return inStr.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

	public static String removeDuplicateEmail(String inEmailList) {
		if (inEmailList == null) {
			return null;
		}
		
		String[] tmpList = inEmailList.split(",");
		List<String> emailList = new ArrayList<String>();
		
		for (int i=0; i<tmpList.length; i++) {
			if (!emailList.contains(tmpList[i].trim())) {
				emailList.add(tmpList[i].trim());
			}
		}
		
		String result = "";
		for (int i=0; i<emailList.size(); i++) {
			if (i != 0) {
				result += ", ";
			}
			
			result += emailList.get(i);
		}
		
		return result;
	}
	
	public static String removeSpecialCharacterFileName(String name) {
		if (isNotEmpty(name)) {
			name = name.replace("./", "").replace("../", "").replace("..\\", "");
		}
		
		return name;
	}
	
	public static String formatDecimal(String inStr) {
		if (inStr == null) {
			return null;
		}
		
		double tmp = Double.parseDouble(inStr);
		return String.valueOf(tmp);
	}
	
	public static String replaceLineBreak(String inStr){
		if (inStr == null) {
			return null;
		}
		
		String tmp = inStr.replaceAll("\r", "");
		tmp = tmp.replaceAll("\n", "<br/>");
		
		return tmp;
	}
}
