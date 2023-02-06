package com.api.customerbase.services;

import com.api.customerbase.dto.ClientDTO;
import com.api.customerbase.entities.Client;
import com.api.customerbase.repositories.ClientRepository;
import com.api.customerbase.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ClientService {
    private ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    public Page<ClientDTO> findPaginated(PageRequest pageRequest) {
        Page<Client> obj = repository.findAll(pageRequest);
        return obj.map(item -> new ClientDTO(item));
    }

    public ClientDTO findById(UUID id) {
        Optional<Client> obj = repository.findById(id);
        Client client = obj.orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));
        return new ClientDTO(client);
    }
    @Transactional
    public ClientDTO insert(ClientDTO dto){
        Client entity = new Client();

        if(dto.getCpf().length() < 11){
            throw new IllegalArgumentException("CPF deve ter no mínimo 11 caracteres.");
        }
        if(dto.getChildren() < 0){
            throw new IllegalArgumentException("Quantidade filhos não pode ser menor que 0.");
        }
        copyProperties(dto, entity);
        entity = repository.save(entity);
        return new ClientDTO(entity);
    }
    @Transactional
    public ClientDTO update(UUID id, ClientDTO dto){
        try{
            Client entity = repository.getReferenceById(id);
            copyProperties(dto, entity);
            entity = repository.save(entity);
            return new ClientDTO(entity);
        }
        catch (EntityNotFoundException error){
            throw new ResourceNotFoundException("Entity Not Found!");
        }
    }
    @Transactional
    public void delete(UUID id) {
        try{
            repository.deleteById(id);
        }
        catch (EmptyResultDataAccessException error){
            throw new ResourceNotFoundException("Entity Not Found!");
        }
        catch (DataIntegrityViolationException error){
            throw new DataIntegrityViolationException("Erro!");
        }
    }

    public void copyProperties(ClientDTO dto, Client entity){
        entity.setName(dto.getName());
        entity.setCpf(dto.getCpf());
        entity.setIncome(dto.getIncome());
        entity.setBirthDate(dto.getBirthDate());
        entity.setChildren(dto.getChildren());
    }
}
