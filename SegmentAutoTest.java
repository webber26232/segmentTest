import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class SegmentAutoTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String dir525 = "D:/Courses/5 2015 autumn/MKT525/Project/textResult";
		String dirMyFoodDeliver = "D:/新建文件夹/Startup/Data";
		BufferedReader br = new BufferedReader(new FileReader(dirMyFoodDeliver+"/New_Code.csv"));
		ArrayList<String[]> al = new ArrayList<String[]>();
		String line;
		while((line=br.readLine())!=null){
			al.add(line.split(","));
		}
		br.close();
		int firstQuestion = 5;
		int columnNo = al.get(0).length-1;
		int firstRow = 2;
		HashMap<String,int[]>[] levels = new HashMap[columnNo];//Be used to count numbers of every level in every question
		for(int column=firstQuestion;column<columnNo;column++){
			levels[column] = new HashMap<String,int[]>();
			HashMap<String,int[]> hm = levels[column];
			for(int row=firstRow;row<al.size();row++){
				String level = al.get(row)[column];
				if(!level.equals("")&&level!=null){	
					if(!hm.containsKey(level)){
						hm.put(level, new int[columnNo]);
					}
					int[] ovlpRtNumbers=hm.get(level);
					for(int atbtColumn=firstQuestion;atbtColumn<columnNo;atbtColumn++){
						if(al.get(row)[atbtColumn]!=null&&!al.get(row)[atbtColumn].equals("")){
							ovlpRtNumbers[atbtColumn]++;
						}
					}
				}
			}
		}
		for(int column=firstQuestion;column<columnNo;column++){
			TreeMap<String,int[]> segment = new TreeMap<String,int[]>();//be used to store the rating sum for other attribute of segments
			BufferedWriter bw = new BufferedWriter(new FileWriter("D:/Courses/5 2015 autumn/MKT525/Project/autoTest/"+al.get(0)[column]+".csv"));
			for(int i=0;i<firstQuestion;i++){
				bw.write(",");
			}
			for(int i=firstQuestion;i<al.get(0).length-1;i++){
				bw.write(al.get(0)[i]+",");
			}
			bw.write(al.get(0)[al.get(0).length-1]+"\r\n");
			for(int i=0;i<firstQuestion;i++){
				bw.write(",");
			}
			for(int i=firstQuestion;i<al.get(1).length-1;i++){
				bw.write(al.get(1)[i]+",");
			}
			bw.write(al.get(1)[al.get(1).length-1]+"\r\n");
			bw.flush();
			for(int row=firstRow;row<al.size();row++){
				String level = al.get(row)[column];
				if(!level.equals("")&&level!=null){
					int[] levelSums;
					if(segment.containsKey(level)){
						levelSums = segment.get(level);
					}else{
						levelSums = new int[columnNo];
						segment.put(level, levelSums);
					}
					for(int segmentColumn=firstQuestion;segmentColumn<columnNo;segmentColumn++){
						String otherLevel = al.get(row)[segmentColumn];
						levelSums[segmentColumn] += (!otherLevel.equals("")&&otherLevel!=null? Integer.parseInt(otherLevel):0);						
					}
				}
			}
			double[] means = new double[columnNo];
			double[] standardDeviation = new double[columnNo];
			for(int segmentColumn=firstQuestion;segmentColumn<columnNo;segmentColumn++){
				int sum=0;
				int count=0;
				for(String level:segment.keySet()){
					sum += segment.get(level)[segmentColumn];
					count += levels[column].get(level)[segmentColumn];
				}
				means[segmentColumn] = (double)sum/count;
			}
			for(int segmentColumn=firstQuestion;segmentColumn<columnNo;segmentColumn++){//calculate 
				int count=0;
				double sumE=0;
				for(int row=firstQuestion;row<al.size();row++){
					if(!al.get(row)[column].equals(" ")&&!al.get(row)[segmentColumn].equals("")){
						count++;
						double e = Double.parseDouble(al.get(row)[segmentColumn])-means[segmentColumn];
						sumE += e*e;
					}
				}
				standardDeviation[segmentColumn]=Math.sqrt(sumE/count);
			}
			
			for(String level:segment.keySet()){
				bw.write("mean of segment "+level+",");
				for(int i=1;i<firstQuestion;i++){
					bw.write(",");
				}
				for(int segmentColumn=firstQuestion;segmentColumn<columnNo-1;segmentColumn++){
					bw.write((((double)segment.get(level)[segmentColumn]/levels[column].get(level)[segmentColumn])-means[segmentColumn])/standardDeviation[segmentColumn]+",");
				}
				bw.write((((double)segment.get(level)[columnNo-1]/levels[column].get(level)[columnNo-1])-means[columnNo-1])/standardDeviation[columnNo-1]+"\r\n");
			}
			bw.write("mean of total,");
			for(int i=1;i<firstQuestion;i++){
				bw.write(",");
			}
			for(int segmentColumn=firstQuestion;segmentColumn<columnNo;segmentColumn++){
				bw.write(means[segmentColumn]+",");
			}
			bw.close();
		}
	}
}
