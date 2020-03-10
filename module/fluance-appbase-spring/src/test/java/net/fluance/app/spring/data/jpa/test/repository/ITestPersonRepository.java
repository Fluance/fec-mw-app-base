package net.fluance.app.spring.data.jpa.test.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import net.fluance.app.spring.data.jpa.repository.IJPAGenericRepository;
import net.fluance.app.spring.data.jpa.test.model.TestPerson;

@Component
public interface ITestPersonRepository extends
		IJPAGenericRepository<TestPerson, Long> {

	List<TestPerson> findByFirstName(String fName);
	List<TestPerson> findByLastName(String lName);
	
}
