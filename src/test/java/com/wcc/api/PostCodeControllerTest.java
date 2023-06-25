package com.wcc.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wcc.config.WebSecurityConfig;
import com.wcc.dto.PostCode;
import com.wcc.dto.PostCodeDistance;
import com.wcc.service.PostCodeService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PostCodeController.class)
@Import(WebSecurityConfig.class)
class PostCodeControllerTest {

    @Autowired
    WebApplicationContext context;
    @Autowired
    FilterChainProxy filterChain;

    private MockMvc mockMvc;

    @MockBean
    private PostCodeService postCodeService;


    ObjectMapper objectMapper = new ObjectMapper();



    @BeforeEach
    public void setUp() {
        this.mockMvc = webAppContextSetup(context).addFilters(filterChain).build();
    }

    @Test
    void whenDistanceInputIsInvalid() throws Exception {
        mockMvc.perform(get("/distance/{postCode1}/{postCode2}", "TW8 0JW", "ad").with(user("root").password("root"))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenDistanceUnUnauthorized() throws Exception {
        mockMvc.perform(get("/distance/{postCode1}/{postCode2}", "TW8 0JW", "ad")
                        .contentType("application/json"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenDistanceInputIsUnknown() throws Exception {
        String postCode1 = "TW8 0JW";
        String postCode2 = "NOT  OK";
        when(postCodeService.getDistance(postCode1, postCode2)).thenThrow(new EntityNotFoundException());
        mockMvc.perform(get("/distance/{postCode1}/{postCode2}", postCode1, postCode2).with(user("root").password("root"))
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenDistanceSuccess() throws Exception {

        String postCode1 = "TW8 0JW";
        String postCode2 = "TW8 0LA";
        PostCodeDistance postCodeDistance = PostCodeDistance.builder()
                .postCode1(postCode1)
                .latitude1(51.485788)
                .longitude1(-0.300685)
                .postCode2(postCode2)
                .latitude2(51.486628)
                .longitude2(-0.298414)
                .distance(13.7)
                .build();
        when(postCodeService.getDistance(postCode1, postCode2)).thenReturn(postCodeDistance);
        String contentAsString = mockMvc.perform(get("/distance/{postCode1}/{postCode2}", postCode1, postCode2).with(user("root").password("root"))
                        .contentType("application/json"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        PostCodeDistanceResponse response = objectMapper.readValue(contentAsString, PostCodeDistanceResponse.class);
        assertEquals(response.getPostCode1(), postCodeDistance.getPostCode1());
        assertEquals(response.getPostCode2(), postCodeDistance.getPostCode2());
        assertEquals(response.getLatitude1(), postCodeDistance.getLatitude1());
        assertEquals(response.getLatitude2(), postCodeDistance.getLatitude2());
        assertEquals(response.getLongitude1(), postCodeDistance.getLongitude1());
        assertEquals(response.getLongitude2(), postCodeDistance.getLongitude2());
    }

    @Test
    void testUpdatePostcode() throws Exception {
        String postCode = "AB1 1AA";
        double latitude = 57.144165;
        double longitude = -2.114848;

        PostCodeUpdateRequest updateRequest = new PostCodeUpdateRequest(postCode, latitude, longitude);
        PostCode postCodeDto = new PostCode(postCode, latitude, longitude);
        PostCodeResponse expectedResponse = new PostCodeResponse(postCode, latitude, longitude);

        when(postCodeService.updatePostCode(eq(postCode), eq(latitude), eq(longitude))).thenReturn(postCodeDto);


        mockMvc.perform(put("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)).with(user("root").password("root")))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    void testUpdatePostcodeInvalidData() throws Exception {
        String postCode = "AB1AA 1AA";
        double latitude = 57.144165;
        double longitude = -2.114848;

        PostCodeUpdateRequest updateRequest = new PostCodeUpdateRequest(postCode, latitude, longitude);

        verifyNoInteractions(postCodeService);

        mockMvc.perform(put("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)).with(user("root").password("root")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdatePostcodeUnauthorized() throws Exception {
        String postCode = "AB1A 1AA";
        double latitude = 57.144165;
        double longitude = -2.114848;

        PostCodeUpdateRequest updateRequest = new PostCodeUpdateRequest(postCode, latitude, longitude);

        verifyNoInteractions(postCodeService);

        mockMvc.perform(put("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isUnauthorized());
    }

}