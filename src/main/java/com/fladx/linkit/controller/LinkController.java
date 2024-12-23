package com.fladx.linkit.controller;

import com.fladx.linkit.dto.CreateLinkRequest;
import com.fladx.linkit.dto.DeleteLinkDTO;
import com.fladx.linkit.dto.EditLinkDto;
import com.fladx.linkit.model.Link;
import com.fladx.linkit.service.LinkService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LinkController {
    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @PostMapping("/link/create")
    public ResponseEntity<Link> createLink(@Valid @RequestBody CreateLinkRequest createLinkRequest) {
        var link = linkService.createLink(createLinkRequest);
        return ResponseEntity.ok(link);
    }

    @PatchMapping("/link")
    public ResponseEntity<Link> editLink(@Valid @RequestBody EditLinkDto createLinkRequest) {
        Link link = linkService.editLink(createLinkRequest);
        return ResponseEntity.ok(link);
    }

    @GetMapping("/{shortLink}")
    public ResponseEntity<Void> redirectLink(@PathVariable String shortLink) {
       return linkService.redirect(shortLink);
    }

    @DeleteMapping("/link")
    public ResponseEntity<Void> deleteLink(@Valid @RequestBody DeleteLinkDTO linkDTO) {
        linkService.deleteLink(linkDTO);

        return ResponseEntity.noContent().build();
    }
}
