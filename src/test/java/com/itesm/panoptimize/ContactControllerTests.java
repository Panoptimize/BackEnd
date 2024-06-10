package com.itesm.panoptimize;

import com.itesm.panoptimize.dto.contact.ContactDTO;
import com.itesm.panoptimize.dto.contact.CreateContactDTO;
import com.itesm.panoptimize.dto.contact.SearchContactsDTO;
import com.itesm.panoptimize.service.ContactService;
import com.itesm.panoptimize.service.ContactSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactService contactService;

    @MockBean
    private ContactSearchService contactSearchService;

    private ContactDTO contactDTO;
    private CreateContactDTO createContactDTO;
    private SearchContactsDTO searchContactsDTO;

    @BeforeEach
    public void setUp() {
        contactDTO = new ContactDTO("1", "John Doe", "john.doe@example.com", "1234567890");
        createContactDTO = new CreateContactDTO("John Doe", "john.doe@example.com", "1234567890");
        searchContactsDTO = new SearchContactsDTO("John Doe");

        Mockito.when(contactService.getContact("1")).thenReturn(contactDTO);
        Mockito.when(contactService.createContact(createContactDTO)).thenReturn(contactDTO);
        Mockito.when(contactService.getAllContacts(Mockito.any(Pageable.class))).thenReturn(new PageImpl<>(Collections.singletonList(contactDTO)));
    }

    @Test
    public void testGetContactById() throws Exception {
        mockMvc.perform(get("/contact/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contactDTO.getId()))
                .andExpect(jsonPath("$.name").value(contactDTO.getName()))
                .andExpect(jsonPath("$.email").value(contactDTO.getEmail()))
                .andExpect(jsonPath("$.phone").value(contactDTO.getPhone()));
    }

    @Test
    public void testCreateContact() throws Exception {
        mockMvc.perform(post("/contact/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"phone\": \"1234567890\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(contactDTO.getId()))
                .andExpect(jsonPath("$.name").value(contactDTO.getName()))
                .andExpect(jsonPath("$.email").value(contactDTO.getEmail()))
                .andExpect(jsonPath("$.phone").value(contactDTO.getPhone()));
    }

    @Test
    public void testDeleteContact() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/contact/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetAllContacts() throws Exception {
        mockMvc.perform(get("/contact/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(contactDTO.getId()))
                .andExpect(jsonPath("$.content[0].name").value(contactDTO.getName()))
                .andExpect(jsonPath("$.content[0].email").value(contactDTO.getEmail()))
                .andExpect(jsonPath("$.content[0].phone").value(contactDTO.getPhone()));
    }

    @Test
    public void testSearchContacts() throws Exception {
        mockMvc.perform(post("/contact/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"query\": \"John Doe\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contacts", hasSize(1)))
                .andExpect(jsonPath("$.contacts[0].id").value(contactDTO.getId()))
                .andExpect(jsonPath("$.contacts[0].name").value(contactDTO.getName()))
                .andExpect(jsonPath("$.contacts[0].email").value(contactDTO.getEmail()))
                .andExpect(jsonPath("$.contacts[0].phone").value(contactDTO.getPhone()));
    }
}
