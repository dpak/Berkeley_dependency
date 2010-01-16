/**
 * 
 */
package converter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

import ling.PennTreeReader;
import ling.Tree;

/**
 * 
 * @author Deepak Santhanam
 * deepak@cs.brown.edu
 *
 */
public class ConvertPTB {
	
	public static void main(String args[]) throws IOException, FileNotFoundException {
	
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
		
		File ptf=new File(_inputFileName);
    	Reader in=new  FileReader(ptf);
    	File opfile = new File(_outputFileName);
    	Writer output = new BufferedWriter(new FileWriter(opfile,true));
    	PennTreeReader btreader = new PennTreeReader(in);
    	int cnt=0;
    	while(btreader.hasNext()) {
    		Tree<String> _curTree = btreader.next();
    		int _size=_curTree.getPreTerminalYield().size();
    		List<String>_preTermYield = _curTree.getPreTerminalYield();
    		List<String> _Yield = _curTree.getYield();
    		System.out.println(cnt++);
    		for(int _wsize=0; _wsize < _size; _wsize++ ) {
    			String _cString = Integer.toString(_wsize+1) + "\t";
    			_cString = _cString + _Yield.get(_wsize) + "\t" + "_" + "\t" + _preTermYield.get(_wsize) + "\t";
    			_cString = _cString + _preTermYield.get(_wsize) + "\t" + "_"+"\n";
    		    output.write(_cString);	
    		}
    		output.write(System.getProperty("line.separator"));
    	}
    	output.close();
    }
}
