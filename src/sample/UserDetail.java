package sample;

import java.security.MessageDigest;

public class UserDetail
{

    private String username, fullName;
    private String email;
    private String password;

    public UserDetail() {}

    public UserDetail(String username, String fullName, String email, String password) {
        this.username = username; this.fullName = fullName;
        this.email = email; this.password = password;
    }

    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }

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
