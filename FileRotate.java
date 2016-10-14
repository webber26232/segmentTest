import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileRotate {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File[] files = new File("D:/Courses/5 2015 autumn/MKT525/Project/autoTest").listFiles();
		for(int i=0;i<files.length;i++){
			BufferedReader br = new BufferedReader(new FileReader(files[i]));
			ArrayList<String[]> al = new ArrayList<String[]>();
			String line;
			while((line=br.readLine())!=null){
				al.add(line.replaceAll(", ",". ").split(","));
			}
			br.close();
			BufferedWriter bw = new BufferedWriter(new FileWriter("D:/Courses/5 2015 autumn/MKT525/Project/rotatedFile/r"+files[i].getName()));
			for(int row=0;row<al.size()-1;row++){
				bw.write(al.get(row)[0]+",");
			}
			bw.write(al.get(al.size()-1)[0]+"\r\n");
			for(int column=5;column<al.get(0).length;column++){
				for(int row=0;row<al.size()-1;row++){
					bw.write(al.get(row)[column]+",");
				}
				bw.write(al.get(al.size()-1)[column]+"\r\n");
			}
			bw.close();
		}
	}
}
