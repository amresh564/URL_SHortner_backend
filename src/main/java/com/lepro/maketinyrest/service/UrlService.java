package com.lepro.maketinyrest.service;

import com.lepro.maketinyrest.dto.UrlDto;
import org.springframework.stereotype.Service;

@Service
public class UrlService {

    public String shortUrl(UrlDto url) {
        String longUrl = url.getLongUrl();

        String uniqueID;
        String baseString = "https://ti.ny/";

        uniqueID = "asnaeb";

        return baseString + uniqueID;
    }
}
