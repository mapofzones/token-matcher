package com.mapofzones.tokenmatcher.service.token;

import com.mapofzones.tokenmatcher.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Token.TokenId> {



}
