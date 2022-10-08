package tacos.data;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import tacos.Taco;
import tacos.Order;

@Repository
public class JdbcOrderRepository implements OrderRepository {

	// rather than use the cumbersome PreparedStatementCreator with KeyHolder, we use SimpleJdbcInsert,
	// an object that wraps JdbcTemplate to make it easier to insert data into a table
	private SimpleJdbcInsert orderInserter;
	private SimpleJdbcInsert orderTacoInserter;
	private ObjectMapper objectMapper;

	@Autowired
	public JdbcOrderRepository(JdbcTemplate jdbc) {
		this.orderInserter = new SimpleJdbcInsert(jdbc).withTableName("Taco_Order")
								.usingGeneratedKeyColumns("id");
		this.orderTacoInserter = new SimpleJdbcInsert(jdbc).withTableName("Taco_Order_Tacos");
		this.objectMapper = new ObjectMapper();
	}

	@Override
	public Order save(Order order) {
		order.setPlacedAt(new Date());
		// save Order object into Taco_Order table and return the latest inserted Order id
		long orderId = saveOrderDetails(order);

		// the latest inserted Order id is used for saving into Taco_Order_Tacos
		order.setId(orderId);
		List<Taco> tacos = order.getTacos();
		for (Taco taco : tacos) {
			saveTacoToOrder(taco, orderId); // save each Taco in the list into the Taco_Order_Tacos table
		}
		return order;
	}

	private long saveOrderDetails(Order order) {
		// because the Order object has a lot of properties, using ObjectMapper to map an Order object
		// into a Map is much easier than copying each property from the Order object into a Map
		@SuppressWarnings("unchecked")
		Map<String, Object> values = objectMapper.convertValue(order, Map.class);

		// ObjectMapper converts the “placedAt” property of the Order object into a long value, which is
		// incompatible with the Date type of “placedAt” property, so we have to set it back to Date type
		values.put("placedAt", order.getPlacedAt());

		// longValue() converts Number object (returned by the executeAndReturnKey method) to a long value
		long orderId = orderInserter.executeAndReturnKey(values).longValue();
		return orderId;
	}

	private void saveTacoToOrder(Taco taco, long orderId) {
		// in this case, we have only two properties, no need to use ObjectMapper
		Map<String, Object> values = new HashMap<>();
		values.put("tacoOrder", orderId);
		values.put("taco", taco.getId());
		orderTacoInserter.execute(values);
	}

}
