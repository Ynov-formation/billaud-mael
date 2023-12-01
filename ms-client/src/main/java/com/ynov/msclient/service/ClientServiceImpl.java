package com.ynov.msclient.service;

import com.ynov.msclient.exception.ClientFailure;
import com.ynov.msclient.exception.FailureEnum;
import com.ynov.msclient.model.Client;
import com.ynov.msclient.model.ClientDto;
import com.ynov.msclient.repository.ClientRepository;
import com.ynov.msclient.util.Util;
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
  public ClientDto create(ClientDto clientDto) {
    if (clientRepository.findByEmail(clientDto.getEmail()) != null) {
      return new ClientFailure(FailureEnum.CLIENT_ALREADY_EXISTS);
    }

    Client clientToSave = new Client(clientDto);
    try {
      return new ClientDto(clientRepository.save(clientToSave));
    } catch (Exception e) {
      return new ClientFailure(FailureEnum.DATABASE);
    }
  }

  @Override
  public ClientDto update(Long id, ClientDto client) {
    try {
      Optional<Client> clientExisting = clientRepository.findById(id);
      if (clientExisting.isEmpty()) {
        return new ClientFailure(FailureEnum.CLIENT_NOT_EXISTS);
      }

      client.setId(id);
      Util.copyNonNullProperties(new Client(client), clientExisting.get());
      return new ClientDto(clientRepository.save(clientExisting.get()));
    } catch (Exception e) {
      return new ClientFailure(FailureEnum.DATABASE);
    }
  }

  @Override
  public ClientDto delete(Long id) {
    try {
      if (!clientRepository.existsById(id)) {
        return new ClientFailure(FailureEnum.CLIENT_NOT_EXISTS);
      }
      clientRepository.deleteById(id);
      return new ClientDto();
    } catch (Exception e) {
      return new ClientFailure(FailureEnum.DATABASE);
    }

  }

  @Override
  public ClientDto findById(Long id) {
    try {
      Optional<Client> result = clientRepository.findById(id);
      return result.map(ClientDto::new).orElseGet(() -> new ClientFailure(FailureEnum.CLIENT_NOT_EXISTS));
    } catch (Exception e) {
      return new ClientFailure(FailureEnum.DATABASE);
    }
  }

  @Override
  public ClientDto findByEmail(String email) {
    try {
      Optional<Client> result = Optional.ofNullable(clientRepository.findByEmail(email));
      return result.map(ClientDto::new).orElseGet(() -> new ClientFailure(FailureEnum.CLIENT_NOT_EXISTS));
    } catch (Exception e) {
      return new ClientFailure(FailureEnum.DATABASE);
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
