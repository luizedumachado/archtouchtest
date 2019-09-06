package mygroup.myproject;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Unit test for simple App.
 */
public class App
{
	private static final String REGEX = " ";
	public static void main( String[] args ) throws org.json.simple.parser.ParseException, IOException, NoSuchAlgorithmException
    {
		
        Pattern p = Pattern.compile(REGEX);
        
        File file = new File("passwords.json");
        Scanner scanner = new Scanner(file);
        String fileContent = "[{";
        String username = "";
        String domain = "";
        String password = "";
        String passwordMd5 = "";
        String passwordSha256 = "";
        String format = "md5";
        String format2 = "sha256";
        
        while (scanner.hasNextLine()) {
        String[] line = p.split(scanner.nextLine());
        int count = 0; //0 = user; 1 = password; 2 = domain
        for(String s : line) {
        	if (count == 0) {
        		username = s;
        	}
        	else if (count == 1) {
        		password = s;
        		MessageDigest m=MessageDigest.getInstance("MD5");
                m.update(password.getBytes(),0,password.length());
                passwordMd5 = new BigInteger(1,m.digest()).toString(16);
        		
                MessageDigest m2 = MessageDigest.getInstance("SHA-256");
                byte messageDigest[] = m2.digest(password.getBytes("UTF-8"));
                passwordSha256 = Base64.getEncoder().encodeToString(messageDigest);
        	}
        	else if (count == 2) {
        		domain = s;
        	}
        	count++;
        }
        fileContent = fileContent.concat("\n\t\"username\": " + "\"" + username + "@" + domain + "\"," + "\n" + "\t\"passwords\": [{ \n\t\t\"password\": "
        			+ "\"" + passwordMd5 + "\"" + ",\n\t\t\"password_format\": " + "\"" + format + "\"" + "\n\t},{\n" +  "\n\t\t\"password\": "
        			+ "\"" + passwordSha256 + "\"" + ",\n\t\t\"password_format\": " + "\"" + format2 + "\"" + "\n\t}]\n}");
        if (scanner.hasNextLine()){
        	fileContent = fileContent.concat(",{");
        }
        else fileContent = fileContent.concat("]");
        }
        
        FileWriter writer = new FileWriter("newpasswords.json");
		writer.write(fileContent);
		writer.close();
		scanner.close();
    }
 
}