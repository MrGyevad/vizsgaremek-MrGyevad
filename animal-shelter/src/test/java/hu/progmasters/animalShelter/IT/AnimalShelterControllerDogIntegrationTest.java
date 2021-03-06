package hu.progmasters.animalShelter.IT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import hu.progmasters.animalShelter.domain.Gender;
import hu.progmasters.animalShelter.dto.*;
import hu.progmasters.animalShelter.repository.AnimalShelterRepository;
import hu.progmasters.animalShelter.repository.BestFriendRepository;
import hu.progmasters.animalShelter.repository.CatRepository;
import hu.progmasters.animalShelter.repository.DogRepository;
import hu.progmasters.animalShelter.service.AnimalShelterService;
import hu.progmasters.animalShelter.service.CatService;
import hu.progmasters.animalShelter.service.DogService;
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
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AnimalShelterControllerDogIntegrationTest {

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

    @Autowired
    DogService dogService;

    @Autowired
    CatService catService;

    @Autowired
    AnimalShelterService animalShelterService;

    @Autowired
    AnimalShelterRepository animalShelterRepository;

    private static AnimalShelterCommand animalShelterCommand;
    private static AnimalShelterInfo animalShelterInfo;
    private static DogCommand dogCommand1;
    private static DogInfo dogInfo1;
    private static CatCommand catCommand1;
    private static CatInfo catInfo1;
    private static DogCommand dogCommand2;
    private static DogInfo dogInfo2;
    private static DogCommand updateDogCommand1;
    private static DogInfo updatedDogInfo1;

    @BeforeEach
    void init(){

        String ldt = "2021-08-24 17:40:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(ldt, formatter);
        animalShelterCommand = new AnimalShelterCommand("HopeForPaws");
        animalShelterInfo = new AnimalShelterInfo(1, "HopeForPaws", null, null);
        dogCommand1 = new DogCommand("Sirion", 6, "Mudi", Gender.SIRE, dateTime,
                true, false, 1);
        dogInfo1 = new DogInfo(1, "Sirion", 6, "Mudi", Gender.SIRE, dateTime,
                true, false, null);
        dogCommand2 = new DogCommand("R??ka", 15, "Vizsla", Gender.BITCH, dateTime,
                true, false, 1);
        dogInfo2 = new DogInfo(2, "R??ka", 15, "Vizsla", Gender.BITCH, dateTime,
                true, false, null);
        catCommand1 = new CatCommand("Lucifer", 10, "Giant", Gender.TOM,
                dateTime, true, false, 1);
        catInfo1 = new CatInfo(1, "Lucifer", 10, "Giant", Gender.TOM,
                dateTime, true, false, null);
        updateDogCommand1 = new DogCommand("R??ka", 15, "Vizsla", Gender.BITCH, dateTime,
                true, false, 1);
        updatedDogInfo1 = new DogInfo(1, "R??ka", 15, "Vizsla", Gender.BITCH, dateTime,
                true, false, null);
    }

    @Test
    void testFindAllResidents_emptyLists() throws Exception{
        mockMvc.perform(get("/api"))
                .andExpect(status().isOk())
                .andExpect(content().json("[[],[]]"));
    }

    @Test
    void testSaveDog_successfulSaveAndDogOnTheList() throws Exception{
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

        mockMvc.perform(post("/api/dog")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dogCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(dogInfo1)));

        assertThat(dogService.findAllDogs()).hasSize(1).contains(dogInfo1);

        mockMvc.perform(get("/api/dog"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(dogInfo1))));

    }

    @Test
    void testFindAllResidents_thereIsACatAndADog() throws Exception{
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

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
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(List.of(dogInfo1), List.of(catInfo1)))));
    }

    @Test
    void testSaveDog_invalidCommand() throws Exception{
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

        dogCommand1.setGender(Gender.PUSSY);
        mockMvc.perform(post("/api/dog")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dogCommand1)))
                .andExpect(status().isBadRequest());

        assertThat(dogService.findAllDogs()).isEmpty();

        mockMvc.perform(get("/api/dog"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testFindDogById_successfulFind() throws Exception{
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

        mockMvc.perform(post("/api/dog")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dogCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(dogInfo1)));

        mockMvc.perform(get("/api/dog/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dogInfo1)));
      }

    @Test
    void testFindDogById_failedToFind_DogNotFoundException() throws Exception{
        mockMvc.perform(get("/api/dog/420"))
                .andExpect(status().isNotFound());

    }

    @Test
    void testUpdateDog_successfulUpdate() throws Exception{
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

        mockMvc.perform(post("/api/dog")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dogCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(dogInfo1)));

        mockMvc.perform(put("/api/dog/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(updateDogCommand1)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedDogInfo1)));

        assertThat(dogService.findAllDogs()).hasSize(1).containsExactly(updatedDogInfo1);
    }

    @Test
    void testUpdateDog_failedToUpdate_dogNotFound() throws Exception {
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

        mockMvc.perform(put("/api/dog/420")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(updateDogCommand1)))
                .andExpect(status().isNotFound());

        assertThat(dogService.findAllDogs()).isEmpty();
    }

    @Test
    void testUpdateDog_failedToUpdate_invalidCommand() throws Exception {
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

        mockMvc.perform(post("/api/dog")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dogCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(dogInfo1)));

        updateDogCommand1.setGender(Gender.PUSSY);
        mockMvc.perform(put("/api/dog/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(updateDogCommand1)))
                .andExpect(status().isBadRequest());

        assertThat(dogService.findAllDogs()).hasSize(1).containsExactly(dogInfo1);
    }

    @Test
    void testDogAdopted_successfulAdoption() throws Exception {
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

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

        mockMvc.perform(put("/api/1/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/dog/adopt/1"))
                .andExpect(status().isOk());

        dogInfo1.setAdopted(true);

        assertThat(dogService.findAllDogs()).isEmpty();
        assertThat(catService.findAllCats()).isEmpty();
    }

    @Test
    void testDogDeceased_successfulDelete() throws Exception{
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

        mockMvc.perform(post("/api/dog")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dogCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(dogInfo1)));

        assertThat(dogService.findAllDogs()).hasSize(1);

        mockMvc.perform(delete("/api/dog/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        assertThat(dogService.findAllDogs()).isEmpty();
    }

    @Test
    void testDogDeceased_dogNotFound() throws Exception{
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

        mockMvc.perform(post("/api/dog")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dogCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(dogInfo1)));

        mockMvc.perform(delete("/api/dog/420")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

        assertThat(dogService.findAllDogs()).hasSize(1);
    }

    @Test
    void testWhoNeedsToWalk_nooneNeedsToWalk() throws Exception{
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

        mockMvc.perform(post("/api/dog")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dogCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(dogInfo1)));

        mockMvc.perform(get("/api/dog/walk")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of())));
    }

    @Test
    void testWhoNeedsToWalk_twoInTheListOneNeedsAWalk() throws Exception{
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

        mockMvc.perform(post("/api/dog")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dogCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(dogInfo1)));

        String ldt = "2021-08-13 15:40:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(ldt, formatter);
        dogCommand2.setLastWalk(dateTime);
        dogInfo2.setLastWalk(dateTime);

        mockMvc.perform(post("/api/dog")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dogCommand2)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(dogInfo2)));

        mockMvc.perform(get("/api/dog/walk")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(dogInfo2))));

        assertThat(dogService.findAllDogs()).hasSize(2).containsExactly(dogInfo1, dogInfo2);
        assertThat(dogService.whoNeedsAWalk()).hasSize(1).containsExactly(dogInfo2);
    }

    @Test
    void testWalkWithMe_dogWalked() throws Exception{
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

        String ldt = "2021-08-13 15:40:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(ldt, formatter);
        dogCommand1.setLastWalk(dateTime);
        dogInfo1.setLastWalk(dateTime);
        mockMvc.perform(post("/api/dog")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dogCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(dogInfo1)));

        assertThat(dogService.whoNeedsAWalk()).hasSize(1).containsExactly(dogInfo1);

        mockMvc.perform(put("/api/dog/walk/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        assertThat(dogService.whoNeedsAWalk()).isEmpty();
    }

    @Test
    void testWalkWithMe_dogNotFound() throws Exception{
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

        mockMvc.perform(post("/api/dog")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dogCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(dogInfo1)));

        mockMvc.perform(put("/api/dog/walk/420")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindAllDogsByGender_emptyList() throws Exception{
        mockMvc.perform(get("/api/dog/gender/SIRE"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of())));
    }

    @Test
    void testFindAllDogsByGender_oneBitchOneSire() throws Exception{
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

        mockMvc.perform(post("/api/dog")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dogCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(dogInfo1)));

        mockMvc.perform(post("/api/dog")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dogCommand2)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(dogInfo2)));

        mockMvc.perform(get("/api/dog/gender/SIRE"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(dogInfo1))));

        mockMvc.perform(get("/api/dog/gender/BITCH"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(dogInfo2))));

        assertThat(dogService.findAllByGender(Gender.SIRE)).hasSize(1).containsExactly(dogInfo1);
        assertThat(dogService.findAllByGender(Gender.BITCH)).hasSize(1).containsExactly(dogInfo2);
    }

    @Test
    void testWalkWithAllDogs_successfulWalk() throws Exception{
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

        String ldt = "2021-08-22 15:40:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(ldt, formatter);
        dogCommand1.setLastWalk(dateTime);
        dogInfo1.setLastWalk(dateTime);
        mockMvc.perform(post("/api/dog")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dogCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(dogInfo1)));

        dogCommand2.setLastWalk(dateTime);
        dogInfo2.setLastWalk(dateTime);
        mockMvc.perform(post("/api/dog")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(dogCommand2)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(dogInfo2)));

        assertThat(dogService.whoNeedsAWalk()).hasSize(2).containsExactlyInAnyOrder(dogInfo1, dogInfo2);

        mockMvc.perform(put("/api/dog/walk"))
                .andExpect(status().isOk());

        assertThat(dogService.whoNeedsAWalk()).isEmpty();
    }

    @Test
    void testFindDogsBestFriend_successfulFind() throws Exception{
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

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

        mockMvc.perform(put("/api/1/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/dog/bestFriend/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(catInfo1)));
    }
}
