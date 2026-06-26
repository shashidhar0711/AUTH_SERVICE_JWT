package com.mslearning.AUTH_SERVICE_JWT.repositories;

import com.mslearning.AUTH_SERVICE_JWT.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {

}
