package com.fladx.linkit.repository;

import com.fladx.linkit.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    Optional<Link> findByShortUrl(String shortUrl);

    List<Link> findAllByExpiredAtBefore(LocalDateTime expiredAtBefore);
}
