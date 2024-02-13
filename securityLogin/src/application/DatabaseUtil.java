package application;

import java.sql.*;

public class DatabaseUtil {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/Users";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void saveKey(String userName, String key) throws Exception {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        String sql = "INSERT INTO encryption_keys (user_name, encryption_key) VALUES (?, ?) ON DUPLICATE KEY UPDATE encryption_key = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);

        //encrypt key before saving
        String encryptedKey = SecureKeyUtil.encryptKey(key);

        pstmt.setString(1, userName);
        pstmt.setString(2, encryptedKey);
        pstmt.setString(3, encryptedKey); //for any duplicate keys
        pstmt.executeUpdate();
        conn.close();
    }

    public static String loadKey(String userName) throws Exception {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        String sql = "SELECT encryption_key FROM encryption_keys WHERE user_name = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, userName);
        ResultSet rs = pstmt.executeQuery();
        String encryptedKey = null;
        if (rs.next()) {
            encryptedKey = rs.getString("encryption_key");
        }
        conn.close();

        //decrypts key when retrieving
        return encryptedKey != null ? SecureKeyUtil.decryptKey(encryptedKey) : null;
    }
}
