
package DBConnect;
import java.sql.*;
/**
 *
 * @author kagam
 */
// Singleton class for Database Access
public class SingletonDatabase {

    private static final SingletonDatabase INSTANCE = new SingletonDatabase();

    private SingletonDatabase() {
        // Private constructor to prevent instantiation
    }

    public static SingletonDatabase getInstance() {
        return INSTANCE;
    }

    /**
     * Đọc kiểu dữ liệu từ cơ sở dữ liệu
     *
     * @return kiểu dữ liệu hoặc chuỗi rỗng nếu không có kết quả
     */
    public String readFromDatabase() {
        String query = "SELECT data_type FROM data_type_selection LIMIT 1";
        try (Connection connection = NNTDB.getConnectionServer();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getString("data_type");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log lỗi chi tiết nếu xảy ra
        }
        return ""; // Trả về giá trị mặc định
    }
}
