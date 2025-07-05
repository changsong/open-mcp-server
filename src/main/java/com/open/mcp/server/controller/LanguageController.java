package com.open.mcp.server.controller;

import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

@RestController
public class LanguageController {

    @Data
    public static class LanguageRequest {
        private String language;
    }

    @PostMapping("/api/setLanguage")
    public void setLanguage(@RequestBody LanguageRequest request) {
        Locale locale = new Locale(request.getLanguage());
        LocaleContextHolder.setLocale(locale);
    }
} 