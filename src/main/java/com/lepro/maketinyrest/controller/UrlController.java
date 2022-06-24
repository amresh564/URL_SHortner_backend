package com.lepro.maketinyrest.controller;

import com.lepro.maketinyrest.dto.UrlDto;
import com.lepro.maketinyrest.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin()
public class UrlController {

    @Autowired
    UrlService urlService;

    private String baseUrl;

    /*
     * User wants to short the url
     */
    @GetMapping(path = "/short", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getShortUrl(@RequestParam String longUrl, HttpServletRequest req) {
        if (longUrl.trim().equals(""))
            return new ResponseEntity<Object>("Please Enter Some Url", HttpStatus.BAD_REQUEST);

        baseUrl = "" + req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/";

        UrlDto url = new UrlDto();
        url.setLongUrl(longUrl);

        System.out.println(longUrl);

        String shortUrl = urlService.shortUrl(url, baseUrl);
        url.setShortUrl(shortUrl);

        return new ResponseEntity<Object>(url, HttpStatus.OK);
    }


    /*
     *   User want to get the long url form the shortened url
     */
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> redirectUser(@PathVariable String id, HttpServletRequest req) {
        UrlDto urlDto = new UrlDto();
        baseUrl = "" + req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/";
        urlDto.setShortUrl(baseUrl + id);

        urlDto.setLongUrl(urlService.getLongUrl(urlDto));
        System.out.println(urlDto.getLongUrl());
        return new ResponseEntity<Object>(urlDto, HttpStatus.OK);
    }
}
