package me.bharadwaj.cafe.order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SelectedItemRepository {
	private Connection dbConnection;

	public SelectedItemRepository(Connection conn) {
		this.dbConnection = conn;
	}

	public List<Integer> insertSelectedItem(List<Integer> itemIdList, int orderId) throws Exception {
		var values = new StringBuffer("VALUES");
		for (Integer itemId : itemIdList) {
			if (itemIdList.indexOf(itemId) == (itemIdList.size() - 1)) {
				values.append(String.format("(%s, %s);", orderId, itemId));
				break;
			}
			values.append(String.format("(%s, %s),", orderId, itemId));
		}

		var sql = String.format("INSERT INTO cafe.selected_items(order_id, item_id) %s", values);

		try {
			var statement = dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			var result = statement.getGeneratedKeys();
			
			var insertedItems = new ArrayList<Integer>();
			while(result.next()) {
				insertedItems.add(result.getInt("id"));
			}
			log.info("inserted selected items: {}", insertedItems);

			statement.close();
			return insertedItems;
		} catch (SQLException e) {
			throw new Exception("Cannot insert selected items", e);
		}
	}
}
