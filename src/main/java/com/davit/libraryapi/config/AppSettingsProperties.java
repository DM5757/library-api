package com.davit.libraryapi.config;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.settings")
public class AppSettingsProperties {

    @NotBlank
    private String applicationTitle;

    @Min(1)
    private int defaultPageSize;

    @Email
    @NotBlank
    private String supportEmail;

    @NotBlank
    @URL
    private String externalServiceUrl;

    private boolean featureEnabled;
}
