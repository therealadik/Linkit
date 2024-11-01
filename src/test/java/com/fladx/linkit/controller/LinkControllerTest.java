package com.fladx.linkit.controller;


import com.fladx.linkit.service.LinkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LinkControllerTest {

    @Mock
    private LinkService linkService;

    @InjectMocks
    private LinkController linkController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(linkController).build();
    }

    @Test
    void testCreateLink() throws Exception {
        mockMvc.perform(post("/link/create")
                        .param("originalUrl", "https://example.com"))
                .andExpect(status().isOk())
                .andExpect(result -> assertEquals("Link created", result.getResponse().getContentAsString()));

    }
}