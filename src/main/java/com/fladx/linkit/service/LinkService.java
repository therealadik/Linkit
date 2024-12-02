package com.fladx.linkit.service;

import com.fladx.linkit.config.LinkConfig;
import com.fladx.linkit.dto.CreateLinkRequest;
import com.fladx.linkit.dto.EditLinkDto;
import com.fladx.linkit.model.Link;
import com.fladx.linkit.model.User;
import com.fladx.linkit.repository.LinkRepository;
import com.fladx.linkit.util.LinkMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class LinkService {
    private final LinkRepository linkRepository;
    private final UserService userService;
    private final LinkConfig linkConfig;
    private final NotificationService notificationService;
    private final LinkMapper linkMapper;

    public LinkService(LinkRepository linkRepository, UserService userService, LinkConfig linkConfig, NotificationService notificationService, LinkMapper linkMapper) {
        this.linkRepository = linkRepository;
        this.userService = userService;
        this.linkConfig = linkConfig;
        this.notificationService = notificationService;
        this.linkMapper = linkMapper;
    }

    public Link getLinkByShortLink(String shortLink) {
        return linkRepository.findByShortUrl(shortLink)
                .orElseThrow(() -> new EntityNotFoundException("Ссылки не существует"));
    }

    public ResponseEntity<Void> redirect(String shortUrl) {
        Link link = getLinkByShortLink(shortUrl);
        if (link.isLocked() == false && link.getClickCount() >= link.getLimitExceeded()) {
            notificationService.sendNotification(link.getUserId(), "Исчерпаны лимиты перехода по ссылке");
            link.setLocked(true);
            linkRepository.save(link);

            return ResponseEntity.status(HttpStatus.LOCKED).build();
        }

        link.setClickCount(link.getClickCount() + 1);
        linkRepository.save(link);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(link.getOriginalUrl()))
                .build();
    }

    public Link editLink(EditLinkDto linkDto) {
        if (linkDto.id() == null)
            throw new RuntimeException("Отсутсвует ID ссылки");

        Link existingLink = linkRepository.findById(linkDto.id())
                .orElseThrow(() -> new EntityNotFoundException("Link not found"));

        if (existingLink.getUserId().equals(linkDto.ownerUser()) == false) {
            throw new RuntimeException("Юзер не владелец ссылки");
        }

        linkMapper.updateLinkFromDTO(linkDto, existingLink);
        linkRepository.save(existingLink);

        return existingLink;
    }

    @Transactional
    public Link createLink(CreateLinkRequest createLinkRequest) {
        User user;

        if (createLinkRequest.userId() == null) {
            user = userService.createUser();
        } else {
            user = userService.getUser(createLinkRequest.userId());
        }

        String shortUrl = getShortUrl();

        LocalDateTime expiredTime = LocalDateTime.now(Clock.systemUTC()).plusSeconds(linkConfig.getLifetime());

        Link link = new Link();
        link.setShortUrl(shortUrl);
        link.setOriginalUrl(createLinkRequest.originalUrl());
        link.setUserId(user.getId());

        if (createLinkRequest.limitExceeded() == null)
            link.setLimitExceeded(linkConfig.getLimitExceeded());
        else
            link.setLimitExceeded(Math.max(createLinkRequest.limitExceeded(), linkConfig.getLimitExceeded()));

        link.setExpiredAt(expiredTime);

        linkRepository.save(link);
        return link;
    }

    private String getShortUrl() {
        String shortUrl;

        do {
            shortUrl = UUID.randomUUID().toString().substring(0, 4);
        } while (linkRepository.findByShortUrl(shortUrl).isPresent());

        return shortUrl;
    }

    @Scheduled(cron = "0 * * * * *")
    protected void checkExpired() {
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
        List<Link> expiredLinks = linkRepository.findAllByExpiredAtBefore(now);

        for (Link link : expiredLinks) {
            notificationService.sendNotification(link.getUserId(), "Ваша ссылка удалена");
            linkRepository.delete(link);
        }
    }
}
