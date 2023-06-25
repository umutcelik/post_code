package com.wcc.repository;

import com.wcc.entity.PostCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostCodeRepository extends JpaRepository<PostCodeEntity, String> {
    @Transactional
    @Modifying
    @Query("update PostCodeEntity p set p.latitude = ?1, p.longitude = ?2 where p.postCode = ?3")
    int update(double latitude, double longitude, String postCode);

}
