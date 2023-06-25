package com.wcc.api;

import com.wcc.service.PostCodeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@Validated
@RequiredArgsConstructor
public class PostCodeController {

    private final PostCodeService postCodeService;
    private final ModelMapper modelMapper;

    @GetMapping("distance/{postCode1}/{postCode2}")
    public PostCodeDistanceResponse getDistance(@PathVariable @Size(min = 7, max = 7) String postCode1,
                                                @PathVariable @Size(min = 7, max = 7) String postCode2) {
        return modelMapper.map(postCodeService.getDistance(postCode1, postCode2), PostCodeDistanceResponse.class);
    }

    @PutMapping
    public PostCodeResponse updatePostcode(@Valid @RequestBody PostCodeUpdateRequest updateRequest) {
        return modelMapper.map(postCodeService.updatePostCode(updateRequest.getPostCode(), updateRequest.getLatitude(),
                updateRequest.getLongitude()), PostCodeResponse.class);
    }
}
