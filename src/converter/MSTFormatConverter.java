/**
 * 
 */
package converter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Deepak Santhanam
 * deepak@cs.brown.edu
 *
 */
public class MSTFormatConverter {

	static int _times=0;
	public static void main(String[] args) throws IOException {
		
		String _inputFileName="PTB.txt";
		String _outputFileName="DepPTB.txt";
		
		if(args.length==2) {
			_inputFileName=args[0];
			_outputFileName=args[1];
		} else if(args.length !=0) {
			System.err.println("Usage : Java MSTFormatConverter [Input File] [Output File]");
			System.err.println("The Default input file is :"+_inputFileName);
			System.err.println("The Default output file is :"+_outputFileName);
			System.exit(-1);
		}
		File _inputFile = new File(_inputFileName);
		File _outputFile = new File(_outputFileName);
		BufferedReader _inputReader =  new BufferedReader(new FileReader(_inputFile));
		BufferedWriter _outputWriter = new BufferedWriter(new FileWriter(_outputFile));
		String _curString = null;
		while((_curString = _inputReader.readLine())!=null) {
			String[] _spl;
			String _fLine="",_sLine="",_thLine="";
			do{
			_spl = _curString.split("\t");
			_fLine=_fLine+_spl[0]+"\t";
			if(_spl.length==1)
				break;
			_sLine=_sLine+_spl[1]+"\t";
			_thLine=_thLine+_spl[2]+"\t";
			_curString = _inputReader.readLine();
			}while(_spl.length!=1);
			_outputWriter.write(_fLine+"\n"+_sLine+"\n"+_thLine+"\n\n");
			System.out.println("Finished Transforming Tree.. "+ ++_times);
		}
		_outputWriter.close();
	}
}
