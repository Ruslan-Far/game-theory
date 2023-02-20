package second;

import common.CommonFunctions;

import java.util.Arrays;

public class Main {

	public static final int INF_MAX = 999999999;
	public static final int INF_MIN = -999999999;

	private static int strategyA;
	private static int strategyB;

//	private static final int[][] INITIAL_MATRIX = {
//			{4, 2, 2},
//			{2, 5, 0},
//			{0, 2, 5}
//	};

	private static final int[][] INITIAL_MATRIX = {
			{3, 5, 1, 1},
			{4, 2, 3, 3},
			{2, 1, 5, 4}
	};

	public static int[][] matrix;

	public static boolean isMin = false;

	public static void main(String[] args) {
		int alpha;
		int beta;
		double[] answerA;
		double[] answerB;
		double f;
		double g;
		double V;
		double[] p;
		double[] q;

		alpha = getLowerGamePrice();
		System.out.println("Нижняя цена игры: " + alpha + "\n");
		beta = getTopGamePrice();
		System.out.println("Верхняя цена игры: " + beta + "\n");
		if (alpha == beta) {
			System.out.println("Игра имеет седловую точку (A" + strategyA + ", B" + strategyB + ")");
			return;
		}
		prepareMatrixForDirect();
		System.out.println("Подготовленная матрица для прямой задачи\n");
		CommonFunctions.printMatrix(matrix);
		Simplex.method();
		answerB = Arrays.stream(Simplex.answer).toArray();
		prepareMatrixForDual();
		System.out.println("\nПодготовленная матрица для двойственной задачи\n");
		CommonFunctions.printMatrix(matrix);
		Simplex.method();
		answerA = Arrays.stream(Simplex.answer).toArray();
		f = Arrays.stream(answerB).sum();
		g = Arrays.stream(answerA).sum();
		System.out.printf("\nЛинейная форма оптимальных планов как сумма найденных координат для 1 игрока: %4.3f", g);
		System.out.printf("\nЛинейная форма оптимальных планов как сумма найденных координат для 2 игрока: %4.3f", f);
		V = 1 / f;
		System.out.printf("\nЦена игры: %4.3f\n\n", V);
		p = Arrays.stream(answerA)
				.map(x -> V * x)
				.toArray();
		q = Arrays.stream(answerB)
				.map(x -> V * x)
				.toArray();
		System.out.println("Оптимальная смешанная стратегия 1 игрока:");
		CommonFunctions.printArray(p);
		System.out.println("Оптимальная смешанная стратегия 2 игрока:");
		CommonFunctions.printArray(q);
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

	private static void prepareMatrixForDirect() {
		matrix = new int[INITIAL_MATRIX.length + 1][INITIAL_MATRIX[0].length + 1];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (i == matrix.length - 1 || j == matrix[i].length - 1) {
					matrix[i][j] = 1;
					continue;
				}
				matrix[i][j] = INITIAL_MATRIX[i][j];
			}
		}
	}

	private static void prepareMatrixForDual() {
		int[][] localMatrix;

		localMatrix = new int[matrix[0].length][matrix.length];
		for (int i = 0; i < localMatrix.length; i++) {
			for (int j = 0; j < localMatrix[i].length; j++) {
				if (i == localMatrix.length - 1) {
					localMatrix[i][j] = matrix[j][i];
					continue;
				}
				localMatrix[i][j] = -matrix[j][i];
			}
		}
		Simplex.answer = new double[localMatrix[0].length - 1];
		matrix = localMatrix;
		isMin = true;
	}
}
