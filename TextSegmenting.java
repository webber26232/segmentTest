import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class TextSegmenting {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String dir525 = "D:/Courses/5 2015 autumn/MKT525/Project/textResult";
		String dirMyFoodDeliver = "D:/新建文件夹/Startup/Data";
		BufferedReader brT = new BufferedReader(new FileReader(dirMyFoodDeliver+"/New_Text.csv"));
		BufferedReader brC = new BufferedReader(new FileReader(dirMyFoodDeliver+"/New_Code.csv"));
		ArrayList<String[]> alT = new ArrayList<String[]>();
		ArrayList<String[]> alC = new ArrayList<String[]>();
		String lineT;
		String lineC;
		while((lineT=brT.readLine())!=null && (lineC=brC.readLine())!=null){
			alT.add(lineT.replaceAll(", ",".").split(","));
			alC.add(lineC.replaceAll(", ",".").split(","));
		}
		brT.close();
		brC.close();
		int firstQuestion = 5;
		int columnNo = alC.get(0).length-1;
		int firstRow = 2;
		TreeMap<String,int[]>[] levels = new TreeMap[columnNo];//Be used to count numbers of every level in every question	
//		int[] count = new int[columnNo];//Be used to count the segment size
		for(int column=firstQuestion;column<columnNo;column++){
			levels[column] = new TreeMap<String,int[]>();
			TreeMap<String,int[]> hm = levels[column];
			for(int row=firstRow;row<alC.size();row++){
				String level = alC.get(row)[column];
				if(!level.equals("")){
//					count[column]++;
					if(!hm.containsKey(level)){
						hm.put(level, new int[columnNo]);
					}
					int[] ovlpRtNumbers=hm.get(level);
					for(int atbtColumn=firstQuestion;atbtColumn<columnNo;atbtColumn++){
						if(!alC.get(row)[atbtColumn].equals("")){
							ovlpRtNumbers[atbtColumn]++;
						}
					}
				}
			}
		}
		HashMap<String,String>[] levelText = new HashMap[columnNo];//Be used to store the text name of levels
		for(int column=firstQuestion;column<columnNo;column++){
			levelText[column] = new HashMap<String,String>();
			for(String level:levels[column].keySet()){
				for(int i=firstQuestion;i<alC.size();i++){
					if(alC.get(i)[column].equals(level)){
						levelText[column].put(level, alT.get(i)[column]);
						break;
					}
				}
			}
		}
		for(int column=firstQuestion;column<columnNo;column++){//create a file for each question
			BufferedWriter bw = new BufferedWriter(new FileWriter(dirMyFoodDeliver+"/result/"+alC.get(0)[column]+".csv"));
			bw.write(",,,");
			for(String level:levels[column].keySet()){
				bw.write(levelText[column].get(level)+",");//write segment name in the 1st row
			}
			bw.write("total,\r\n");
			bw.write(",,Segment Size,");
			int sizeSum = 0;
			for(String level:levels[column].keySet()){
				sizeSum += levels[column].get(level)[column];
				bw.write(levels[column].get(level)[column]+",");//write segment name in the 1st row
			}
			bw.write(sizeSum+",\r\n");
			for(int segmtColumn=firstQuestion;segmtColumn<columnNo;segmtColumn++){//test every question based on the level of question before
				int sum = 0;
				for(int row=firstRow;row<alC.size();row++){
					if(!alC.get(row)[column].equals("")&&!alC.get(row)[segmtColumn].equals("")){
						sum++;
					}
				}
				for(String segmtlevel:levels[segmtColumn].keySet()){//levels for each question
					bw.write(alC.get(0)[segmtColumn]+","+alC.get(1)[segmtColumn]+","+levelText[segmtColumn].get(segmtlevel)+",");
					for(String level:levels[column].keySet()){//levels vs levels
						int count = 0;
						for(int row=firstRow;row<alC.size();row++){
							if(alC.get(row)[column].equals(level)&&alC.get(row)[segmtColumn].equals(segmtlevel)){
								count++;
							}
						}
						bw.write((double)count/levels[column].get(level)[segmtColumn]*100+",");
					}
					int count = 0;
					for(int row=firstRow;row<alC.size();row++){
						if(!alC.get(row)[column].equals("")&&alC.get(row)[segmtColumn].equals(segmtlevel)){
							count++;
						}
					}
					bw.write((double)count/sum*100+"\r\n");
				}
			}
			bw.close();
		}
	}
}
