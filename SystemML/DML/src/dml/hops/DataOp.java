package dml.hops;

import dml.lops.Data;
import dml.lops.Lops;
import dml.parser.Expression.DataType;
import dml.parser.Expression.ValueType;
import dml.sql.sqllops.SQLLopProperties;
import dml.sql.sqllops.SQLLops;
import dml.sql.sqllops.SQLSelectStatement;
import dml.sql.sqllops.SQLTableReference;
import dml.sql.sqllops.SQLLopProperties.AGGREGATIONTYPE;
import dml.sql.sqllops.SQLLopProperties.JOINTYPE;
import dml.sql.sqllops.SQLLops.GENERATES;
import dml.utils.HopsException;

public class DataOp extends Hops {

	DataOpTypes _dataop;
	String fileName;
	private FileFormatTypes _formatType = FileFormatTypes.TEXT;
	
	
	// READ operation for Matrix w/ dim1, dim2.  
	
	
	public DataOp(String l, DataType dt, ValueType vt, DataOpTypes dop,
			String fname, long dim1, long dim2, int rowsPerBlock, int colsPerBlock) {
		super(Kind.DataOp, l, dt, vt);
		_dataop = dop;
		
		fileName = fname;
		set_dim1(dim1);
		set_dim2(dim2);
		set_rows_per_block(rowsPerBlock);
		set_cols_per_block(colsPerBlock);
				
		if (dop == DataOpTypes.TRANSIENTREAD)
			setFormatType(FileFormatTypes.BINARY);
	}

	// WRITE operation

	public DataOp(String l, DataType dt, ValueType vt, Hops in,
			DataOpTypes dop, String fname) {
		super(Kind.DataOp, l, dt, vt);
		_dataop = dop;
		getInput().add(0, in);
		in.getParent().add(this);
		fileName = fname;

		if (dop == DataOpTypes.TRANSIENTWRITE)
			setFormatType(FileFormatTypes.BINARY);
	}
	
	public void setOutputParams(long dim1, long dim2, int rowsPerBlock, int colsPerBlock) {
		set_dim1(dim1);
		set_dim2(dim2);
		set_rows_per_block(rowsPerBlock);
		set_cols_per_block(colsPerBlock);
	}

	public void setFileName(String fn) {
		fileName = fn;
	}

	public String getFileName() {
		return fileName;
	}

	@Override
	public Lops constructLops()
			throws HopsException {

		if (get_lops() == null) {

			Lops l = null;

			if (_dataop == DataOpTypes.PERSISTENTREAD) {
				l = new Data(
						getFileName(), HopsData2Lops.get(_dataop), null, null,
						get_dataType(), get_valueType(), false);
			} else if (_dataop == DataOpTypes.TRANSIENTREAD) {
				l = new Data(
						getFileName(), HopsData2Lops.get(_dataop), get_name(),
						null, get_dataType(), get_valueType(), true);
			} else if (_dataop == DataOpTypes.PERSISTENTWRITE) {
				l = new Data(
						getFileName(), HopsData2Lops.get(_dataop), this
								.getInput().get(0).constructLops(), null, null,
						get_dataType(), get_valueType(), false);
			} else if (_dataop == DataOpTypes.TRANSIENTWRITE) {
				l = new Data(
						getFileName(), HopsData2Lops.get(_dataop), this
								.getInput().get(0).constructLops(), get_name(),
						null, get_dataType(), get_valueType(), true);
			}
			((Data) l).setFileFormatType(this.getFormatType());

			l.getOutputParameters().setDimensions(get_dim1(), get_dim2(),
					get_rows_per_block(), get_cols_per_block());
			set_lops(l);
		}

		return get_lops();

	}

	public void setFormatType(FileFormatTypes ft) {
		_formatType = ft;
	}

	public FileFormatTypes getFormatType() {
		return _formatType;
	}

	@Override
	public String getOpString() {
		String s = new String("");
		s += HopsData2String.get(_dataop);
		return s;
	}

	public void printMe() throws HopsException {
		if (get_visited() != VISIT_STATUS.DONE) {
			super.printMe();
			System.out.print("  DataOp: " + _dataop);
			if (fileName != null) {
				System.out.print(" file: " + fileName);
			}
			System.out.println(" format: " + getFormatType());
			System.out.print("\n");
			for (Hops h : getInput()) {
				h.printMe();
			}
			;
		}
		set_visited(VISIT_STATUS.DONE);
	}

	public DataOpTypes get_dataop() {
		return _dataop;
	}

	private SQLLopProperties getProperties(String input)
	{
		SQLLopProperties prop = new SQLLopProperties();
		prop.setJoinType(JOINTYPE.NONE);
		prop.setAggType(AGGREGATIONTYPE.NONE);
		
		String op = "PRead ";
		if(this._dataop == DataOpTypes.PERSISTENTWRITE)
			op = "PWrite ";
		else if(this._dataop == DataOpTypes.TRANSIENTREAD)
			op = "TRead ";
		else if(this._dataop == DataOpTypes.TRANSIENTWRITE)
			op = "TWrite ";

		prop.setOpString(op + input);

		return prop;
	}
	
	@Override
	public SQLLops constructSQLLOPs() throws HopsException {
		if(this.get_sqllops() == null)
		{
			SQLLops sqllop;
			
			Hops input = null;
			if(this.getInput().size() > 0)
				input = this.getInput().get(0);
			
			//Should not have any inputs
			if(this._dataop == DataOpTypes.PERSISTENTREAD)
			{
				sqllop = new SQLLops(this.get_name(),
									GENERATES.SQL,
									this.get_valueType(),
									this.get_dataType());
				
				if(this.get_dataType() == DataType.SCALAR)
					sqllop.set_sql(String.format(SQLLops.SELECTSCALAR, SQLLops.addQuotes(this.getFileName())));
				else
				{
					//sqllop.set_flag(GENERATES.DML);
					sqllop.set_tableName(this.getFileName());
					sqllop.set_flag(GENERATES.NONE);
					sqllop.set_sql(String.format(SQLLops.SELECTSTAR, this.getFileName()));
				}
			}
			else if(this._dataop == DataOpTypes.TRANSIENTREAD)
			{
				//Here we do not have a file name, so the name is taken
				sqllop = new SQLLops(this.get_name(),
									GENERATES.NONE,
									this.get_valueType(),
									this.get_dataType());
				
				String name = this.get_name();
				
				if(this.get_dataType() == DataType.MATRIX)
					name += "_transmatrix";
				else 
					name = "##" + name + "_transscalar##";//TODO: put ## here for placeholders
				
				sqllop.set_tableName(name);
				sqllop.set_sql(name);
			}
			else if(this._dataop == DataOpTypes.PERSISTENTWRITE && this.get_dataType() == DataType.SCALAR)
			{
				sqllop = new SQLLops(this.getFileName(),
						GENERATES.DML_PERSISTENT,
						input.constructSQLLOPs(),
						this.get_valueType(),
						this.get_dataType());
				sqllop.set_tableName(this.getFileName());
				//sqllop.set_dataType(DataType.MATRIX);
				
				if(input.get_sqllops().get_flag() == GENERATES.NONE)
					sqllop.set_sql("SELECT " + input.get_sqllops().get_tableName());
				else
					sqllop.set_sql(input.get_sqllops().get_sql());
			}
			else
			{
				if(this.getInput().size() < 1)
					throw new HopsException("A write needs at least one input");

				String name = this.getFileName();
				
				//With scalars or transient writes there is no filename
				if(this.get_dataType() == DataType.SCALAR)
					name = "##" + this.get_name() + "_transscalar##";
				else if(this._dataop == DataOpTypes.TRANSIENTWRITE)
					name = this.get_name() + "_transmatrix";
				
				sqllop = new SQLLops(name,
				(this._dataop == DataOpTypes.TRANSIENTWRITE) ? GENERATES.DML_TRANSIENT : GENERATES.DML_PERSISTENT,
									input.constructSQLLOPs(),
									this.get_valueType(),
									this.get_dataType());
				sqllop.set_tableName(name);
				
				if(input.get_sqllops().get_dataType() == DataType.MATRIX)
					sqllop.set_sql(String.format(SQLLops.SELECTSTAR, input.get_sqllops().get_tableName()));
				//Scalar with SQL
				else if(input.get_sqllops().get_flag() == GENERATES.SQL)
					sqllop.set_sql(String.format(SQLLops.SIMPLESCALARSELECTFROM, "sval", SQLLops.addQuotes(input.get_sqllops().get_tableName() )));
				//Other scalars such as variable names and literals
				else
					sqllop.set_sql(input.get_sqllops().get_tableName());
			}
			String i = this.fileName;
			if(input != null && this.fileName != null)
				i = input.get_sqllops().get_tableName() + "->" + i;
			else
				i = "";
			sqllop.set_properties(getProperties(i));
			this.set_sqllops(sqllop);
		}
		return this.get_sqllops();
	}
	
	private SQLSelectStatement getSQLSelect(Hops input)
	{
		SQLSelectStatement stmt = new SQLSelectStatement();
		
		if(this.get_dataType() == DataType.MATRIX)
		{
			if(this._dataop == DataOpTypes.PERSISTENTREAD)
			{
				stmt.getColumns().add("*");
				stmt.setTable(new SQLTableReference(this.getFileName()));
			}
			else if(this._dataop == DataOpTypes.TRANSIENTREAD)
			{
				return null;
			}
			else if(this._dataop == DataOpTypes.PERSISTENTWRITE || this._dataop == DataOpTypes.TRANSIENTWRITE)
			{
				stmt.getColumns().add("*");
				stmt.setTable(new SQLTableReference(input.get_sqllops().get_tableName()));
			}
		}
		else if(this.get_dataType() == DataType.SCALAR)
		{
			if(this._dataop == DataOpTypes.TRANSIENTREAD)
			{
				return null;
			}
			else if(this._dataop == DataOpTypes.PERSISTENTREAD || this._dataop == DataOpTypes.PERSISTENTWRITE || this._dataop == DataOpTypes.TRANSIENTWRITE)
			{
				stmt.getColumns().add("*");
				stmt.setTable(new SQLTableReference(input.get_sqllops().get_tableName()));
			}
		}
		return stmt;
	}
}
