# nasa

Application built with Spring Boot framework, and used the RestTemplate to consume Mars Rover Photo API from the [NASA API](https://api.nasa.gov/).
To keep the number of images to limited the query (https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?camera=FHAZ) filters the 
photos from camera FHAZ taken by Curiousity.



Basic Features

- Two GET enpoints as below

  - To get photos for a specific date (/photos?earth_data=yyyy-MM-dd)
  
      http://localhost:8080/mars/photos?earth_date=2018-04-03
      
  - To get photos for list of dates mentioned in "requesteddates.txt" under resources section of the app source. (/allphotos)
  
      http://localhost:8080/mars/allphotos

- Stores the images queried from the endpoints into /build/resources/main/static and can be viewed from browser as below as an example.

      http://localhost:8080/FLB_576015655EDR_F0691072FHAZ00479M_.JPG
      
- Docker image for the the application is build using gradle command as below, and running docker image it can be accessed over 8080 port for the endpoints
      
      gradle docker
      
      
