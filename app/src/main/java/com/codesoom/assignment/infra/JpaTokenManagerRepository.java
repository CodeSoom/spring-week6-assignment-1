package com.codesoom.assignment.infra;

import com.codesoom.assignment.domain.TokenManagerRepository;
import com.codesoom.assignment.domain.TokenManager;
import org.springframework.data.repository.CrudRepository;

public interface JpaTokenManagerRepository
        extends TokenManagerRepository, CrudRepository<TokenManager, Long> {
    TokenManager save(TokenManager tokenManager);
}
