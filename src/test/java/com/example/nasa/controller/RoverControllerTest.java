package com.example.nasa.controller;

import com.example.nasa.model.Photo;
import com.example.nasa.service.RoverService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {RoverController.class})
public class RoverControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoverService roverService;

    @Test
    public void getMarsPhotos_thenReturnMockedPhotos() throws Exception {
        //Given
        List<Photo> photoList = Arrays.asList(
                new Photo(617458, "http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01622/opgs/edr/fcam/FLB_541484941EDR_F0611140FHAZ00341M_.JPG", "2017-02-27"),
                new Photo(617458, "http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01622/opgs/edr/fcam/FLB_541484941EDR_F0611140FHAZ00341M_.JPG", "2017-02-27")
        );

        when(
                roverService.getMarsPhotos("2018-04-03")
        ).thenReturn(photoList);

        mockMvc.perform(get("/mars/photos?earth_date=2018-04-03"))
                .andExpect(status().isOk());

    }

    @Test
    public void whenWrongRequest_ThenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/mars/photos"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenNoImagesforDate_ThenReturnNotFound() throws Exception {
        //Given
        List<Photo> photoList = Arrays.asList();

        when(
                roverService.getMarsPhotos("2018-04-03")
        ).thenReturn(photoList);

        mockMvc.perform(get("/mars/photos?earth_date=2018-04-03"))
                .andExpect(status().isNotFound());
    }
}