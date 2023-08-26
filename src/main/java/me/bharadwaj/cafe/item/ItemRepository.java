package me.bharadwaj.cafe.item;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ItemRepository {

	private Connection dbConnection;

	public ItemRepository(Connection dbConnection) {
		this.dbConnection = dbConnection;
	}

	public void insertItem(int itemTypeId, String name, String serving, double quantity, double price) throws Exception {
		var sql = String.format("INSERT INTO cafe.items(name, type_id, serving, quantity, price) VALUES('%s', %s, '%s', %s, %s);", name, itemTypeId, serving, quantity, price);
		
		Statement stmnt = null;
		try {
			stmnt = dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			stmnt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			
			int itemId = 0;
			var result = stmnt.getGeneratedKeys();
			while(result.next()) {
				itemId = result.getInt("id");
				log.info("Returning item_id({}) after insert", itemId);
			}
		} catch(SQLException e) {
			stmnt.close();
			throw new Exception("Error inserting item", e);
		}

		stmnt.close();
	}
}
