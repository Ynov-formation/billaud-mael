package com.ynov.msoperation.repository;

import com.ynov.msoperation.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, Long> {}
