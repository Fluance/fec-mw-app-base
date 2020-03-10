/**
 * 
 */
package net.fluance.app.spring.data.jpa.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;

import net.fluance.app.spring.data.jpa.repository.IJPAGenericRepository;
import net.fluance.app.spring.data.jpa.test.model.TestPerson;
import net.fluance.app.test.AbstractTest;

@SpringApplicationConfiguration(classes = Application.class)
public class JPAGenericRepositoryTest extends AbstractTest {

	@Autowired
	private IJPAGenericRepository<TestPerson, Long> repository;

	@Test
	public void testSavePersists() {
		TestPerson person = new TestPerson();
		person.setFirstName("Oscar");
		person.setLastName("Toto");
		TestPerson newPerson = (TestPerson) repository.save(person);
		assertTrue("Oscar".equals(newPerson.getFirstName()) && "Toto".equals(newPerson.getLastName()));
	}

	@Test
	public void testUpdate() {
		TestPerson person = new TestPerson();
		person.setFirstName("Oscar");
		person.setLastName("Toto");
		TestPerson newPerson = (TestPerson) repository.save(person);
		newPerson.setFirstName("Emile");
		newPerson.setLastName("Titi");
		TestPerson updatedPerson = (TestPerson) repository.save(newPerson);
		assertTrue("Emile".equals(updatedPerson.getFirstName()) && "Titi".equals(updatedPerson.getLastName()));
	}
	
	@Test
	public void testDelete() {
		TestPerson person = new TestPerson();
		person.setFirstName("Oscar");
		person.setLastName("Toto");
		TestPerson newPerson = (TestPerson) repository.save(person);
		repository.delete(newPerson);
		assertTrue(repository.count() == 0);
	}

	@Test
	public void testFindById() {
		TestPerson person = new TestPerson();
		person.setFirstName("Oscar");
		person.setLastName("Toto");
		TestPerson newPerson = (TestPerson) repository.save(person);
		repository.delete(newPerson);
		assertTrue(repository.count() == 0);
	}

}
