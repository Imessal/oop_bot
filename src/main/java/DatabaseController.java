import java.sql.*;
import java.util.ArrayList;

public class DatabaseController{
    private static Connection connection;

//    enum UserInfo{
//        token,
//        movies_seen,
//    }

    public DatabaseController() {
        connection = connect();
    }

    public void createNewTable() {
        getRequest(createTableRequest());
    }

    public void addUser(Integer token) {
        getRequest(addNewUserRequest(token));
    }

    public void removeUser(Integer token) {
        getRequest(removeUserRequest(token));
    }

    public void getUserInfo(Integer token) {
        postRequest(getUserInfoRequest(token));
    }

    public void updateUserInfo(Integer token, String field, Object newValue) {
        getRequest(updateUserInfoRequest(token, field, newValue));
    }

    private Connection connect() {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        try {
            return DriverManager.getConnection(dbUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getRequest(String task) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(task);
            System.out.println("Success");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Object> postRequest(String task) {
        ArrayList values = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(task);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    values.add(resultSet.getObject(i));
                }
            }
            System.out.println(values);
            System.out.println("Success");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return values;
    }



//    private String createMoviesTableRequest(){
//        return "CREATE TABLE movies (" +
//                "movies_seen varchar(150) NULL" +
//                ");";
//    }

    private String createTableRequest() {
        return "CREATE TABLE sessions (" +
                "token integer NOT NULL PRIMARY KEY," +
                "movies_seen varchar(200) NULL" +
                ");";
    }

    private String addNewUserRequest(Integer token) {
        return String.format("INSERT INTO sessions (token) VALUES (%d);", token);
    }

    private String removeUserRequest(Integer token) {
        return String.format("DELETE FROM sessions WHERE token = %d;", token);
    }

    private String getUserInfoRequest(Integer token) {
        return String.format("SELECT * FROM sessions WHERE token = %d;", token);
    }

    private String updateUserInfoRequest(Integer token, String column, Object value) {
        return String.format("UPDATE sessions SET %s = \'%s\' WHERE token = %d;", column, value, token);
    }
//
//    public static void main(String args[]){
//        DatabaseController controller = new DatabaseController();
//        controller.createNewTable();
//        controller.addUser(1234);
//        controller.getUserInfo(1234);
//        controller.updateUserInfo(1234, UserInfo.movies_seen, "Зверополис");
//        controller.removeUser(1234);
//    }
}