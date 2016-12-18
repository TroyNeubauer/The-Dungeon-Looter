package utils;

import com.troy.troyberry.math.Maths;
import com.troy.troyberry.math.Vector2f;

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

	public static int xToIntCoords(float value, int length) {
		value += 1f;
		value /= 2f;
		value *= (float)length;
		return Maths.floor(value);
	}
	
	public static int xToIntWidth(float value, int length) {
		value = Math.abs(value);
		value *= ((float)length / 2f);
		return Maths.floor(value);
	}

	public static Vector2f getMouse() {
		float x  = 0, y = 0;
		return new Vector2f(x, y);
	}

}
