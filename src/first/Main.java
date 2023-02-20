package first;

import common.CommonFunctions;

public class Main {

	private static final int INF_MAX = 999999999;
	private static final int INF_MIN = -999999999;

	private static final int[][] INITIAL_MATRIX = {
			{3, 6, 1, 4},
			{5, 3, 6, 2},
			{1, 4, 3, 5}
	};

//	private static final int[][] INITIAL_MATRIX = {
//			{2, 5, 4, 12, 6},
//			{6, 11, 9, 8, 7},
//			{3, 3, 2, 0, 1}
//	};

	private static int strategyA;
	private static int strategyB;

	public static void main(String[] args) {
		int alpha;
		int beta;

		alpha = getLowerGamePrice();
		System.out.println("Нижняя цена игры: " + alpha + "\n");
		beta = getTopGamePrice();
		System.out.println("Верхняя цена игры: " + beta + "\n");
		if (alpha == beta) {
			System.out.println("Игра имеет седловую точку (A" + strategyA + ", B" + strategyB + ")");
		}
	}

	private static int getLowerGamePrice() {
		int min;
		int max;
		int[] array_min;

		array_min = new int[INITIAL_MATRIX.length];
		for (int i = 0; i < INITIAL_MATRIX.length; i++) {
			min = INF_MAX;
			for (int j = 0; j < INITIAL_MATRIX[i].length; j++) {
				if (INITIAL_MATRIX[i][j] < min) {
					array_min[i] = INITIAL_MATRIX[i][j];
					min = INITIAL_MATRIX[i][j];
				}
			}
		}
		System.out.println("Массив минимумов по строкам");
		CommonFunctions.printArray(array_min);
		max = INF_MIN;
		for (int i = 0; i < array_min.length; i++) {
			if (array_min[i] > max) {
				strategyA = i + 1;
				max = array_min[i];
			}
		}
		return max;
	}

	private static int getTopGamePrice() {
		int min;
		int max;
		int[] array_max;

		array_max = new int[INITIAL_MATRIX[0].length];
		for (int j = 0; j < INITIAL_MATRIX[0].length; j++) {
			max = INF_MIN;
			for (int i = 0; i < INITIAL_MATRIX.length; i++) {
				if (INITIAL_MATRIX[i][j] > max) {
					array_max[j] = INITIAL_MATRIX[i][j];
					max = INITIAL_MATRIX[i][j];
				}
			}
		}
		System.out.println("Массив максимумов по столбцам");
		CommonFunctions.printArray(array_max);
		min = INF_MAX;
		for (int i = 0; i < array_max.length; i++) {
			if (array_max[i] < min) {
				strategyB = i + 1;
				min = array_max[i];
			}
		}
		return min;
	}
}
