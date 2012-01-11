package dml.runtime.instructions.CPInstructions;


public class BooleanObject extends ScalarObject  {

	private boolean _value;

	public BooleanObject(boolean val){
		this(null,val);
	}

	public BooleanObject(String name,boolean val){
		super(name);
		_value = val;
	}

	public int getIntValue(){
		throw new UnsupportedOperationException();
	}

	public double getDoubleValue(){
		throw new UnsupportedOperationException();
	}

	public boolean getBooleanValue(){
		return _value;
	}

	public Object getValue(){
		return _value;
	}

	public String getStringValue(){
		return Boolean.toString(_value);
	}

	@Override
	public long getLongValue() {
		throw new UnsupportedOperationException();
	}
	
}
