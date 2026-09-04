package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnessioneDatabase {
	private static ConnessioneDatabase instance;
    public Connection connection;
	private final String nome = "postgres";
	private final String password = "Tigrocane";
	private final String url = "jdbc:postgresql://localhost:5432/progettocasinò";
	private final String driver = "org.postgresql.Driver";

	private ConnessioneDatabase() throws SQLException {
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, nome, password);

		} catch (ClassNotFoundException e) {
			throw new SQLException("driver non trovato");
		}
	}


    public static ConnessioneDatabase getInstance() throws SQLException {
		if (instance == null) instance = new ConnessioneDatabase();
		else if (instance.connection.isClosed()) instance = new ConnessioneDatabase();
		return instance;
	}
}