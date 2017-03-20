import java.util.function.DoubleUnaryOperator;

/* Set of possible scaling functions for the StrategyEvolution project. */
public enum ScaleFunction
{
	//	@formatter:off
	LINEAR(d ->
	{
		double val = Math.max(d, 0.);
		return val;
	}), 
	
	LADDER(d ->
	{
		double val = Math.max(d, 0.);
		return val - val % 11;
	}), 
	
	SINUSOIDAL(d ->
	{
		double val = Math.max(d, 0.);
		return val < 100 ? (1.2 * val + 
					val * Math.sin(val * 2.* 3. * Math.PI / 100.))/2.2 : val;
	});
	//	@formatter:on

	private DoubleUnaryOperator fct;

	ScaleFunction(DoubleUnaryOperator fct)
	{
		this.fct = fct;
	}

	public DoubleUnaryOperator getFunction()
	{
		return fct;
	};
}
