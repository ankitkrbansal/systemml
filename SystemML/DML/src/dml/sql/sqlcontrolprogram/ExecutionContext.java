package dml.sql.sqlcontrolprogram;

import java.util.ArrayList;
import java.util.HashMap;

import dml.parser.Expression.ValueType;
import dml.runtime.instructions.CPInstructions.BooleanObject;
import dml.runtime.instructions.CPInstructions.Data;
import dml.runtime.instructions.CPInstructions.DoubleObject;
import dml.runtime.instructions.CPInstructions.IntObject;
import dml.runtime.instructions.CPInstructions.ScalarObject;
import dml.runtime.instructions.CPInstructions.StringObject;
import dml.utils.DMLRuntimeException;

public class ExecutionContext {
	
	public ExecutionContext(NetezzaConnector nzCon)
	{
		nzConnector = nzCon;
		statistics = new ArrayList<SQLExecutionStatistics>();
	}
	
	protected HashMap<String, Data> _variables;
	private NetezzaConnector nzConnector;
	private boolean debug;
	ArrayList<SQLExecutionStatistics> statistics;

	public void addStatistic(int instructionId, long runtime, String opString)
	{
		SQLExecutionStatistics s = new SQLExecutionStatistics(opString, instructionId, runtime);
		statistics.add(s);
	}
	
	public void clearStatistics()
	{
		statistics.clear();
	}
	
	public HashMap<Integer, SQLExecutionStatistics> getStatisticsByInstruction() {
		HashMap<Integer, SQLExecutionStatistics> stats = new HashMap<Integer, SQLExecutionStatistics>();
		
		for(SQLExecutionStatistics s : statistics)
		{
			if(stats.containsKey(s.getInstructionId()))
			{
				SQLExecutionStatistics st = stats.get(s.getInstructionId());
				st.setRuntime(st.getRuntime() + s.getRuntime());
				st.setTimesRun(st.getTimesRun() + 1);
			}
			else
				stats.put(s.getInstructionId(), s);
		}
		
		return stats;
	}
	
	public ArrayList<SQLExecutionStatistics> getStatistics() {
		return statistics;
	}

	public boolean isDebug() {
		return debug;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	public HashMap<String, Data> get_variables() {
		return _variables;
	}
	public void set_variables(HashMap<String, Data> variables) {
		_variables = variables;
	}
	public NetezzaConnector getNzConnector() {
		return nzConnector;
	}
	
	public void setVariable(String name, Data val) {
		_variables.put(name, val);
	}
	
	public String getVariableString(String name, boolean forSQL)
	{
		Data obj = _variables.get(name);
		if(obj != null)
		{
			String s = ((ScalarObject)obj).getStringValue();
			if(obj instanceof StringObject)
				s = "'" + s + "'";
			else if (obj instanceof DoubleObject && forSQL)
				s = s + "::double precision";
 			return s;
		}
		else return name;
	}
	
	public Data getVariable(String name, ValueType vt) {
		Data obj = _variables.get(name);
		if (obj == null) {
			try {
				switch (vt) {
				case INT:
					int intVal = Integer.parseInt(name);
					IntObject intObj = new IntObject(intVal);
					return intObj;
				case DOUBLE:
					double doubleVal = Double.parseDouble(name);
					DoubleObject doubleObj = new DoubleObject(doubleVal);
					return doubleObj;
				case BOOLEAN:
					Boolean boolVal = Boolean.parseBoolean(name);
					BooleanObject boolObj = new BooleanObject(boolVal);
					return boolObj;
				case STRING:
					StringObject stringObj = new StringObject(name);
					return stringObj;
				default:
					throw new DMLRuntimeException("Unknown variable: " + name + ", or unknown value type: " + vt);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return obj;
	}
}
