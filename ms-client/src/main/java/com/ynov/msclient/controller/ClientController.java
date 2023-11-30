package com.ynov.msclient.controller;

import com.ynov.msclient.model.ClientDto;
import com.ynov.msclient.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    Optional<ClientDto> createdClient = clientService.create(clientDto);

    if (createdClient.isEmpty()) {
      return ResponseEntity.badRequest().body("Erreur lors de la création ou le client existe déjà");
    }

    return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody ClientDto clientDto) {
    Optional<ClientDto> updatedClient = clientService.update(id, clientDto);

    if (updatedClient.isEmpty()) {
      return ResponseEntity.badRequest().body("Erreur lors de la mise à jour ou le client n'existe pas");
    }

    return new ResponseEntity<>(updatedClient, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> delete(@PathVariable Long id) {
    if (!clientService.delete(id)) {
      return ResponseEntity.badRequest().body("Erreur lors de la suppression ou le client n'existe pas");
    }

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ClientDto> findById(@PathVariable Long id) {
    return clientService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/email/{email}")
  public ResponseEntity<ClientDto> findByEmail(@PathVariable String email) {
    return clientService.findByEmail(email).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<List<ClientDto>> findAll() {
    return ResponseEntity.ok(clientService.findAll());
  }
}
