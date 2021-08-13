package hu.progmasters.animalShelter.IT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import hu.progmasters.animalShelter.domain.Dog;
import hu.progmasters.animalShelter.domain.Gender;
import hu.progmasters.animalShelter.dto.*;
import hu.progmasters.animalShelter.repository.BestFriendRepository;
import hu.progmasters.animalShelter.repository.CatRepository;
import hu.progmasters.animalShelter.repository.DogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AnimalShelterControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    private final ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();

    @Autowired
    DogRepository dogRepository;

    @Autowired
    CatRepository catRepository;

    @Autowired
    BestFriendRepository bestFriendRepository;

    @Autowired
    ModelMapper modelMapper;

    private static DogCommand dogCommand1;
    private static DogInfo dogInfo1;
    private static CatCommand catCommand1;
    private static CatInfo catInfo1;
    private static BestFriendInfo bestFriendInfo1;
    private static DogCommand dogCommand2;
    private static DogInfo dogInfo2;
    private static CatCommand catCommand2;
    private static CatInfo catInfo2;
    private static BestFriendInfo bestFriendInfo2;
    private static DogCommand updateDogCommand1;
    private static CatCommand updateCatCommand1;
    
    @BeforeEach
    void init(){
        ModelMapper modelMapper = new ModelMapper();
        dogCommand1 = new DogCommand("Sirion", 6, "Mudi", Gender.SIRE, LocalDateTime.now(),
                true, false);
        dogInfo1 = new DogInfo(1, "Sirion", 6, "Mudi", Gender.SIRE, LocalDateTime.now(),
                true, false);
        dogCommand2 = new DogCommand("Diego", 11, "Maltese", Gender.SIRE, LocalDateTime.now(),
                true, false);
        dogInfo2 = new DogInfo(2, "Diego", 11, "Maltese", Gender.SIRE, LocalDateTime.now(),
                true, false);
        catCommand1 = new CatCommand("Lucifer", 10, "Giant", Gender.TOM,
                LocalDateTime.now(), true, false);
        catInfo1 = new CatInfo(1, "Lucifer", 10, "Giant", Gender.TOM,
                LocalDateTime.now(), true, false);
        catCommand2 = new CatCommand("Ribizli", 5, "Halfear", Gender.PUSSY,
                LocalDateTime.now(), true, false);
        catInfo2 = new CatInfo(2, "Ribizli", 5, "Halfear", Gender.PUSSY,
                LocalDateTime.now(), true, false);
        bestFriendInfo1 = new BestFriendInfo(1, catInfo1, dogInfo1);
        bestFriendInfo2 = new BestFriendInfo(2, catInfo2, dogInfo2);
        updateDogCommand1 = new DogCommand("Réka", 15, "Vizsla", Gender.BITCH, LocalDateTime.now(),
                true, false);
        updateCatCommand1 = new CatCommand("Retek", 4, "Ginger", Gender.TOM,
                LocalDateTime.now(), true, false);
    }

    @Test
    void testFindAllResidents_emptyList() throws Exception{
        mockMvc.perform(get("/api"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testSaveDog_successfulSaveAndDogOnTheList() throws Exception{
        mockMvc.perform(post("/api/dog")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dogCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(dogInfo1)));

        assertThat(dogRepository.findAll()).hasSize(1).containsExactly(modelMapper.map(dogInfo1, Dog.class));

        mockMvc.perform(get("/api/dog"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dogInfo1)));

    }

    @Test
    void testFindAllResidents_thereIsACatAndADog() throws Exception{
        mockMvc.perform(post("/api/dog")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dogCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(dogInfo1)));

        mockMvc.perform(post("/api/cat")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(catCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(catInfo1)));

        mockMvc.perform(get("/api"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(dogInfo1, catInfo1))));
    }

}
