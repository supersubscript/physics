import java.util.function.DoubleUnaryOperator;

public class Exercise
{

	public static Exercise exc_2_2()
	{
		return new Exercise(s ->
		{
			return s * s - 1;
		} , s ->
		{
			return 2 * s;
		});
	}

	public static Exercise exc_2_3()
	{
		return new Exercise(s ->
		{
			return s * s + 1;
		} , s ->
		{
			return 2 * s;
		});
	}

	public static Exercise exc_2_4()
	{
		return new Exercise(s ->
		{
			return s * s * s - s + Math.sqrt(2.) / 2.;
		} , s ->
		{
			return 3 * s * s - 1;
		});
	}

	public static Exercise exc_2_5(double r)
	{
		return new Exercise(r, s ->
		{
			return r * s * (1. - s);
		} , s ->
		{
			return r - 2 * r * s;
		});
	}

	private DoubleUnaryOperator	fct;
	private DoubleUnaryOperator	derivative;
	private double						controlParameter;

	private Exercise(DoubleUnaryOperator fct, DoubleUnaryOperator derivative)
	{
		this.setFct(fct);
		this.setDerivative(derivative);
	}

	private Exercise(double cp, DoubleUnaryOperator fct,
			DoubleUnaryOperator derivative)
	{
		this.setControlParameter(cp);
		this.setFct(fct);
		this.setDerivative(derivative);
	}

	public DoubleUnaryOperator getFct()
	{
		return fct;
	}

	public void setFct(DoubleUnaryOperator fct)
	{
		this.fct = fct;
	}

	public DoubleUnaryOperator getDerivative()
	{
		return derivative;
	}

	public void setDerivative(DoubleUnaryOperator derivative)
	{
		this.derivative = derivative;
	}

	public double getControlParameter()
	{
		return controlParameter;
	}

	public void setControlParameter(double controlParameter)
	{
		this.controlParameter = controlParameter;
	}

}
