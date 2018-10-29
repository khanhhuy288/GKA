import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.shape.Line;
import sun.nio.cs.ext.MacHebrew;

public class GkaReader {
	public static void main(String[] args) throws FileNotFoundException {
		File file = new File("./gka-Dateien/graph01.gka");
		Scanner input = new Scanner(file);

		while(input.hasNextLine()) {
		    String line = input.nextLine();
		
		}
		
//		String line = "hellofuck";
//		Pattern r = Pattern.compile("^hello(fuck)?");
//		Matcher m = r.matcher(line);
//		if (m.find()) {
//			System.out.println(m.group(1));
//		}
		
		String regex = "^\\s*(\\w+)\\s*((->|--)\\s*(\\w+)\\s*(\\(\\s*(\\w+)\\s*\\)\\s*)?(:\\s*(\\d+)\\s*)?)?;$";
		String line = " 1->5(a)  ;";
		Pattern r = Pattern.compile(regex);
		Matcher matcher = r.matcher(line);
		if (matcher.matches()) {
			String nameNode1 = matcher.group(1);
			System.out.println(nameNode1);
			String direction = matcher.group(3);
			System.out.println(direction);
			String nameNode2 = matcher.group(4);
			System.out.println(nameNode2);
			String edgeName = matcher.group(6);
			System.out.println(edgeName);
			String edgeWeight = matcher.group(8);
			System.out.println(edgeWeight);

		}
		
	   
	   

		input.close();
	}
}
