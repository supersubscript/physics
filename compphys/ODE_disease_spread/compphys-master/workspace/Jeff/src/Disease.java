public class Disease {
	private double alpha; // recovery rate
	private double beta; // infection rate
	private double gamma; // vaccination rate

	public Disease(double a, double b, double c) {
		this.alpha = a;
		this.beta = b;
		this.gamma = c;
	}

	public double getAlpha() {
		return alpha;
	}

	public double getBeta() {
		return beta;
	}

	public double getGamma() {
		return gamma;
	}

}
