package sample;

import java.security.MessageDigest;

public class UserDetail
{
    private String username;
    private String email;
    private String password;

    public UserDetail() {}
    public UserDetail(String username, String email, String password) {
        this.username = username; this.email = email; this.password = password;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }

    /**
     * Method Name : passwordHash
     * Purpose : Get the hash of the password
     * @param password - user's password
     * @return - the hash of the password
     */
    public static String passwordHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());

            StringBuilder sb = new StringBuilder();
            byte[] bytes = md.digest();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }// end for

            password = sb.toString();
        }
        catch (Exception ignored) { } // end try/catch
        return password;

    }// end passwordHash(String)

}// end UserDetail class
