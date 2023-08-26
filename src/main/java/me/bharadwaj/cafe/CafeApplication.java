package me.bharadwaj.cafe;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;
import me.bharadwaj.cafe.database.Config;
import me.bharadwaj.cafe.item.ItemRepository;
import me.bharadwaj.cafe.item.ItemTypesRepository;
import me.bharadwaj.cafe.order.OrderRepository;
import me.bharadwaj.cafe.order.PaymentRepository;
import me.bharadwaj.cafe.order.SelectedItemRepository;

@Slf4j
public class CafeApplication {

	public static void main(String[] args) {
		var dbConfig = new Config();
		try {
			dbConfig.connectToDB();
			dbConfig.setupSchema();
//			dbConfig.bootstrappingSql();
		} catch (SQLException e) {
			log.error("GODDMAN: ", e);
		} catch (IOException e) {
			log.error("HUH: ", e);
		}
		
		var itemTypesRepository = new ItemTypesRepository(dbConfig.getConn());
		var itemRepository = new ItemRepository(dbConfig.getConn());
		var paymentsRepository = new PaymentRepository(dbConfig.getConn());
		var orderRepository = new OrderRepository(dbConfig.getConn());
		var selectedItemRespository = new SelectedItemRepository(dbConfig.getConn());
		
		// Scenario: basically introduce new type and item at the same time and process an order
		try {
			var id = itemTypesRepository.insertItemType("PIZZA");
			itemRepository.insertItem(id, "Margharita", "REGULAR", 100, 350);
			
			var payId = paymentsRepository.createNewPayment();
			var orderId = orderRepository.createNewOrder(payId);
			selectedItemRespository.insertSelectedItem(Arrays.asList(1, 2, 3), orderId);
			paymentsRepository.updatePaymentForAnOrder(orderId, true);
			
			
			// call these two first to initiate order
			payId = paymentsRepository.createNewPayment();
			orderId = orderRepository.createNewOrder(payId);
			
			// insert selected items and process payment
			selectedItemRespository.insertSelectedItem(Arrays.asList(3, 4), orderId);
			paymentsRepository.updatePaymentForAnOrder(orderId, true);
			// done, serve the items. 
		} catch (Exception e) {
			log.error("Couldn't create order", e);
		}
		
		// close DB
		try {
			dbConfig.close();
		} catch (SQLException e) {
			log.error("Can't release DB connection", e);
		}
		
		log.info("Its over. 💀");
		// yeah, so you need to drop the schema before running the application again: sql script and the inserts above violate unique constraint.
//		dbConfig.tearDownSchema();
	}

}
