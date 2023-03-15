package com.example.deezerpullingservice;

import com.example.deezerpullingservice.service.ArtistService;
import com.example.deezerpullingservice.service.TrackService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest
public class WebTest {

    @MockBean
    ArtistService artistService;

    @MockBean
    TrackService trackService;

    @Test
    void test() {
    }

}
