package shared.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import shared.functional.BinaryEffect;
import shared.functional.Functor;
import shared.functional.Inserter;
import shared.functional.NullaryFunction;

public class CSVReader {
	
	private Inserter<String , String[], List<String[]>> inserter;
	private FileHandler<String[],List<String[]>> fh;
	
	public CSVReader(String path) throws FileNotFoundException {
		NullaryFunction<List<String[]>> instance = 
				() -> new ArrayList<>();
		Functor<String , String[]> mapping = x -> x.split(",");
		BinaryEffect<String[],List<String[]>> insert = (x,y) -> y.add(x); 
		this.inserter = new Inserter<String , String[], List<String[]>>( instance , mapping, insert );
		this.fh = new FileHandler<String[],List<String[]>>(path, inserter);
	} 
	
	public List<String[]> readFile() throws IOException{
		return this.fh.readFile();
	}

}
