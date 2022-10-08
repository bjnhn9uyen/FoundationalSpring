package tacos.data;

import org.springframework.data.repository.CrudRepository;

import tacos.User;

public interface UserRepository extends CrudRepository<User, Long> {

	User findByUsername(String username);
	/**
	 * When generating the repository implementation, Spring Data examines any methods in the repository
	 * interface, parses the method name, and attempts to understand the method’s purpose in the context of
	 * the persisted object. Repository methods are composed of a verb, an optional subject, the word “By”,
	 * and a predicate. In the case of ‘findByUsername’, the verb is “find” and the predicate is “Username”,
	 * the subject isn’t specified and is implied to be a User. Spring Data knows that this method is intended
	 * to find a User, because you’ve parameterized CrudRepository with User. This method should find all User
	 * entities by matching their ‘username’ property with the value passed in as a parameter to the method
	 */

}
