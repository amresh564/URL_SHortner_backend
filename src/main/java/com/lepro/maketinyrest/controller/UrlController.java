package com.lepro.maketinyrest.controller;

import com.lepro.maketinyrest.dto.UrlDto;
import com.lepro.maketinyrest.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UrlController {

    @Autowired
    UrlService urlService;

    private String baseUrl;

    /*
     * User wants to short the url
     */
    @CrossOrigin(origins = "*")
    @GetMapping(path = "/short", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getShortUrl(@RequestParam String longUrl, HttpServletRequest req) {
        if (longUrl.trim().equals(""))
            return new ResponseEntity<Object>("Please Enter Some Url", HttpStatus.BAD_REQUEST);

        baseUrl = "" + req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/";

        UrlDto url = new UrlDto();
        url.setLongUrl(longUrl);

        String shortUrl = urlService.shortUrl(url, baseUrl);
        url.setShortUrl(shortUrl);

        return new ResponseEntity<Object>(url, HttpStatus.OK);
    }


    /*
     *   User want to get the long url form the shortened url
     */
    @GetMapping("/{id}")
    public RedirectView redirectUser(@PathVariable String id, HttpServletRequest req) {
        UrlDto urlDto = new UrlDto();

        urlDto.setShortUrl(id);

        urlDto.setLongUrl(urlService.getLongUrl(urlDto));
        return new RedirectView(urlDto.getLongUrl());
    }
}
