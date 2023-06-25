package com.wcc.service;

import com.wcc.dto.PostCode;
import com.wcc.dto.PostCodeDistance;
import com.wcc.entity.PostCodeEntity;
import com.wcc.repository.PostCodeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostCodeServiceImpl implements PostCodeService {

    private final PostCodeRepository postCodeRepository;
    private static final double EARTH_RADIUS = 6371;

    @Override
    public PostCodeDistance getDistance(String postCode1, String postCode2) {
        PostCodeEntity postCodeEntity1 = postCodeRepository.findById(postCode1).orElseThrow(() -> new EntityNotFoundException("Post code doesn't exists:" + postCode1));
        PostCodeEntity postCodeEntity2 = postCodeRepository.findById(postCode2).orElseThrow(() -> new EntityNotFoundException("Post code doesn't exists:" + postCode2));
        double distance = calculateDistance(postCodeEntity1.getLatitude(), postCodeEntity1.getLongitude(), postCodeEntity2.getLatitude(), postCodeEntity2.getLongitude());
        log.debug("Distance between post codes {} and {} is {}", postCode1, postCode2, distance);
        return new PostCodeDistance(postCode1, postCodeEntity1.getLatitude(), postCodeEntity1.getLongitude(), postCode2, postCodeEntity2.getLatitude(), postCodeEntity2.getLongitude(), distance);
    }

    @Override
    public PostCode updatePostCode(String postCode, double latitude, double longitude) {
        if(1 == postCodeRepository.update(latitude, longitude, postCode)){
            return PostCode.builder().postCode(postCode).latitude(latitude).longitude(longitude).build();
        }
        throw new EntityNotFoundException("Post code doesn't exists:" + postCode);
    }


    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}