package hu.progmasters.animalShelter.IT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CatServiceTest {
  
  @Mock
  CatRepository catRepository;
  
  @InjectMocks
  CatService catservice;
  
  private Cat cat1;
  private Cat cat2;
  private Dog dog1;
  private CatCommand command1;
  private CatCommand command2;
  
  @BeforeEach
  void init(){
    cat1 = new Cat(1, "Lucifer", 10, "Giant", Gender.TOM, LocalDateTime.now(), true, false, new BestFriend(1, null, null));
    cat2 = new Cat(2, "Ribizli", 5, "Halfear", Gender.PUSSY, LocalDateTime.now(), true, false, new BestFriend(2, null, null));
    dog1 = new Dog(1, "Sirion", 6, "Mudi", Gender.SIRE, LocalDateTime.now(), true, false, new BestFriend(1, null, null));
    command1 = new CatCommand("Lucifer", 10, "Giant", Gender.TOM, LocalDateTime.now(), true, false, new BestFriend(1, null, null));
    command2 = new CatCommand("Ribizli", 5, "Halfear", Gender.PUSSY, LocalDateTime.now(), true, false, new BestFriend(2, null, null));
   }
  
  @Test
  void testList_atStart_emptyList(){
    when(catRepository.findAll()).thenReturn(List.of());
    assertThat(catService.findAll().isEmpty);
  }
  
  @Test
  void testList_singleCatSaved_singleCatInList(){
    when(catRepository.save(cat1)).thenReturn(cat1);
    when(catRepository.findAll()).thenReturn(List.of(cat1));
    
    Cat catSaved = movieService.save(cat1);
    assertEquals(cat1, catSaved);
    assertThat(catService.findAll())
      .hasSize(1)
      .containsExactly(cat1);
    
    verify(catRepository, times(1)).save(any());
    verify(catRepository, times(1)).list();
    verifyNoMoreInteractions(catRepository);
   
 
