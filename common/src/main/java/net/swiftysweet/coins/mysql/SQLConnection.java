package net.swiftysweet.coins.mysql;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SQLConnection {

    private MysqlDataSource dataSource = new MysqlDataSource();
    private Connection connection;

    private static final Map<String, SQLConnection> databases = new HashMap<>();

    private SQLConnection(String host, int port, String username, String password, String database,
                          Map<String, String> tables) {

        dataSource.setServerName(host);
        dataSource.setPort(port);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setDatabaseName(database);

        dataSource.setEncoding("UTF-8");
        dataSource.setAutoReconnect(true);
        dataSource.setUseSSL(false);
        try {
            dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Executor.getExecutor(this).execute(true, String.format("CREATE DATABASE IF NOT EXISTS `%s`", database));

        for (String table : tables.keySet()) {
            final String value = tables.get(table);
            final String sql = String.format("CREATE TABLE IF NOT EXISTS `%s` (%s)", table, value);
            Executor.getExecutor(this).execute(true, sql);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void refreshConnection() {
        try {
            if (connection != null && !connection.isClosed() && connection.isValid(1000)) {
                return;
            }
            this.connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Все нахуй обосралось с MySql");
        }
    }

    public static SQLBuilder newBuilder() {
        return new SQLBuilder();
    }

    public static class SQLBuilder implements Builder<SQLConnection> {

        private String host = "localhost",
                password = "",
                username = "root",
                database = "mysql";
        private int port = 3306;
        private Map<String, String> tables = new HashMap<>();

        public SQLBuilder setDatabase(String database) {
            this.database = database;
            return this;
        }

        public SQLBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public SQLBuilder setPort(int port) {
            this.port = port;
            return this;
        }

        public SQLBuilder setHost(String host) {
            this.host = host;
            return this;
        }

        public SQLBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public SQLBuilder createTable(String table, String value) {
            this.tables.put(table, value);
            return this;
        }

        @Override
        public SQLConnection build() {
            SQLConnection connection = databases.getOrDefault(database, null);
            if (connection == null) {
                connection = new SQLConnection(this.host, this.port, this.username,
                        this.password, this.database, this.tables);
                databases.put(database, connection);
            }
            return connection;
        }
    }

}
