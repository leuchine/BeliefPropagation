package bp;

class Distribution {
	public double[] one;

	public void setValue(double[] one) {
		this.one = one;
	}
}

public class BeliefPropagation {
	static int length = 7;
	static int distributionlength = 2;
	static double[] node1 = { 0.7, 0.3 };
	static double[] node2 = { 0.1, 0.9 };
	static double[][] edges = { { 1, 0.5 }, { 0.5, 1 } };
	static Distribution[][] distributions = new Distribution[length][length];

	public static void collect(int[][] graph, int parent, int current) {
		for (int i = 1; i < length; i++) {
			if (graph[current][i] == 1 && i != parent) {
				collect(graph, current, i);
			}
		}
		sendMessage(graph, current, parent);
	}

	public static void distribute(int[][] graph, int parent, int current) {
		for (int i = 1; i < length; i++) {
			if (graph[current][i] == 1 && i != parent) {
				sendMessage(graph, current, i);
				distribute(graph, current, i);
			}
		}
	}

	public static void sendMessage(int[][] graph, int j, int i) {
		double[] one = new double[length];
		double[] node = null;
		if (j % 2 == 0) {
			node = node2;
		} else {
			node = node1;
		}
		// i
		for (int k = 0; k < distributionlength; k++) {
			double sum = 0;
			// j

			for (int l = 0; l < distributionlength; l++) {
				double product = node[l] * edges[k][l];
				for (int n = 1; n < length; n++) {
					if (graph[j][n] == 1 && n != i) {
						product *= distributions[n][j].one[l];
					}
				}
				sum += product;
			}
			System.out.println("message "+j+" to "+i+" value "+ k+"="+sum);
			one[k] = sum;
		}
		distributions[j][i].one = one;
	}

	public static void computeMarginal(int[][] graph) {
		for (int i = 1; i < length; i++) {
			System.out.println("margin: " + i);
			double[] node = null;
			if (i % 2 == 0) {
				node = node2;
			} else {
				node = node1;
			}
			for (int j = 0; j < distributionlength; j++) {
				System.out.print(j + ": ");
				double product = node[j];
				for (int k = 1; k < length; k++) {
					if (graph[k][i] == 1) {
						product *= distributions[k][i].one[j];
					}
				}
				System.out.println(product);
			}

		}
	}

	public static void main(String[] args) {
		int[][] graph = new int[length][length];
		graph[1][2] = 1;
		graph[2][1] = 1;
		graph[1][3] = 1;
		graph[3][1] = 1;
		graph[2][4] = 1;
		graph[4][2] = 1;
		graph[2][5] = 1;
		graph[5][2] = 1;
		graph[3][6] = 1;
		graph[6][3] = 1;
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				distributions[i][j] = new Distribution();
			}
		}
		int root = 1;
		collect(graph, 0, 1);
		distribute(graph, 0, 1);
		computeMarginal(graph);
	}
}