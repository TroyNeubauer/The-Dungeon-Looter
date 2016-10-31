package utils;

public class MiscUtil {

	public static long getlong(String s) {
		long number = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			number += ((int) c) * ((long) Math.pow(10, i));
		}
		return number;
	}

	private MiscUtil() {

	}

}
