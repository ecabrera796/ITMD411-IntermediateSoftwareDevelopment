/*
 * Erick Cabrera
 * Dec. 9, 2018
 * User.java
 * Tickets_FA18
 */

import java.security.MessageDigest;

public class User {
    public String name;
    public String role;
    public User(String name, String role){
        this.name = name;
        this.role = role;
    }
    // create hash passwords
    public static String hashPassword(String password){
        String generatedPassword = null;
        try{
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(password.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        }catch(Exception ex){
        }
        return  generatedPassword;
    }
}
