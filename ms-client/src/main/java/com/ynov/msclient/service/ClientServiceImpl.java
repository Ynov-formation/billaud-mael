package com.ynov.msclient.service;

import com.ynov.msclient.model.Client;
import com.ynov.msclient.model.ClientDto;
import com.ynov.msclient.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {
  private final ClientRepository clientRepository;

  public ClientServiceImpl(ClientRepository clientRepository) {
    this.clientRepository = clientRepository;
  }

  @Override
  public Optional<ClientDto> create(ClientDto clientDto) {
    if (clientRepository.findByEmail(clientDto.getEmail()) != null) {
      return Optional.empty();
    }

    Client clientToSave = new Client(clientDto);
    try {
      return Optional.of(new ClientDto(clientRepository.save(clientToSave)));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<ClientDto> findById(Long id) {
    try {
      Optional<Client> result = clientRepository.findById(id);
      return result.map(ClientDto::new);
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<ClientDto> findByEmail(String email) {
    try {
      Optional<Client> result = Optional.ofNullable(clientRepository.findByEmail(email));
      return result.map(ClientDto::new);
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  @Override
  public List<ClientDto> findAll() {
    List<Client> resultClient;
    try {
      resultClient = clientRepository.findAll();
    } catch (Exception e) {
      return Collections.emptyList();
    }
    return resultClient.stream().map(ClientDto::new).toList();
  }
}
