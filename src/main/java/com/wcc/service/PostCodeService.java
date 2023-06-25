package com.wcc.service;

import com.wcc.dto.PostCode;
import com.wcc.dto.PostCodeDistance;
import org.springframework.stereotype.Service;

public interface PostCodeService {
    PostCodeDistance getDistance(String postCode1, String postCode2);

    PostCode updatePostCode(String postCode, double latitude, double longitude);
}
