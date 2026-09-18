package com.spring.urlshortner.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@NoArgsConstructor
public class ShortenURLRequest {

    @NotBlank
    @URL
    private String url;
}
