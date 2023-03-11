import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class EmailTracker {
    
    private Connection conn;
    
    public EmailTracker() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost/email_tracker";
            String username = "root";
            String password = "password";
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void trackEmail(String email) {
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO email_stats (email, time_opened, count) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE time_opened=?, count=count+1");
            stmt.setString(1, email);
            stmt.setTimestamp(2, new Timestamp(new Date().getTime()));
            stmt.setInt(3, 1);
            stmt.setTimestamp(4, new Timestamp(new Date().getTime()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int getEmailCount(String email) {
        int count = 0;
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT count FROM email_stats WHERE email=?");
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    
    public static void main(String[] args) {
        EmailTracker tracker = new EmailTracker();
        String email = "example@example.com";
        tracker.trackEmail(email);
        int count = tracker.getEmailCount(email);
        System.out.println("Email " + email + " has been opened " + count + " times.");
    }

}