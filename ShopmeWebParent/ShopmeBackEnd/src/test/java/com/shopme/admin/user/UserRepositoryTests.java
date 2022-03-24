package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);		
		User userName = new User("test@test.com", "test2022", "Winthrop","Shillingford");
		userName.addRole(roleAdmin);
		
		User savedUser = repo.save(userName);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateUserWithTwoRole() {
		User userName = new User("test2@test.com", "test2022", "Winthrop","Shillingford");
		
		Role roleEditor = new Role (3);
		Role roleAssistant = new Role (5);
		userName.addRole(roleEditor);
		userName.addRole(roleAssistant);
		
		User savedUser = repo.save(userName);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(new Consumer<User>() {
			@Override
			public void accept(User user) {
				System.out.println(user);				
			};				
		});
	}
	
	@Test
	public void testGetUserById() {
		User userName = repo.findById(1).get();
		System.out.println(userName);
		assertThat(userName).isNotNull();	
	}
	
	@Test
	public void testUpdateUserDetails() {
		User userName = repo.findById(35).get();
		userName.setEnabled(true);
		userName.setEmail("namejavaprogrammer@gmail.com");
		
		repo.save(userName);		
	}
	
	@Test
	public void testUpdateUserRoles() {
		User userName = repo.findById(1).get();
		Role roleEditor = new Role(4);
		Role roleSalesperson = new Role(5);
		
		userName.getRoles().remove(roleEditor);
		userName.addRole(roleSalesperson);		

		repo.save(userName);		
	}	
	
	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);		
	}
	
	@Test
	public void testGetUserByEmail() {
		
		String email = "test@test.com";
		User user = repo.getUserByEmail(email);
		
		assertThat(user).isNotNull();
		
	}
	
	@Test
	public void testCountById() {
		Integer id = 1;
		Long countById = repo.countById(id);
		System.out.println("Number " + countById);
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
	
}
