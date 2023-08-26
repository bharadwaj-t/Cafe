package me.bharadwaj.cafe.item;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ItemTypesRepository {
	private Connection dbConnection;
	
	public ItemTypesRepository(Connection dConnection) {
		this.dbConnection = dConnection;
	}
	
	public int insertItemType(String type) throws Exception {
		var sql = String.format("INSERT INTO cafe.item_types(value) VALUES('%s');", type);
		Statement stmnt = null;
		try {
			stmnt = dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			stmnt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			
			int itemTypeId = 0;
			var result = stmnt.getGeneratedKeys();
			while(result.next()) {
				itemTypeId = result.getInt("id");
				log.info("Returning item_types_id({}) after insert", itemTypeId);
			}
			stmnt.close();
			return itemTypeId;
		} catch (SQLException e) {
			stmnt.close();
			throw new Exception("Error trying to insert item type", e);
		}
	}
}