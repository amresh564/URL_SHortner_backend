package com.lepro.maketinyrest.controller;

import com.lepro.maketinyrest.dto.UrlDto;
import com.lepro.maketinyrest.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlController {

    @Autowired
    UrlService urlService;

    @GetMapping(path = "/short", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getShortUrl(@RequestParam String longUrl) {

        if (longUrl.trim().equals(""))
            return new ResponseEntity<Object>("Please Enter Some Url", HttpStatus.BAD_REQUEST);

        UrlDto url = new UrlDto();
        url.setLongUrl(longUrl);

        String shortUrl = urlService.shortUrl(url);
        url.setShortUrl(shortUrl);

        return new ResponseEntity<Object>(url, HttpStatus.OK);
    }
}
