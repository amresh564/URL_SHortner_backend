package com.lepro.maketinyrest.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lepro.maketinyrest.dto.UrlDto;
import com.lepro.maketinyrest.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Hashtable;

@RestController
public class UrlController {

    @Autowired
    UrlService urlService;

    private String baseUrl;

    /*
     * User wants to short the url
     */
    @CrossOrigin
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
        url.setQrUrl("https://www."+longUrl);
        return new ResponseEntity<Object>(url, HttpStatus.OK);
    }


    /*
     *   User want to get the long url form the shortened url
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<Object> redirectUser(@PathVariable String id, HttpServletRequest req) {
        UrlDto urlDto = new UrlDto();
        baseUrl = "" + req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/";
        urlDto.setShortUrl(baseUrl + id);

        urlDto.setLongUrl(urlService.getLongUrl(urlDto));
        System.out.println(urlDto);

        String urlToRedirect = urlDto.getLongUrl().toString();
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("https://www." + urlToRedirect)).build();
    }

    @GetMapping(path = "/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getImage(@PathVariable String id, HttpServletRequest req) throws WriterException, IOException {

        UrlDto urlDto = new UrlDto();
        baseUrl = "" + req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/";
        urlDto.setShortUrl(baseUrl + id);

        urlDto.setLongUrl(urlService.getLongUrl(urlDto));
        System.out.println(urlDto);

        String urlToRedirect = urlDto.getLongUrl().toString();

        QRCodeWriter writer = new QRCodeWriter();
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
        BitMatrix bitMatrix = writer.encode("https://www." + urlToRedirect, BarcodeFormat.QR_CODE, 400, 400, hintMap);

        int matrixWidth = bitMatrix.getWidth();
        int matrixHeight = bitMatrix.getHeight();

        BufferedImage image = new BufferedImage(matrixWidth, matrixHeight, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixHeight);
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixHeight; j++) {
                if (bitMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", baos);
        return baos.toByteArray();
    }
}
