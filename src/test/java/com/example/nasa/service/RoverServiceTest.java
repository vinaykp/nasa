package com.example.nasa.service;

import com.example.nasa.model.Mars;
import com.example.nasa.model.Photo;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {RoverService.class})
public class RoverServiceTest {

    @MockBean
    NasaApi nasaApi;

    @SpyBean
    private RoverService roverService;

    @Test
    public void givenMarsPhotos_WhenNasaAPI_ThenReturnMockPhotos() {

        //Given
        List<Photo> photoList = Arrays.asList(
                new Photo(617458, "http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01622/opgs/edr/fcam/FLB_541484941EDR_F0611140FHAZ00341M_.JPG", "2017-02-27"),
                new Photo(617458, "http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01622/opgs/edr/fcam/FLB_541484941EDR_F0611140FHAZ00341M_.JPG", "2017-02-27")
        );
        Mars mars = new Mars();
        mars.setPhotos(photoList);

        //When
        when(nasaApi.callMarsPhotosAPI(any())).thenReturn(new ResponseEntity(mars, HttpStatus.OK));
        doNothing().when(roverService).storeImages(any());
        //Then
        List<Photo> actual = roverService.getMarsPhotos("2018-09-22");
        Assertions.assertThat(actual.size() == 2);
    }

    @Test
    public void givenNoMarsPhotos_WhenNasaAPI_ThenReturnNoMockPhotos() {

        //Given
        List<Photo> photoList = Arrays.asList();
        Mars mars = new Mars();
        mars.setPhotos(photoList);

        //When
        when(nasaApi.callMarsPhotosAPI(any())).thenReturn(new ResponseEntity(mars, HttpStatus.OK));
        doNothing().when(roverService).storeImages(any());
        //Then
        List<Photo> actual = roverService.getMarsPhotos("2018-09-22");
        Assertions.assertThat(actual.size() == 0);
    }
}