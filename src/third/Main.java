package third;

import common.CommonFunctions;

public class Main {

	private static final int INF_MAX = 999999999;
	private static final int INF_MIN = -999999999;

	private static final int[][] INITIAL_MATRIX = {
			{6, 1, 4},
			{2, 4, 2},
			{4, 3, 5}
	};

//	private static final int[][] INITIAL_MATRIX = {
//			{3, 5, 1, 1},
//			{4, 2, 3, 3},
//			{2, 1, 5, 4}
//	};

	private static int strategyA;
	private static int strategyB;

	public static void main(String[] args) {
		int alpha;
		int beta;
		int k;
		int limitMax;
		double[][] matrix;

		alpha = getLowerGamePrice();
		System.out.println("Нижняя цена игры: " + alpha + "\n");
		beta = getTopGamePrice();
		System.out.println("Верхняя цена игры: " + beta + "\n");
		if (alpha == beta) {
			System.out.println("Игра имеет седловую точку (A" + strategyA + ", B" + strategyB + ")");
			return;
		}
		k = 0;
		limitMax = 30;
		matrix = new double[limitMax][INITIAL_MATRIX.length + INITIAL_MATRIX[0].length + 6];
		while (k < limitMax) {
			int i;
			int j;
			double vMin;
			double vMax;

			matrix[k][0] = k + 1;
			if (k == 0) {
				i = 0;
				j = 1;
			}
			else {
				i = selectAstrategy(matrix, k);
				//matrix[k][1] = i + 1;///////////////////////////////////
				j = selectBstrategy(matrix, k);
			}
			matrix[k][1] = i + 1;
			matrix[k][2 + INITIAL_MATRIX[i].length] = j + 1;
			if (k == 0) {
				for (int jj = 0; jj < INITIAL_MATRIX[i].length; jj++) {
					matrix[k][jj + 2] = INITIAL_MATRIX[i][jj];
				}
				for (int ii = 0; ii < INITIAL_MATRIX.length; ii++) {
					matrix[k][ii + 3 + INITIAL_MATRIX[i].length] = INITIAL_MATRIX[ii][j];
				}
			}
			else {
				for (int jj = 0; jj < INITIAL_MATRIX[i].length; jj++) {
					matrix[k][jj + 2] = matrix[k - 1][jj + 2] + INITIAL_MATRIX[i][jj];
				}
				for (int ii = 0; ii < INITIAL_MATRIX.length; ii++) {
					matrix[k][ii + 3 + INITIAL_MATRIX[i].length] = matrix[k - 1][ii + 6] + INITIAL_MATRIX[ii][j];
				}
			}
			vMin = getVmin(matrix, k);
			vMax = getVmax(matrix, k);
			matrix[k][INITIAL_MATRIX.length + INITIAL_MATRIX[i].length + 3] = vMin;
			matrix[k][INITIAL_MATRIX.length + INITIAL_MATRIX[i].length + 4] = vMax;
			matrix[k][matrix[i].length - 1] = (vMin + vMax) / 2;
			k++;
		}
		CommonFunctions.printMatrix(matrix);
		getAnswer(matrix);
	}

	private static int selectAstrategy(double[][] matrix, int k) {
		int j;
		int max;
		int strategyA;

		j = (int) matrix[k - 1][2 + INITIAL_MATRIX[0].length] - 1;
		max = INF_MIN;
		strategyA = -1;
		for (int i = 0; i < INITIAL_MATRIX.length; i++) {
			if (INITIAL_MATRIX[i][j] > max) {
				strategyA = i;
				max = INITIAL_MATRIX[i][j];
			}
		}
		return strategyA;
	}

	private static int selectBstrategy(double[][] matrix, int k) {
		int i;
		int min;
		int strategyB;

		//i = (int) matrix[k][1] - 1;//////////////////////////k-1
		i = (int) matrix[k - 1][1] - 1;//////////////////////////
		min = INF_MAX;
		strategyB = -1;
		for (int j = 0; j < INITIAL_MATRIX[i].length; j++) {
			if (INITIAL_MATRIX[i][j] < min) {
				strategyB = j;
				min = INITIAL_MATRIX[i][j];
			}
		}
		return strategyB;
	}

	private static double getVmin(double[][] matrix, int k) {
		double min;

		min = INF_MAX;
		for (int j = 0; j < INITIAL_MATRIX[0].length; j++) {
			double num;

			num = matrix[k][j + 2] / (k + 1);
			if (num < min)
				min = num;
		}
		return min;
	}

	private static double getVmax(double[][] matrix, int k) {
		double max;

		max = INF_MIN;
		for (int j = 0; j < INITIAL_MATRIX.length; j++) {
			double num;

			num = matrix[k][j + 3 + INITIAL_MATRIX[0].length] / (k + 1);
			if (num > max)
				max = num;
		}
		return max;
	}

	private static void getAnswer(double[][] matrix) {
		int k;
		double min;

		k = -1;
		min = INF_MAX;
		for (int i = 0; i < matrix.length; i++) {
			double e;

			e = matrix[i][matrix[0].length - 2] - matrix[i][matrix[0].length - 3];
			if (e < min) {
				k = i + 1;
				min = e;
			}
		}
		System.out.println("\n" + k);
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
