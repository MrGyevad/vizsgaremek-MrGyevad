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
public class AnimalShelterControllerCatIntegrationTest {

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
    private static CatCommand catCommand2;
    private static CatInfo catInfo2;
    private static CatInfo updatedCatInfo1;
    private static CatCommand updateCatCommand1;

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
        catCommand1 = new CatCommand("Lucifer", 10, "Giant", Gender.TOM,
                dateTime, true, false, 1);
        catInfo1 = new CatInfo(1, "Lucifer", 10, "Giant", Gender.TOM,
                dateTime, true, false, null);
        catCommand2 = new CatCommand("Ribizli", 5, "Halfear", Gender.PUSSY,
                dateTime, true, false, 1);
        catInfo2 = new CatInfo(2, "Ribizli", 5, "Halfear", Gender.PUSSY,
                dateTime, true, false, null);
        updateCatCommand1 = new CatCommand("Retek", 4, "Ginger", Gender.TOM,
                dateTime, true, false, 1);
        updatedCatInfo1 = new CatInfo(1, "Retek", 4, "Ginger", Gender.TOM,
                dateTime, true, false, null);
    }

    @Test
    void testSaveCat_successfulSaveAndCatOnTheList() throws Exception{
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

        mockMvc.perform(post("/api/cat")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(catCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(catInfo1)));

        assertThat(catService.findAllCats()).hasSize(1).contains(catInfo1);

        mockMvc.perform(get("/api/cat")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is3xxRedirection())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(catInfo1))));
    }

    @Test
    void testSaveCat_invalidCommand() throws Exception{
        catCommand1.setGender(Gender.BITCH);
        mockMvc.perform(post("/api/cat")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(catCommand1)))
                .andExpect(status().isBadRequest());

        assertThat(catService.findAllCats()).isEmpty();

        mockMvc.perform(get("/api/cat")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is3xxRedirection())
                .andExpect(content().json("[]"));
    }

    @Test
    void testFindCatById_successfulFind() throws Exception{
        mockMvc.perform(post("/api/animalShelter")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(animalShelterCommand)))
            .andExpect(status().isCreated())
            .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

        mockMvc.perform(post("/api/cat")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(catCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(catInfo1)));

        mockMvc.perform(get("/api/cat/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is3xxRedirection())
                .andExpect(content().json(objectMapper.writeValueAsString(catInfo1)));
    }

    @Test
    void testFindCatById_failedToFind_CatNotFoundException() throws Exception{
        mockMvc.perform(get("/api/cat/420"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateCat_successfulUpdate() throws Exception{
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

        mockMvc.perform(post("/api/cat")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(catCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(catInfo1)));

        mockMvc.perform(put("/api/cat/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(updateCatCommand1)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedCatInfo1)));

        assertThat(catService.findAllCats()).hasSize(1).containsExactly(updatedCatInfo1);
    }

    @Test
    void testUpdateCat_failedToUpdate_catNotFound() throws Exception {
        mockMvc.perform(put("/api/cat/420")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(updateCatCommand1)))
                .andExpect(status().isNotFound());

        assertThat(catService.findAllCats()).isEmpty();
    }

    @Test
    void testUpdateCat_failedToUpdate_invalidCommand() throws Exception {
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

        mockMvc.perform(post("/api/cat")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(catCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(catInfo1)));

        updateCatCommand1.setGender(Gender.BITCH);
        mockMvc.perform(put("/api/cat/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(updateCatCommand1)))
                .andExpect(status().isBadRequest());

        assertThat(catService.findAllCats()).hasSize(1).containsExactly(catInfo1);
    }

    @Test
    void testCatAdopted_successfulAdoption() throws Exception {
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

        mockMvc.perform(put("/api/cat/adopt/1"))
                .andExpect(status().isOk());

        catInfo1.setAdopted(true);

        assertThat(catService.findAllAdoptedCats()).hasSize(1).containsExactly(catInfo1);
        assertThat(catService.findAllCats()).isEmpty();
        assertThat(dogService.findAllDogs()).isEmpty();
    }

    @Test
    void testCatDeceased_successfulDelete() throws Exception{
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

        mockMvc.perform(post("/api/cat")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(catCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(catInfo1)));

        assertThat(catService.findAllCats()).hasSize(1);

        mockMvc.perform(delete("/api/cat/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        assertThat(catService.findAllCats()).isEmpty();
    }

    @Test
    void testCatDeceased_catNotFound() throws Exception{
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

        mockMvc.perform(post("/api/cat")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(catCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(catInfo1)));

        mockMvc.perform(delete("/api/cat/420")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

        assertThat(catService.findAllCats()).hasSize(1);
    }

    @Test
    void testWhoNeedsToPlay_nooneNeedsToPlay() throws Exception{
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

        mockMvc.perform(post("/api/cat")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(catCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(catInfo1)));

        mockMvc.perform(get("/api/cat/play")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of())));
    }

    @Test
    void testWhoNeedsToPlay_twoInTheListOneNeedsToPlay() throws Exception{
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

        mockMvc.perform(post("/api/cat")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(catCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(catInfo1)));

        String ldt = "2021-08-13 15:40:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(ldt, formatter);
        catCommand2.setLastPlay(dateTime);
        catInfo2.setLastPlay(dateTime);

        mockMvc.perform(post("/api/cat")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(catCommand2)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(catInfo2)));

        mockMvc.perform(get("/api/cat/play")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(catInfo2))));

        assertThat(catService.findAllCats()).hasSize(2).containsExactly(catInfo1, catInfo2);
        assertThat(catService.whoNeedsToPlay()).hasSize(1).containsExactly(catInfo2);
    }

    @Test
    void testPlayWithMe_catPlayed() throws Exception{
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

        String ldt = "2021-08-13 15:40:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(ldt, formatter);
        catCommand1.setLastPlay(dateTime);
        catInfo1.setLastPlay(dateTime);
        mockMvc.perform(post("/api/cat")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(catCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(catInfo1)));

        assertThat(catService.whoNeedsToPlay()).hasSize(1).containsExactly(catInfo1);

        mockMvc.perform(put("/api/cat/play/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        assertThat(catService.whoNeedsToPlay()).isEmpty();
    }

    @Test
    void testPlayWithMe_catNotFound() throws Exception{
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

        mockMvc.perform(post("/api/cat")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(catCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(catInfo1)));

        mockMvc.perform(put("/api/cat/play/420")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindAllCatsByGender_emptyList() throws Exception{
        mockMvc.perform(get("/api/cat/gender/PUSSY"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of())));
    }

    @Test
    void testFindAllCatsByGender_onePussyOneTom() throws Exception{
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

        mockMvc.perform(post("/api/cat")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(catCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(catInfo1)));

        mockMvc.perform(post("/api/cat")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(catCommand2)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(catInfo2)));

        mockMvc.perform(get("/api/cat/gender/TOM"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(catInfo1))));

        mockMvc.perform(get("/api/cat/gender/PUSSY"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(catInfo2))));

        assertThat(catService.findAllByGender(Gender.TOM)).hasSize(1).containsExactly(catInfo1);
        assertThat(catService.findAllByGender(Gender.PUSSY)).hasSize(1).containsExactly(catInfo2);
    }

    @Test
    void testPlayWithAllCats_successfulPlay() throws Exception{
        mockMvc.perform(post("/api/animalShelter")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(animalShelterCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(animalShelterInfo)));

        String ldt = "2021-08-13 15:40:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(ldt, formatter);
        catCommand1.setLastPlay(dateTime);
        catInfo1.setLastPlay(dateTime);
        mockMvc.perform(post("/api/cat")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(catCommand1)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(catInfo1)));

        catCommand2.setLastPlay(dateTime);
        catInfo2.setLastPlay(dateTime);
        mockMvc.perform(post("/api/cat")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(catCommand2)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(catInfo2)));

        assertThat(catService.whoNeedsToPlay()).hasSize(2).containsExactlyInAnyOrder(catInfo1, catInfo2);

        mockMvc.perform(put("/api/cat/play"))
                .andExpect(status().isOk());

        assertThat(catService.whoNeedsToPlay()).isEmpty();
    }
}
