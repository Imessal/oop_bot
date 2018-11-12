import java.sql.*;
import java.util.ArrayList;

class DatabaseController{
    private static Connection connection;

    DatabaseController() {
        connection = connect();
    }

    private Connection connect() {
        String dbUrl = "jdbc:postgresql://ec2-54-195-246-59.eu-west-1.compute.amazonaws.com:5432/d85qs8kb1qsqjs?user=skutpslmhajgws&password=9202c08527f52d3b34023bc75baacf481ed1ce9044de197aeaff3e58e9a6d8ec&sslmode=require";
        //String dbUrl = System.getenv("JDBC_DATABASE_URL");
        try {
            return DriverManager.getConnection(dbUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ResultSet executeQuery(String request){
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(request);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void executeUpdate(String task) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(task);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void createMainTable(){
        executeUpdate("CREATE TABLE if not exists a_users(u_id integer NOT NULL PRIMARY KEY, username text);");
    }

    void addUser(User user) {
        executeUpdate(String.format("INSERT INTO a_users VALUES (%d, \'%s\') ON CONFLICT DO NOTHING;", user.id, user.username));
        executeUpdate(String.format("CREATE TABLE if not exists black_list_%d (m_id integer NOT NULL PRIMARY KEY, m_name text);", user.id));
    }

    void addMovieToBlackList(User user, Movie movie){
        executeUpdate(String.format("INSERT INTO black_list_%d VALUES (%d, \'%s\') ON CONFLICT DO NOTHING;", user.id, movie.id, movie.name));
    }

    boolean checkMovie(User user, Movie movie) {
        ResultSet result = executeQuery(String.format("SELECT m_id FROM black_list_%d WHERE m_id = %d", user.id, movie.id));
        try {
            return !(result != null && result.next());
        } catch (SQLException e) {
            e.printStackTrace();
        } return true;
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return values;
    }

    private String updateUserInfoRequest(Integer token, String column, Object value) {
        return String.format("UPDATE users SET %s = \'%s\' WHERE u_id = %d;", column, value, token);
    }
}