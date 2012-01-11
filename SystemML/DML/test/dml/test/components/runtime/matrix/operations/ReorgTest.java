package dml.test.components.runtime.matrix.operations;

import static org.junit.Assert.*;

import org.junit.Test;

import dml.runtime.instructions.MRInstructions.ReorgInstruction;
import dml.utils.DMLException;

public class ReorgTest {

    @Test
    public void testParseOperation() {
    	fail("ReorgTest.testParseOperation() is not implemented yet.");
    	/*
        try {
            assertEquals(Reorg.SupportedOperation.REORG_TRANSPOSE, Reorg.parseOperation("r'"));
            assertEquals(Reorg.SupportedOperation.REORG_VECTOR_DIAG, Reorg.parseOperation("rdiag"));
        } catch(DMLUnsupportedOperationException e) {
            fail("Operation parsing failed");
        }
        try {
            Reorg.parseOperation("wrong");
            fail("Wrong operation gets parsed");
        } catch(DMLUnsupportedOperationException e) { }
        */
    }

    @Test
    public void testParseInstruction() {
        try {
            ReorgInstruction instType = (ReorgInstruction)ReorgInstruction.parseInstruction("r' 0 1");
            //assertEquals(Reorg.SupportedOperation.REORG_TRANSPOSE, instType.operation);
            assertEquals(0, instType.input);
            assertEquals(1, instType.output);
            
            instType = (ReorgInstruction)ReorgInstruction.parseInstruction("rdiag 0 1");
            //assertEquals(Reorg.SupportedOperation.REORG_VECTOR_DIAG, instType.operation);
            assertEquals(0, instType.input);
            assertEquals(1, instType.output);
        } catch (DMLException e) {
            fail("Instruction parsing failed");
        }
    }

}
