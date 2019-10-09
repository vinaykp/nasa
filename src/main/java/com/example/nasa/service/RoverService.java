package com.example.nasa.service;

import com.example.nasa.exception.InvalidRequestException;
import com.example.nasa.model.Mars;
import com.example.nasa.model.Photo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoverService {

    @Autowired
    private NasaApi nasaApi;

    @Autowired
    private ResourceLoader resourceLoader;

    public List<Mars> getAllMarsPhotos() {
        List<String> datesList = getRequestDates();
        List<Mars> marsList = new ArrayList<>();

        datesList.parallelStream()
                .map(s -> getMarsPhotos(s))
                .filter(s -> s.size() > 0)
                .map(p -> new Mars(p))
                .forEachOrdered(mars -> {
                    marsList.add(mars);
                });
        return marsList;
    }

    public List<Photo> getMarsPhotos(String earth_date) {
        try {
            LocalDate date = LocalDate.parse(earth_date);
        } catch (DateTimeParseException e) {
            throw new InvalidRequestException(e.getMessage());
        }
        ResponseEntity<Mars> responseEntity = nasaApi.callMarsPhotosAPI(earth_date);
        List<Photo> photos = new ArrayList<>();
        if (responseEntity.getStatusCode().is2xxSuccessful())
            photos = responseEntity.getBody().getPhotos();
        storeImages(photos);
        return photos;
    }

    private List<String> getRequestDates() {
        Resource datesFile = resourceLoader.getResource("classpath:requestdates.txt");
        List<String> datesList = new ArrayList<>();

        try {
            InputStream is = datesFile.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            datesList = br.lines().collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        datesList.forEach(System.out::println);

        datesList.forEach(date -> {
            try {
                LocalDate.parse(date);
            } catch (DateTimeException e) {
                log.error("Invalid date in the text file " + e.getMessage());
            }
        });

        return datesList;
    }

    public void storeImages(List<Photo> photoList) {
        String filepath = null;
        try {
            filepath = ResourceUtils.getFile("classpath:static/").getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String finalFilepath = filepath;
        photoList.forEach(photo -> {
            try {
                downloadImage(photo.getImg_src(),
                        new File(finalFilepath).getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void downloadImage(String sourceUrl, String targetDirectory)
            throws IOException {
        URL imageUrl = new URL(sourceUrl);
        String fileName = sourceUrl.substring(sourceUrl.lastIndexOf('/') + 1, sourceUrl.length());

        try (InputStream imageReader = new BufferedInputStream(
                imageUrl.openStream());
             OutputStream imageWriter = new BufferedOutputStream(
                     new FileOutputStream(targetDirectory + File.separator
                             + fileName));) {
            int readByte;
            while ((readByte = imageReader.read()) != -1) {
                imageWriter.write(readByte);
            }
        }
    }
}
