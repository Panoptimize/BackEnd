package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.contact.ContactDTO;
import com.itesm.panoptimize.dto.contact.CreateContactDTO;
import com.itesm.panoptimize.model.Contact;
import com.itesm.panoptimize.model.User;
import com.itesm.panoptimize.repository.ContactRepository;
import com.itesm.panoptimize.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    private Contact convertToEntity(ContactDTO contactDTO) {
        return modelMapper.map(contactDTO, Contact.class);
    }

    private ContactDTO convertToDTO(Contact contact) {
        return modelMapper.map(contact, ContactDTO.class);
    }

    public ContactService(ContactRepository contactRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public void deleteContact(String id) {
        contactRepository.deleteById(id);
    }

    public ContactDTO getContact(String id) {
        return convertToDTO(contactRepository.findById(id).orElse(null));
    }

    public Page<ContactDTO> getAllContacts(Pageable pageable) {
        return contactRepository.findAll(pageable).map(this::convertToDTO);
    }

    public ContactDTO createContact(CreateContactDTO contactDTO) {
        Contact contact = new Contact();
        contact.setId(contactDTO.getId());
        contact.setSatisfaction(contactDTO.getSatisfaction());

        User agent = userRepository.connectId(contactDTO.getAgentId()).orElseThrow(
                () -> new IllegalArgumentException("Agent not found")
        );
        contact.setUser(agent);
        return convertToDTO(contactRepository.save(contact));
    }

}
