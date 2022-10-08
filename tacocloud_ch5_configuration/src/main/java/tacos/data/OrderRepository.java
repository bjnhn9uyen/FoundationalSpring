package tacos.data;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import tacos.Order;
import tacos.User;

public interface OrderRepository extends CrudRepository<Order, Long> {

	// along with adding this method, you also add the necessary findByUser method (contains “findByUser”)
	List<Order> findByUserOrderByPlacedAtDesc(User user, Pageable pageable);

}
