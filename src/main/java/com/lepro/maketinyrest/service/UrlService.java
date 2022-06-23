package com.lepro.maketinyrest.service;

import com.lepro.maketinyrest.dto.UrlDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UrlService {
    private Map<String, String> longToShort;
    private Map<String, String> shortToLong;

    public UrlService() {
        longToShort = new HashMap<String, String>();
        shortToLong = new HashMap<String, String>();
    }

    public String shortUrl(UrlDto url, String baseUrl) {
        String longUrl = url.getLongUrl();
        String id = generateUniqueId(longUrl);

        longToShort.put(longUrl, id);
        shortToLong.put(id, longUrl);

        return "http://www.tiney/kjdf";
//        return baseUrl + id;
    }

    public String getLongUrl(UrlDto urlDto) {
        return shortToLong.get(urlDto.getShortUrl());
    }

    private String generateUniqueId(String url) {
        return "";
    }
}
