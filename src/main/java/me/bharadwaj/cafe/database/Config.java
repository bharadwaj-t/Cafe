package me.bharadwaj.cafe.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.bharadwaj.cafe.ApplicationPropertiesReader;

@Slf4j
@Data
public class Config {

	private String userName;
	private String password;
	private String host;
	private String schemaCreatorScriptName;
	private String schemaDeleter;
	private Connection conn;

	public Config() {
		this.userName = ApplicationPropertiesReader.getProperty("database.user.name");
		this.password = ApplicationPropertiesReader.getProperty("database.password");
		this.host = ApplicationPropertiesReader.getProperty("database.host");
		this.schemaCreatorScriptName = ApplicationPropertiesReader.getProperty("database.schema.creator");
		this.schemaDeleter = ApplicationPropertiesReader.getProperty("database.tear.down.schema");
	}

	public void connectToDB() throws SQLException {
		var url = String.format("jdbc:postgresql://%s/postgres?user=%s&password=%s&ssl=false", "localhost", userName,
				password, host);
		this.conn = DriverManager.getConnection(url);
	}

	public void setupSchema() throws IOException, SQLException {
		var inputStream = getClass().getClassLoader().getResourceAsStream(schemaCreatorScriptName);
		var sql = new BufferedReader(new InputStreamReader(inputStream)).lines().parallel()
				.collect(Collectors.joining("\n"));
		var statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		statement.execute(sql);
	}
	
	public void close() throws SQLException {
		conn.close();
	}
	
	public void tearDownSchema() throws SQLException {
		var statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		statement.execute(schemaDeleter);
	}
	
	// just a test sql method 
	// TODO: remove.
	public void bootstrappingSql() throws SQLException {
		var cal = Calendar.getInstance();
		var millis = cal.getTimeInMillis();
		var timestamp = new Timestamp(millis);

		var sql = String.format("INSERT INTO timestamp_demo(tstz) VALUES('%s')", timestamp);
		var statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
		var result = statement.getGeneratedKeys();
		while(result.next()) {
			log.info("keys: {}", result.getObject("id"));
		}
	}
}
