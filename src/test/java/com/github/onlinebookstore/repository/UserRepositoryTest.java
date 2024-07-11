package com.github.onlinebookstore.repository;

import com.github.onlinebookstore.model.Role;
import com.github.onlinebookstore.model.User;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Test
    void findByEmailWithRoles_withValidEmail_ReturnsUser() {
        String email = "email@email.com";
        User expected = new User();
        expected.setEmail(email);
        Role role = new Role();
        role.setName(Role.RoleName.ROLE_USER);
        roleRepository.save(role);
        expected.setRoles(Set.of(role));
        expected.setPassword("password");
        expected.setFirstName("FirstName");
        expected.setLastName("LastName");

        userRepository.save(expected);
        Optional<User> actual = userRepository.findByEmailWithRoles(email);

        Assertions.assertEquals(expected, actual.get(),
                "Expected user does not match actual user");
    }
}