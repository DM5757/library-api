package com.davit.libraryapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Tag(name = "Root", description = "Root entry point")
public class RootController {

    @GetMapping("/")
    @Operation(summary = "Redirect to Swagger UI", description = "Redirects the user from the root path to the Swagger UI page")
    public String redirectToSwagger() {
        return "redirect:/swagger-ui/index.html";
    }
}
