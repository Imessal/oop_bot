import java.sql.*;
import java.util.ArrayList;

class DatabaseRepository {
    private static Connection connection;

    DatabaseRepository() {
        connection = connect();
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

    private ResultSet executeQuery(String request) {
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

    void createMainTable() {
        executeUpdate("CREATE TABLE if not exists users(id integer NOT NULL PRIMARY KEY, username text, first_name text);");
        executeUpdate("CREATE TABLE if not exists movies(id integer NOT NULL PRIMARY KEY, name text, link text NOT NULL, rating text, year text);");
        executeUpdate("CREATE TABLE if not exists black_list(u_id integer NOT NULL, m_id integer NOT NULL, PRIMARY KEY(u_id, m_id))");
    }

    void addUser(User user) {
        executeUpdate(String.format("INSERT INTO users VALUES (%d, \'%s\', \'%s\') ON CONFLICT DO NOTHING;",
                user.getId(), queryInspector(user.username), queryInspector(user.first_name)));
    }

    void addMovieToBlackList(User user, Movie movie) {
        addMovie(movie);
        executeUpdate(String.format("INSERT INTO black_list VALUES (%d, %d) ON CONFLICT DO NOTHING;",
                user.getId(), movie.getId()));
    }

    private void addMovie(Movie movie) {
        executeUpdate(String.format("INSERT INTO movies VALUES (%d, \'%s\',\'%s\',\'%s\',\'%s\') ON CONFLICT DO NOTHING;",
                movie.getId(),
                queryInspector(movie.getName()),
                queryInspector(movie.getLink()),
                queryInspector(movie.getRating()),
                queryInspector(movie.getYear())));
    }

    boolean checkMovie(User user, Movie movie) {
        ResultSet result = executeQuery(String.format("SELECT m_id FROM black_list WHERE u_id = %d AND m_id = %d", user.getId(), movie.getId()));
        try {
            return !(result != null && result.next());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private String updateUserInfoRequest(Integer token, String column, Object value) {
        return String.format("UPDATE users SET %s = \'%s\' WHERE u_id = %d;", column, value, token);
    }

    //Защита от инъекций однако! Ну минимальная защита
    private String queryInspector(String query) {
        if (query == null) {
            return null;
        }
        return query
                .replaceAll("\\\\", "\\\\\\\\")
                .replaceAll("'", "\\\\'")
                .replaceAll(";", "\\\\;");
    }
}