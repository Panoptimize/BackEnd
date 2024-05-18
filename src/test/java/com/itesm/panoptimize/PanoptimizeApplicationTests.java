package com.itesm.panoptimize;

import com.itesm.panoptimize.model.Company;
import com.itesm.panoptimize.model.User;
import com.itesm.panoptimize.model.UserType;
import com.itesm.panoptimize.repository.CompanyRepository;
import com.itesm.panoptimize.repository.UserRepository;
import com.itesm.panoptimize.repository.UserTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
class PanoptimizeApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserTypeRepository userTypeRepository;
	@Autowired
	private CompanyRepository companyRepository;

	@Test
	public void testFindUserById() throws Exception {
		User user= new User();
		user.setFullName("Andrés Torres");
		user.setEmail("atorres@tec.mx");
		user.setConnectId(UUID.randomUUID().toString());
		UserType ut= userTypeRepository.findById(2).get();
		user.setUserType(ut);
		Company company= companyRepository.findById(1L).get();
		user.setCompany(company);
		User savedUser= userRepository.save(user);
		System.out.println(savedUser.getId());
		System.out.println("Encontré el usuario: "+ user.getFullName());

		mockMvc.perform(MockMvcRequestBuilders
						.get("/agent/"+savedUser.getId())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.jsonPath("id").value(savedUser.getId()))
				.andExpect(MockMvcResultMatchers.jsonPath("name").value(savedUser.getFullName()))
		;
		userRepository.delete(savedUser);
	}
	@Test
	public void testNotFound() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
						.get("/agent/2")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
		;
	}


}
