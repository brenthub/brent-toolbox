package cn.brent.toolbox;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
	static String str = " 101  @achi  200  @a低碳阴谋：中国与欧美的生死之战@ADI TAN LIN MOU@f勾红洋著  210  @a太原@c山西经济出版社  690  @aX511@v4"; 

	public static void main(String[] args) {
		regTest();

	}
	
	public static void  spiltTest(){
		String regex = "(?<= )([.]*)(?= \\d{3})"; 
		String[] strs = str.split(regex); 
		for(int i = 0; i < strs. length; i++) { 
		    System. out.printf( "%s%n", strs[i]); 
		}
	}
	
	public static void regTest(){
		String s="go go";
		Pattern p=Pattern.compile("(?<word>\\w+)\\s+\\1");
		Matcher m=p.matcher(s);
		System.out.println(m.find());
		System.out.println(m.group(""));
	}

}
