package auth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class FileHandler {
	
	private FileReader reader;
	private BufferedReader buffer; 
	
	public FileHandler(String path) throws FileNotFoundException {
		try {
			this.reader = new FileReader(new File(path));
			this.buffer = new BufferedReader(this.reader); 
		} catch (FileNotFoundException e){
			throw e;
		}
	}
	
	public ConcurrentHashMap<String, String> readUsers() throws IOException{
		ConcurrentHashMap<String, String> users = new ConcurrentHashMap<String, String>();
		BufferedReader buffer = new BufferedReader(this.reader);
		String line = null;
		while( (line = buffer.readLine()) != null ) {
			String[] tokens = line.split(",");
			users.put(tokens[0], tokens[1]);
		}
		return users;
	}
	
	public void close() throws IOException {
		this.buffer.close();
		this.reader.close();
	}
}
