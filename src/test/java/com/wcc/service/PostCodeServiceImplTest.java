package com.wcc.service;

import com.wcc.dto.PostCode;
import com.wcc.dto.PostCodeDistance;
import com.wcc.entity.PostCodeEntity;
import com.wcc.repository.PostCodeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostCodeServiceImplTest {

    @Mock
    private PostCodeRepository postCodeRepository;

    private PostCodeServiceImpl postCodeService;

    @BeforeEach
    void setUp() {
        postCodeService = new PostCodeServiceImpl(postCodeRepository);
    }

    @Test
    void testGetDistance() {
        String postCode1 = "AB10 1AA";
        String postCode2 = "AB10 6RN";
        double latitude1 = 57.144165;
        double longitude1 = -2.114848;
        double latitude2 = 57.13788;
        double longitude2 = -2.121487;
        double expectedDistance = 0.8055;

        PostCodeEntity postCodeEntity1 = new PostCodeEntity(postCode1, latitude1, longitude1);
        PostCodeEntity postCodeEntity2 = new PostCodeEntity(postCode2, latitude2, longitude2);

        when(postCodeRepository.findById(postCode1)).thenReturn(Optional.of(postCodeEntity1));
        when(postCodeRepository.findById(postCode2)).thenReturn(Optional.of(postCodeEntity2));

        PostCodeDistance result = postCodeService.getDistance(postCode1, postCode2);


        assertNotNull(result);
        assertEquals(postCode1, result.getPostCode1());
        assertEquals(latitude1, result.getLatitude1());
        assertEquals(longitude1, result.getLongitude1());
        assertEquals(postCode2, result.getPostCode2());
        assertEquals(latitude2, result.getLatitude2());
        assertEquals(longitude2, result.getLongitude2());
        assertEquals(expectedDistance, result.getDistance(), 0.001);

        verify(postCodeRepository, times(1)).findById(postCode1);
        verify(postCodeRepository, times(1)).findById(postCode2);
    }

    @Test
    void testGetDistanceWithInvalidPostCode() {

        String postCode1 = "AB10 1AA";
        String invalidPostCode = "Invalid";

        when(postCodeRepository.findById(postCode1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> postCodeService.getDistance(postCode1, invalidPostCode));

        verify(postCodeRepository, times(1)).findById(postCode1);
        verify(postCodeRepository, never()).findById(invalidPostCode);
    }

    @Test
    void testUpdatePostCodeSuccessful() {
        String postCode = "ABC1234";
        double latitude = 45.678;
        double longitude = -78.901;

        when(postCodeRepository.update(latitude, longitude, postCode)).thenReturn(1);

        PostCode updatedPostCode = postCodeService.updatePostCode(postCode, latitude, longitude);

        assertNotNull(updatedPostCode);
        assertEquals(postCode, updatedPostCode.getPostCode());
        assertEquals(latitude, updatedPostCode.getLatitude());
        assertEquals(longitude, updatedPostCode.getLongitude());

        verify(postCodeRepository, times(1)).update(latitude, longitude, postCode);
    }

    @Test
    void testUpdatePostCodeEntityNotFound() {
        String postCode = "ABC1234";
        double latitude = 45.678;
        double longitude = -78.901;

        when(postCodeRepository.update(latitude, longitude, postCode)).thenReturn(0);

        assertThrows(EntityNotFoundException.class, () -> postCodeService.updatePostCode(postCode, latitude, longitude));

        verify(postCodeRepository, times(1)).update(latitude, longitude, postCode);
    }
}
