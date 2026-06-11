package com.davit.libraryapi.controller;

import com.davit.libraryapi.config.AppSettingsProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/metadata")
@RequiredArgsConstructor
@Tag(name = "Metadata", description = "Application metadata and settings")
public class MetadataController {

    private final AppSettingsProperties appSettings;
    private final Environment env;

    @Operation(summary = "Get application metadata", description = "Returns values from AppSettingsProperties and active profiles")
    @GetMapping
    public Map<String, Object> getMetadata() {
        log.info("Metadata requested");
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("applicationTitle", appSettings.getApplicationTitle());
        metadata.put("defaultPageSize", appSettings.getDefaultPageSize());
        metadata.put("supportEmail", appSettings.getSupportEmail());
        metadata.put("externalServiceUrl", appSettings.getExternalServiceUrl());
        metadata.put("featureEnabled", appSettings.isFeatureEnabled());
        metadata.put("activeProfiles", env.getActiveProfiles());
        return metadata;
    }
}
