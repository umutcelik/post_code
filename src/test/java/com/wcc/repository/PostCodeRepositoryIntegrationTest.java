package com.wcc.repository;

import com.wcc.entity.PostCodeEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@Transactional
class PostCodeRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PostCodeRepository postCodeRepository;

    @Test
    void testFindById() {
        
        String postCode = "AB10 1AA";
        double latitude = 57.144165;
        double longitude = -2.114848;

        PostCodeEntity postCodeEntity = new PostCodeEntity(postCode, latitude, longitude);
        entityManager.persist(postCodeEntity);
        entityManager.flush();

        
        Optional<PostCodeEntity> result = postCodeRepository.findById(postCode);

        assertTrue(result.isPresent());
        assertEquals(latitude, result.get().getLatitude(), 0.001);
        assertEquals(longitude, result.get().getLongitude(), 0.001);
    }

    @Test
    void testFindByIdEntityNotFound() {
        
        String nonExistingPostCode = "AB11 1AB";
        Optional<PostCodeEntity> result = postCodeRepository.findById(nonExistingPostCode);
        assertFalse(result.isPresent());
    }

    @Test
    void testUpdateSuccessful() {
        PostCodeEntity postCodeEntity = new PostCodeEntity("ABC1234", 45.678, -78.901);
        entityManager.persist(postCodeEntity);
        entityManager.flush();

        double newLatitude = 12.345;
        double newLongitude = -45.678;
        String postCode = "ABC1234";
        int rowsUpdated = postCodeRepository.update(newLatitude, newLongitude, postCode);

        assertEquals(1, rowsUpdated);

        entityManager.clear();

        PostCodeEntity updatedEntity = entityManager.find(PostCodeEntity.class, postCode);

        assertNotNull(updatedEntity);
        assertEquals(newLatitude, updatedEntity.getLatitude());
        assertEquals(newLongitude, updatedEntity.getLongitude());
    }


    @Test
    void testUpdateEntityNotFound() {

        double newLatitude = 12.345;
        double newLongitude = -45.678;
        String postCode = "NonExistent";
        int rowsUpdated = postCodeRepository.update(newLatitude, newLongitude, postCode);

        assertEquals(0, rowsUpdated);
    }
}