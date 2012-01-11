package dml.packagesupport;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.nimble.algorithms.sequencemining.SequenceMinerTask;
import org.nimble.hadoop.HDFSFileManager;
import org.nimble.io.utils.FixedWidthDataset;
import org.nimble.io.utils.LongComparableSerializable;
import org.nimble.utils.MatrixToSequenceConv;

import dml.packagesupport.Matrix.ValueType;

/**
 * Wrapper class for sequence mining. The class first converts the matrix into
 * sequences. It then invokes the sequence miner on this data set. Finally, it
 * takes the output sequences and returns a matrix.
 * 
 * @author aghoting
 * 
 */
public class SequenceMinerWrapper extends PackageFunction {

	private static final long serialVersionUID = -2650101218663060483L;

	Matrix freq_sequences;
	Matrix seq_support;

	final long entries_in_memory = 1000000;
	final long data_block_size = 10000000;
	final long sequence_block_size = 10000;
	final int numPhysicalFiles = 10;
	final long in_memory_scratch_size = 1000000;
	final long max_projection_size = 10000000;
	final String NIMBLEoutFile = "seq_scratch/SequenceMinerWrapperOutput";
	public String freq_seq_file = "seq_scratch/FreqSeqFile";
	public String seq_support_file = "seq_scratch/FreqSeqSupportFile";
	

	@Override
	public int getNumFunctionOutputs() {

		return 2;

	}

	@Override
	public FIO getFunctionOutput(int pos) {

		if (pos == 0)
			return freq_sequences;
		if (pos == 1)
			return seq_support;

		return null;

	}

	@Override
	public void execute() {

		try {
			// preprocess matrix to convert to sequences
			Matrix m = (Matrix) this.getFunctionInput(0);

			MatrixToSequenceConv dataConv = new MatrixToSequenceConv(
					numPhysicalFiles, entries_in_memory);
			FixedWidthDataset d = new FixedWidthDataset();
			d.setFilePath(m.getFilePath());
			d.setNumFields(1);
			d.setFieldType(0, "java.lang.String");

			dataConv.setNumInputDatasets(1);
			dataConv.addInputDataset(d, 0);
			this.getDAGQueue().pushTask(dataConv);
			dataConv = (MatrixToSequenceConv) this.getDAGQueue().waitOnTask(
					dataConv);
			
			//dataset for sequence miner task
			FixedWidthDataset seqMiningDataset = dataConv.getSequenceDataset();
			
			//support
			double min_sup = Double.parseDouble(((Scalar) this.getFunctionInput(1)).getValue());
			
			//max level
			int max_sequence_size = (int) Double.parseDouble(((Scalar) this.getFunctionInput(2)).getValue());
			
			//invoke sequence mining
			SequenceMinerTask seqMiner = new SequenceMinerTask(min_sup, data_block_size, sequence_block_size, in_memory_scratch_size,  max_projection_size,  max_sequence_size,  NIMBLEoutFile);
			seqMiner.setNumInputDatasets(1);
			seqMiner.addInputDataset(seqMiningDataset, 0);
			this.getDAGQueue().pushTask(seqMiner);
			seqMiner = (SequenceMinerTask) this.getDAGQueue().waitOnTask(seqMiner); 
			
			//write out output matrices
			
			String [] files1 = new HDFSFileManager().getFileNamesWithPrefix(NIMBLEoutFile + "/part");
			String [] files2 = new HDFSFileManager().getFileNamesWithPrefix(NIMBLEoutFile + "/inmemory");
			

			ArrayList <String> files = new ArrayList<String>();
			if(files1 != null)
			{
				for(int i=0; i < files1.length; i++)
					files.add(files1[i]);
			}
			if(files2 != null)
			{
				for(int i=0; i < files2.length; i++)
					files.add(files2[i]);
			}
			
		    
			
			//output sequences
			DataOutputStream ostream1 = HDFSFileManager.getOutputStreamStatic(freq_seq_file, true);
			DataOutputStream ostream2 = HDFSFileManager.getOutputStreamStatic(seq_support_file, true);
			
		
			LongComparableSerializable max_col = new LongComparableSerializable(0);
			String line;
			long seq_id = 1;
			for(String file:files)
			{
				DataInputStream inStrm = new HDFSFileManager().getInputStream(file);
				BufferedReader br = new BufferedReader(new InputStreamReader(inStrm));

				while ((line = br.readLine()) != null) {
            	
					writeOutSequence(seq_id, ostream1, ostream2, line, max_col);
					seq_id++;
            	
				}
				br.close();
				inStrm.close();
				
			}
						
			ostream1.close();
			ostream2.close();
			
			freq_sequences = new Matrix(freq_seq_file, seq_id-1 , max_col.getLong(), ValueType.Double);
			seq_support = new Matrix(seq_support_file, seq_id-1, 2, ValueType.Double);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new PackageRuntimeException("Error execution sequence miner");
		}

	}

	//method to write out sequences as matrices
	//output 1 stores seqid, txnid, item
	//output 2 stores seqid, support
	private void writeOutSequence(long seq_id, DataOutputStream ostream1,
			DataOutputStream ostream2, String line, LongComparableSerializable max_col) throws IOException {
		StringTokenizer tk = new StringTokenizer(line);
		
		//empty line
		if(!tk.hasMoreTokens())
			return;
		
		ArrayList <Long> sequence = new ArrayList<Long>();
		while(tk.hasMoreTokens())
		{
			sequence.add(Long.parseLong(tk.nextToken()));
		}
		

		
		for(int i=0; i < sequence.size(); i++)
		{

			if(sequence.get(i) == -2)
			{
				long count = sequence.get(i+1);
				double d_count = count;
				ostream2.writeBytes(seq_id + " " + "1" + " " + seq_id + "\n" );
				ostream2.writeBytes(seq_id + " " + "2" + " " + d_count + "\n");
				break;
			}
				
			
			ostream1.writeBytes(seq_id + " " + (i+1) + " " + sequence.get(i) + "\n");
			
		}
		
		
		
		if (max_col.getLong() < sequence.size())
		{
			max_col.setLong(sequence.size());
			
		}
	}

}
