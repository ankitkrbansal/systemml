package dml.sql.sqlcontrolprogram;

import java.util.ArrayList;

import dml.runtime.instructions.Instruction;
import dml.runtime.instructions.SQLInstructions.SQLInstruction;
import dml.sql.sqllops.SQLLops;

public class SQLCreateTable extends SQLCreateBase {
	
	public SQLCreateTable()
	{
		_withs = new ArrayList<SQLWithTable>();
	}
	
	private ArrayList<SQLWithTable> _withs;

	public ArrayList<SQLWithTable> get_withs() {
		return _withs;
	}
	
	private String getCreateTableString()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("CREATE TABLE \"");
		sb.append(this.get_tableName());
		sb.append("\" (");
		sb.append(SQLLops.ROWCOLUMN);
		sb.append(" INT8 NOT NULL, ");
		sb.append(SQLLops.COLUMNCOLUMN);
		sb.append(" INT8 NOT NULL, ");
		sb.append(SQLLops.VALUECOLUMN);
		sb.append(" double precision ) DISTRIBUTE ON (");
		sb.append(SQLLops.ROWCOLUMN);
		//sb.append(", ");
		//sb.append(SQLLops.COLUMNCOLUMN);
		sb.append(");\r\n");
		
		return sb.toString();
	}
	
	private String getInsertIntoString()
	{
		StringBuffer sb = new StringBuffer();

		sb.append("INSERT INTO \"");
		sb.append(this.get_tableName());
		sb.append("\"\r\n(\r\n");
		
		if(this.get_withs().size() > 0)
			sb.append("   WITH ");
		
		for(int i = 0; i < _withs.size(); i++)
		{
			if(i != 0)
				sb.append("   ");
			sb.append(this.get_withs().get(i).generateSQLString());
			if(i < this.get_withs().size() - 1)
				sb.append(",\r\n");
			else
				sb.append("\r\n");
		}
		sb.append("    ");
		sb.append(this.get_selectStatement());
		sb.append("\r\n);\r\n");
		return sb.toString();
	}
	
	private String getGenerateStatisticsString()
	{
		return "GENERATE STATISTICS ON \"" + this.get_tableName() + "\";\r\n";
	}
	
	public String generateSQLString()
	{
		return getCreateTableString() + getInsertIntoString();
	}

	@Override
	public Instruction[] getInstructions() {
		Instruction[] instr = new Instruction[3];
		instr[0] = new SQLInstruction(getCreateTableString());
		instr[1] = new SQLInstruction(getInsertIntoString());
		instr[2] = new SQLInstruction(getGenerateStatisticsString());
		return instr;
	}

	@Override
	public int getStatementCount() {
		return 2;
	}
}
