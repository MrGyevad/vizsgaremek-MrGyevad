package hu.progmasters.animalShelter;


import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AnimalShelterApp {

    public static void main(String[] args) {
        SpringApplication.run(AnimalShelterApp.class, args);
    }

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelmapper = new ModelMapper();
        modelmapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelmapper;
    }

}
