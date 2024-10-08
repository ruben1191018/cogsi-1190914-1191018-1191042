package payroll;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

	private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

	@Bean
	CommandLineRunner initDatabase(EmployeeRepository employeeRepository, OrderRepository orderRepository) {

		return args -> {
			employeeRepository.save(new Employee("Bilbo", "Baggins","bilbo.baggins@gmail.com", "burglar", "Software Engineer", 5));
			employeeRepository.save(new Employee("Frodo", "Baggins","frodo.baggins@gmail.com", "thief", "DevOps Engineer", 10));

			employeeRepository.findAll().forEach(employee -> log.info("Preloaded " + employee));

			
			orderRepository.save(new Order("MacBook Pro", Status.COMPLETED));
			orderRepository.save(new Order("iPhone", Status.IN_PROGRESS));

			orderRepository.findAll().forEach(order -> {
				log.info("Preloaded " + order);
			});
			
		};
	}
}
