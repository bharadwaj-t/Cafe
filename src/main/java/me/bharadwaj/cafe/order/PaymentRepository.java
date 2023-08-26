package me.bharadwaj.cafe.order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PaymentRepository {
	private Connection dbConnection;

	public PaymentRepository(Connection conn) {
		this.dbConnection = conn;
	}

	public int createNewPayment() throws Exception {
		var currentMillis = Calendar.getInstance().getTimeInMillis();
		var currentTimestampMillis = new Timestamp(currentMillis);
		var sql = String.format("INSERT INTO cafe.payments(create_date, update_date) VALUES('%s', '%s');",
				currentTimestampMillis, currentTimestampMillis);

		try {
			var statement = dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			var result = statement.getGeneratedKeys();

			int paymentId = 0;
			while (result.next()) {
				paymentId = result.getInt("id");
				log.info("created a new payment with id: {}", paymentId);
			}

			statement.close();
			return paymentId;
		} catch (SQLException e) {
			throw new Exception("Cannot create a new payment", e);
		}
	}

	public void updatePaymentForAnOrder(int orderId, boolean isSettled) throws Exception {
		var sql = String.format(
				"UPDATE cafe.payments SET amount_recorded = (SELECT SUM(price) FROM cafe.items i WHERE i.id IN (SELECT item_id FROM cafe.selected_items si WHERE si.order_id = %s)), settled = %s "
						+ "WHERE id = (SELECT payment_id FROM cafe.orders o WHERE o.id IN (SELECT order_id FROM cafe.selected_items si WHERE si.order_id = %s));",
				orderId, isSettled, orderId);

		try {
			var statement = dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			var status = statement.executeUpdate(sql);
			log.info("Payment update status: {}", status);
			statement.close();
		} catch (SQLException e) {
			throw new Exception("Cannot update payment", e);
		}
	}
}
