package com.fladx.linkit.service;

import com.fladx.linkit.config.LinkConfig;
import com.fladx.linkit.dto.CreateLinkRequest;
import com.fladx.linkit.model.Link;
import com.fladx.linkit.model.User;
import com.fladx.linkit.repository.LinkRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class LinkServiceTest {
    @Mock
    private LinkRepository linkRepository;

    @Mock
    private UserService userService;

    @Mock
    private LinkConfig linkConfig;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private LinkService linkService;

    @Test
    void testGetLinkByShortLink_Success() {
        // Arrange
        String shortUrl = "abcd";
        Link mockLink = new Link();
        mockLink.setShortUrl(shortUrl);
        mockLink.setOriginalUrl("http://example.com");

        Mockito.when(linkRepository.findByShortUrl(shortUrl)).thenReturn(Optional.of(mockLink));

        // Act
        Link result = linkService.getLinkByShortLink(shortUrl);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals("http://example.com", result.getOriginalUrl());
        Mockito.verify(linkRepository).findByShortUrl(shortUrl);
    }

    @Test
    void testGetLinkByShortLink_NotFound() {
        // Arrange
        String shortUrl = "abcd";
        Mockito.when(linkRepository.findByShortUrl(shortUrl)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> linkService.getLinkByShortLink(shortUrl));
    }

    @Test
    void testRedirect_LimitExceeded() {
        // Arrange
        String shortUrl = "abcd";
        Link mockLink = new Link();
        UUID uuid = UUID.randomUUID();

        mockLink.setShortUrl(shortUrl);
        mockLink.setOriginalUrl("http://example.com");
        mockLink.setClickCount(5);
        mockLink.setLimitExceeded(5);
        mockLink.setUserId(uuid);
        mockLink.setLocked(false);

        Mockito.when(linkRepository.findByShortUrl(shortUrl)).thenReturn(Optional.of(mockLink));

        // Act
        ResponseEntity<Void> response = linkService.redirect(shortUrl);

        // Assert
        Assertions.assertEquals(HttpStatus.LOCKED, response.getStatusCode());
        Mockito.verify(notificationService).sendNotification(uuid, "Исчерпаны лимиты перехода по ссылке");
        Mockito.verify(linkRepository).save(mockLink);
    }

    @Test
    void testRedirect_SuccessfulRedirect() {
        // Arrange
        String shortUrl = "abcd";
        Link mockLink = new Link();
        UUID uuid = UUID.randomUUID();

        mockLink.setShortUrl(shortUrl);
        mockLink.setOriginalUrl("http://example.com");
        mockLink.setClickCount(3);
        mockLink.setLimitExceeded(5);
        mockLink.setUserId(uuid);
        mockLink.setLocked(false);

        Mockito.when(linkRepository.findByShortUrl(shortUrl)).thenReturn(Optional.of(mockLink));

        // Act
        ResponseEntity<Void> response = linkService.redirect(shortUrl);

        // Assert
        Assertions.assertEquals(HttpStatus.PERMANENT_REDIRECT, response.getStatusCode());
        Assertions.assertEquals("http://example.com", Objects.requireNonNull(response.getHeaders().getLocation()).toString());
        Mockito.verify(linkRepository).save(mockLink);
    }

    @Test
    void testCreateLink() {
        // Arrange
        CreateLinkRequest request = new CreateLinkRequest("http://example.com", null, 10);
        User mockUser = new User();
        UUID uuid = UUID.randomUUID();
        mockUser.setId(uuid);

        Mockito.when(userService.createUser()).thenReturn(mockUser);
        Mockito.when(linkConfig.getLifetime()).thenReturn(86400);

        Link savedLink = new Link();
        savedLink.setShortUrl("abcd");
        savedLink.setOriginalUrl("http://example.com");

        Mockito.when(linkRepository.save(any(Link.class))).thenReturn(savedLink);

        // Act
        Link result = linkService.createLink(request);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals("http://example.com", result.getOriginalUrl());
        Mockito.verify(linkRepository).save(any(Link.class));
    }

    @Test
    void testCheckExpired() {
        // Arrange
        Link expiredLink = new Link();
        UUID uuid = UUID.randomUUID();
        expiredLink.setUserId(uuid);

        Mockito.when(linkRepository.findAllByExpiredAtBefore(any(LocalDateTime.class))).thenReturn(List.of(expiredLink));

        // Act
        linkService.checkExpired();

        // Assert
        Mockito.verify(notificationService).sendNotification(uuid, "Ваша ссылка удалена");
        Mockito.verify(linkRepository).delete(expiredLink);
    }
}