package particle;

import java.util.List;

public class InsertionSort {

	public static void sortHighToLow(List<Particle> list) {
		for (int i = 1; i < list.size(); i++) {
			Particle item = list.get(i);
			if (item.distance > list.get(i - 1).distance) {
				sortUpHighToLow(list, i);
			}
		}
	}

	private static void sortUpHighToLow(List<Particle> list, int i) {
		Particle item = list.get(i);
		int attemptPos = i - 1;
		while (attemptPos != 0 && list.get(attemptPos - 1).distance < item.distance) {
			attemptPos--;
		}
		list.remove(i);
		list.add(attemptPos, item);
	}

}
