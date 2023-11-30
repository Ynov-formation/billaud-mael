package com.ynov.msclient.controller;

import com.ynov.msclient.exception.ClientFailure;
import com.ynov.msclient.exception.FailureEnum;
import com.ynov.msclient.model.ClientDto;
import com.ynov.msclient.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("client/v1")
@CrossOrigin("*")
public class ClientController {
  private final ClientService clientService;

  public ClientController(ClientService clientService) {
    this.clientService = clientService;
  }

  @PostMapping
  public ResponseEntity<Object> create(@RequestBody ClientDto clientDto) {
    ClientDto createdClient = clientService.create(clientDto);

    if (createdClient instanceof ClientFailure clientFailure) {
      return checkClientFailure(clientFailure);
    }

    return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody ClientDto clientDto) {
    ClientDto updatedClient = clientService.update(id, clientDto);

    if (updatedClient instanceof ClientFailure clientFailure) {
      return checkClientFailure(clientFailure);
    }

    return new ResponseEntity<>(updatedClient, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> delete(@PathVariable Long id) {
    ClientDto deletedClient = clientService.delete(id);

    if (deletedClient instanceof ClientFailure clientFailure) {
      return checkClientFailure(clientFailure);
    }

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> findById(@PathVariable Long id) {
    ClientDto client = clientService.findById(id);

    if (client instanceof ClientFailure clientFailure) {
      return checkClientFailure(clientFailure);
    }

    return new ResponseEntity<>(client, HttpStatus.OK);
  }

  @GetMapping("/email/{email}")
  public ResponseEntity<Object> findByEmail(@PathVariable String email) {
    ClientDto client = clientService.findByEmail(email);

    if (client instanceof ClientFailure clientFailure) {
      return checkClientFailure(clientFailure);
    }

    return new ResponseEntity<>(client, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<ClientDto>> findAll() {

    return ResponseEntity.ok(clientService.findAll());
  }

  /**
   * Vérifie le type d'exception et retourne une ResponseEntity en fonction
   * @param clientFailure {@link ClientFailure}
   * @return {@link ResponseEntity}
   */
  private ResponseEntity<Object> checkClientFailure(ClientFailure clientFailure) {
    return switch (clientFailure.getExceptionType()) {
      case CLIENT_NOT_EXISTS -> new ResponseEntity<>(FailureEnum.CLIENT_NOT_EXISTS.getMessage(), HttpStatus.NOT_FOUND);
      case CLIENT_ALREADY_EXISTS -> ResponseEntity.badRequest().body(FailureEnum.CLIENT_ALREADY_EXISTS.getMessage());
      case DATABASE -> ResponseEntity.badRequest().body(FailureEnum.DATABASE.getMessage());
    };
  }
}
