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

    public static String passwordHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());

            StringBuilder sb = new StringBuilder();
            byte[] bytes = md.digest();
            for(int i=0; i< bytes.length ;i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            password = sb.toString();
        }
        catch (Exception e) { e.printStackTrace(); }
        return password;
    }
}
