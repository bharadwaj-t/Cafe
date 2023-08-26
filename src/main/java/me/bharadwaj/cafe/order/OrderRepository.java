package me.bharadwaj.cafe.order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderRepository {
	private Connection dbConnection;

	public OrderRepository(Connection conn) {
		this.dbConnection = conn;
	}
	
	public int createNewOrder(int paymentId) throws Exception {
		var currentMillis = Calendar.getInstance().getTimeInMillis();
		var currentTimestampMillis = new Timestamp(currentMillis);
		var sql = String.format("INSERT INTO cafe.orders(payment_id, create_date, update_date) VALUES(%s, '%s', '%s')", paymentId, currentTimestampMillis, currentTimestampMillis);

		try {
			var statement = dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			var result = statement.getGeneratedKeys();
			
			int orderId = 0;
			while(result.next()) {
				orderId = result.getInt("id");
				log.info("created a new order with id: {}", orderId);
			}

			statement.close();
			return orderId;
		} catch (SQLException e) {
			throw new Exception("Cannot create a new payment", e);
		}
	}
	
	public int getPaymentForOrder(int orderId) throws Exception {
		var sql = String.format("SELECT ord.payment_id FROM cafe.orders ord WHERE ord.id = %s", orderId);

		try {
			var statement = dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			var result = statement.getGeneratedKeys();
			
			int paymentId = 0;
			while(result.next()) {
				paymentId = result.getInt("payment_id");
				log.info("found payment id: {}", paymentId);
			}

			statement.close();
			return paymentId;
		} catch (SQLException e) {
			throw new Exception("Cannot create a new payment", e);
		}
	}
}
