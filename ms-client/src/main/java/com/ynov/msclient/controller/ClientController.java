package com.ynov.msclient.controller;

import com.ynov.msclient.model.ClientDto;
import com.ynov.msclient.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/clients")
@CrossOrigin("*")
public class ClientController {
  private final ClientService clientService;

  public ClientController(ClientService clientService) {
    this.clientService = clientService;
  }

  @PostMapping
  public ResponseEntity<ClientDto> create(@RequestBody ClientDto clientDto) {
    return clientService.create(clientDto)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.badRequest().build());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ClientDto> findById(@PathVariable Long id) {
    return clientService.findById(id)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping("/email/{email}")
  public ResponseEntity<ClientDto> findByEmail(@PathVariable String email) {
    return clientService.findByEmail(email)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<List<ClientDto>> findAll() {
    return ResponseEntity.ok(clientService.findAll());
  }
}
