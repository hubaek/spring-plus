package org.example.expert.domain.user.repository;

import org.example.expert.domain.user.entity.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@CacheConfig(cacheNames = "users")
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Cacheable(key = "#nickname", unless = "#result == null ")
    User findByNickname(String nickname);
}
