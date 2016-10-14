import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Collection;

public class SegmentEvaluate {
	private static ArrayList<String[]> alC = new ArrayList<String[]>();;
	private static ArrayList<String[]> alT = new ArrayList<String[]>();
	private static Scanner s = new Scanner(System.in);
	private static int firstQuestion;
	private static int firstRow; 
	private static HashSet<String[]> premise = new HashSet<String[]>();
	private static final String LINE_SEPARATOR = System.lineSeparator();
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		BufferedReader brT = new BufferedReader(new FileReader("D:/Courses/5 2015 autumn/MKT525/Project/textResult/New_Text.csv"));
		BufferedReader brC = new BufferedReader(new FileReader("D:/Courses/5 2015 autumn/MKT525/Project/textResult/New_Code.csv"));
		String lineT;
		String lineC;
		while((lineC=brC.readLine())!=null&&(lineT=brT.readLine())!=null){
			alC.add(lineC.replaceAll(", ","£¬").split(","));
			alT.add(lineT.replaceAll(", ","£¬").split(","));
		}
		brC.close();
		brT.close();
		firstRow = 3;
		firstQuestion = 5;
		int questionNo;
		while(true){
			System.out.println(LINE_SEPARATOR+"Please input a number:");
			System.out.println("1. Add an OR premise");
			System.out.println("2. Add an AND premise");
			System.out.println("3. Show result");
			System.out.println("4. Clear premise");
			System.out.println("5. Adjust segments");
			switch (s.next()){
			case "1":
				questionNo = chooseQuestion();
				showDistribution(questionNo,false);
				System.out.println(LINE_SEPARATOR+"Please choose a level");
				addOrPremise(questionNo);
				break;
			case "2":
				questionNo = chooseQuestion();
				showDistribution(questionNo,false);
				System.out.println(LINE_SEPARATOR+"Please choose a level");
				addAndPremise(questionNo);
				break;
			case "3":
				questionNo = chooseQuestion();
				showDistribution(questionNo,true);
				break;
			case "4":
				premise.clear();
				System.out.println(LINE_SEPARATOR+"Premise has been clear"+LINE_SEPARATOR);
				break;
			case "5":
				questionNo = chooseQuestion();
				showDistribution(questionNo,false);
				segmentAdjust(questionNo);
				break;
			default:
				System.out.println("Wrong Number"+LINE_SEPARATOR);
			}
		}	
	}
	private static int chooseQuestion(){
		System.out.println(LINE_SEPARATOR+"Please input the number of question:");
		String Qnumber = "Q"+s.next();
		for(int i=firstQuestion;i<alC.get(0).length;i++){
			if(alC.get(0)[i].matches(Qnumber)||alC.get(0)[i].matches(Qnumber+"_\\d+")){
				if(alC.get(0)[i].matches(Qnumber+"_\\d+")){
					System.out.println("This question have subselections, Please input the number of subselection:");
					for(int j=i,count=1;alC.get(0)[j].matches(Qnumber+"_\\d+");j++,count++){
						System.out.println(count+": "+alC.get(1)[j]);							
					}
					i += s.nextInt()-1;
				}
				System.out.println("The question you select is: "+alC.get(1)[i]);
				return i;
			}
		}
		return -1;
	}
	private static void addOrPremise(int question){
		String[] levels = s.next().split(",");
		for(String level:levels){
			for(int j=firstRow;j<alC.size();j++){
				if(alC.get(j)[question].equals(level)){
					premise.add(alC.get(j));
				}
			}
			System.out.println("You have put level "+level+" of "+alC.get(1)[question]+"into premise");
			System.out.println("Now there are "+premise.size()+" people satisfy the premise"+LINE_SEPARATOR);
		}
	}
	private static void addAndPremise(int question){
		HashSet<String[]> temp = new HashSet<String[]>();
		temp.addAll(premise);
		int size = premise.size();
		String[] levels = s.next().split(",");
		for(String level:levels){
			for(String[] str:temp){
				if(!str[question].equals(level)){
					premise.remove(str);
				}
			}
			System.out.println("You have add an and premise of level "+level+" for "+alC.get(1)[question]+"into premise");
		}
		System.out.println((size-premise.size())+" people were removed. Now there are "+premise.size()+" people satisfy the premise"+LINE_SEPARATOR);
	}
	private static void showDistribution(int question, boolean showSegment){
		TreeMap<Integer,Integer> distribution = new TreeMap<Integer,Integer>();
		Collection<String[]> c = showSegment? premise:alC;
		for(String[] str:c){
			if(!str[question].equals("")&&str[question].matches("\\d+")){
				int level = Integer.parseInt(str[question]);
				if(distribution.containsKey(level)){
					distribution.put(level, distribution.get(level)+1);
				}else{
					distribution.put(level, 1);
				}
			}
		}
		for(int level:distribution.keySet()){
			String slevel = "";
			for(int j=firstRow;j<alC.size();j++){
				if(alC.get(j)[question].equals(level+"")){
					slevel = alT.get(j)[question];
					break;
				}
			}
			System.out.println(level+"."+slevel+": "+distribution.get(level));
		}
	}
	private static void segmentAdjust(int question){
		System.out.println(LINE_SEPARATOR+"Please input the level needed be change");
		String fromLevel = s.next();
		String sFromLevel = "";
		for(int j=firstRow;j<alC.size();j++){
			if(alC.get(j)[question].equals(fromLevel)){
				sFromLevel = alT.get(j)[question];
				break;
			}
		}
		System.out.println("Please input the level needed be change to");
		String toLevel = s.next();
		String sToLevel = "";
		for(int j=firstRow;j<alC.size();j++){
			if(alC.get(j)[question].equals(toLevel)){
				sToLevel = alT.get(j)[question];
				break;
			}
		}
		System.out.println(LINE_SEPARATOR+"Do you really want to change the level---"+fromLevel+":"+sFromLevel+LINE_SEPARATOR+"to---"+toLevel+":"+sToLevel+LINE_SEPARATOR+"for the question of---"+alC.get(1)[question]);
		System.out.println("Please input y or n");
		switch(s.next()){
		case "y":
			for(int row=firstRow;row<alC.size();row++){
				if(alC.get(row)[question].equals(fromLevel)||alC.get(row)[question].equals(toLevel)){
					alC.get(row)[question] = toLevel;
					alT.get(row)[question] = sFromLevel+" or "+sToLevel;
				}
			}
			System.out.println("The level of---"+fromLevel+":"+sFromLevel+"---and---"+toLevel+":"+sToLevel+"have be combined to:");
			System.out.println(toLevel+":"+sFromLevel+" or "+sToLevel+LINE_SEPARATOR);
			break;
		case "n":
			System.err.println("You have canceled the level adjustion");
			break;
		default:
			System.out.println("Wrong letter");
		}
	}
}