package dml.test.integration.functions.binary.matrix;

import org.junit.Test;

import dml.test.integration.AutomatedTestBase;
import dml.test.integration.TestConfiguration;


public class ScalarMultiplicationTest extends AutomatedTestBase {

	@Override
	public void setUp() {
		baseDirectory = SCRIPT_DIR + "functions/binary/matrix/";
		
		// positive tests
		availableTestConfigurations.put("IntConstTest", new TestConfiguration("ScalarMultiplicationTest",
				new String[] { "vector_left", "vector_right", "matrix_left", "matrix_right" }));
		availableTestConfigurations.put("IntVarTest", new TestConfiguration("ScalarMultiplicationTest",
				new String[] { "vector_left", "vector_right", "matrix_left", "matrix_right" }));
		availableTestConfigurations.put("DoubleConstTest", new TestConfiguration("ScalarMultiplicationTest",
				new String[] { "vector_left", "vector_right", "matrix_left", "matrix_right" }));
		availableTestConfigurations.put("DoubleVarTest", new TestConfiguration("ScalarMultiplicationTest",
				new String[] { "vector_left", "vector_right", "matrix_left", "matrix_right" }));
		availableTestConfigurations.put("SparseTest", new TestConfiguration("ScalarMultiplicationTest",
				new String[] { "vector_left", "vector_right", "matrix_left", "matrix_right" }));
		availableTestConfigurations.put("EmptyTest", new TestConfiguration("ScalarMultiplicationTest",
				new String[] { "vector_left", "vector_right", "matrix_left", "matrix_right" }));
		
		// negative tests
	}
	
	@Test
	public void testIntConst() {
		int rows = 10;
		int cols = 10;
		int factor = 2;
		
		TestConfiguration config = availableTestConfigurations.get("IntConstTest");
		config.addVariable("rows", rows);
		config.addVariable("cols", cols);
		config.addVariable("vardeclaration", "");
		config.addVariable("factor", factor);
		
		loadTestConfiguration("IntConstTest");
		
		double[][] vector = getRandomMatrix(rows, 1, 0, 1, 1, -1);
		double[][] computedVector = new double[rows][1];
		for(int i = 0; i < rows; i++) {
			computedVector[i][0] = vector[i][0] * factor;
		}
		writeInputMatrix("vector", vector);
		writeExpectedMatrix("vector_left", computedVector);
		writeExpectedMatrix("vector_right", computedVector);
		
		double[][] matrix = getRandomMatrix(rows, cols, 0, 1, 1, -1);
		double[][] computedMatrix = new double[rows][cols];
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				computedMatrix[i][j] = matrix[i][j] * factor;
			}
		}
		writeInputMatrix("matrix", matrix);
		writeExpectedMatrix("matrix_left", computedMatrix);
		writeExpectedMatrix("matrix_right", computedMatrix);
		
		runTest();
		
		compareResults();
	}
	
	@Test
	public void testIntVar() {
		int rows = 10;
		int cols = 10;
		int factor = 2;
		
		TestConfiguration config = availableTestConfigurations.get("IntVarTest");
		config.addVariable("rows", rows);
		config.addVariable("cols", cols);
		config.addVariable("vardeclaration", "Factor = " + factor);
		config.addVariable("factor", "Factor");
		
		loadTestConfiguration("IntVarTest");
		
		double[][] vector = getRandomMatrix(rows, 1, 0, 1, 1, -1);
		double[][] computedVector = new double[rows][1];
		for(int i = 0; i < rows; i++) {
			computedVector[i][0] = vector[i][0] * factor;
		}
		writeInputMatrix("vector", vector);
		writeExpectedMatrix("vector_left", computedVector);
		writeExpectedMatrix("vector_right", computedVector);
		
		double[][] matrix = getRandomMatrix(rows, cols, 0, 1, 1, -1);
		double[][] computedMatrix = new double[rows][cols];
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				computedMatrix[i][j] = matrix[i][j] * factor;
			}
		}
		writeInputMatrix("matrix", matrix);
		writeExpectedMatrix("matrix_left", computedMatrix);
		writeExpectedMatrix("matrix_right", computedMatrix);
		
		runTest();
		
		compareResults();
	}
	
	@Test
	public void testDoubleConst() {
		int rows = 10;
		int cols = 10;
		double factor = 2;
		
		TestConfiguration config = availableTestConfigurations.get("DoubleConstTest");
		config.addVariable("rows", rows);
		config.addVariable("cols", cols);
		config.addVariable("vardeclaration", "");
		config.addVariable("factor", factor);
		
		loadTestConfiguration("DoubleConstTest");
		
		double[][] vector = getRandomMatrix(rows, 1, 0, 1, 1, -1);
		double[][] computedVector = new double[rows][1];
		for(int i = 0; i < rows; i++) {
			computedVector[i][0] = vector[i][0] * factor;
		}
		writeInputMatrix("vector", vector);
		writeExpectedMatrix("vector_left", computedVector);
		writeExpectedMatrix("vector_right", computedVector);
		
		double[][] matrix = getRandomMatrix(rows, cols, 0, 1, 1, -1);
		double[][] computedMatrix = new double[rows][cols];
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				computedMatrix[i][j] = matrix[i][j] * factor;
			}
		}
		writeInputMatrix("matrix", matrix);
		writeExpectedMatrix("matrix_left", computedMatrix);
		writeExpectedMatrix("matrix_right", computedMatrix);
		
		runTest();
		
		compareResults();
	}
	
	@Test
	public void testDoubleVar() {
		int rows = 10;
		int cols = 10;
		double factor = 2;
		
		TestConfiguration config = availableTestConfigurations.get("DoubleVarTest");
		config.addVariable("rows", rows);
		config.addVariable("cols", cols);
		config.addVariable("vardeclaration", "Factor = " + factor);
		config.addVariable("factor", "Factor");
		
		loadTestConfiguration("DoubleVarTest");
		
		double[][] vector = getRandomMatrix(rows, 1, 0, 1, 1, -1);
		double[][] computedVector = new double[rows][1];
		for(int i = 0; i < rows; i++) {
			computedVector[i][0] = vector[i][0] * factor;
		}
		writeInputMatrix("vector", vector);
		writeExpectedMatrix("vector_left", computedVector);
		writeExpectedMatrix("vector_right", computedVector);
		
		double[][] matrix = getRandomMatrix(rows, cols, 0, 1, 1, -1);
		double[][] computedMatrix = new double[rows][cols];
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				computedMatrix[i][j] = matrix[i][j] * factor;
			}
		}
		writeInputMatrix("matrix", matrix);
		writeExpectedMatrix("matrix_left", computedMatrix);
		writeExpectedMatrix("matrix_right", computedMatrix);
		
		runTest();
		
		compareResults();
	}
	
	@Test
	public void testSparse() {
		int rows = 100;
		int cols = 50;
		int factor = 2;
		
		TestConfiguration config = availableTestConfigurations.get("SparseTest");
		config.addVariable("rows", rows);
		config.addVariable("cols", cols);
		config.addVariable("vardeclaration", "");
		config.addVariable("factor", factor);
		
		loadTestConfiguration("SparseTest");
		
		double[][] vector = getRandomMatrix(rows, 1, -1, 1, 0.05, -1);
		double[][] computedVector = new double[rows][1];
		for(int i = 0; i < rows; i++) {
			computedVector[i][0] = vector[i][0] * factor;
		}
		writeInputMatrix("vector", vector);
		writeExpectedMatrix("vector_left", computedVector);
		writeExpectedMatrix("vector_right", computedVector);
		
		double[][] matrix = getRandomMatrix(rows, cols, -1, 1, 0.05, -1);
		double[][] computedMatrix = new double[rows][cols];
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				computedMatrix[i][j] = matrix[i][j] * factor;
			}
		}
		writeInputMatrix("matrix", matrix);
		writeExpectedMatrix("matrix_left", computedMatrix);
		writeExpectedMatrix("matrix_right", computedMatrix);
		
		runTest();
		
		compareResults();
	}
	
}
