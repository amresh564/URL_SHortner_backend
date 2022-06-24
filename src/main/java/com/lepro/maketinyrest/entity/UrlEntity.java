package com.lepro.maketinyrest.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UrlEntity {
    @Id
    private Long id;

    private String shortUrl;
    private String longUrl;

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
