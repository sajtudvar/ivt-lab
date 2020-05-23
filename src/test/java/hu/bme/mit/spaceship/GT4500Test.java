package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore mockStore;
  private TorpedoStore mockStore2;

  @BeforeEach
  public void init(){
    mockStore = mock(TorpedoStore.class);
    mockStore2 = mock(TorpedoStore.class);
    this.ship = new GT4500(mockStore);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(mockStore.fire(1)).thenReturn(true);
    when(mockStore.isEmpty()).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockStore,times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    when(mockStore.fire(1)).thenReturn(true);
    when(mockStore.isEmpty()).thenReturn(false);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(mockStore,times(2)).fire(1);
  }

  @Test
  public void double_fire_single_torpedo(){
    when(mockStore.fire(1)).thenReturn(true);
    when(mockStore.isEmpty()).thenReturn(false);

    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    result = ship.fireTorpedo(FiringMode.SINGLE);

    verify(mockStore,times(2)).fire(1);

    assertEquals(true,result);

  }

  @Test
  public void fire_single_torpedo_without_ammo(){
    when(mockStore.fire(1)).thenReturn(true);
    when(mockStore.isEmpty()).thenReturn(true);
     
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    verify(mockStore,times(0)).fire(1);

    assertEquals(false,result);
  }

  @Test
  public void fire_single_torpedo_failure(){
    when(mockStore.fire(1)).thenReturn(false);
    when(mockStore.isEmpty()).thenReturn(false);

    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    verify(mockStore,times(1)).fire(1);

    assertEquals(false,result);
  }

  @Test
  public void fire_all_torpedo_both_fail(){
    when(mockStore.fire(1)).thenReturn(false);
    when(mockStore.isEmpty()).thenReturn(false);

    boolean result = ship.fireTorpedo(FiringMode.ALL);
    verify(mockStore,times(2)).fire(1);

    assertEquals(false,result);
  }

  @Test
  public void fire_all_torpedo_without_ammo(){
    when(mockStore.fire(1)).thenReturn(false);
    when(mockStore.isEmpty()).thenReturn(true);

    boolean result = ship.fireTorpedo(FiringMode.ALL);
    verify(mockStore,times(0)).fire(1);

    assertEquals(false,result);
  }

  @Test
  public void fire_single_torpedo_twice_fail_once(){
    when(mockStore.fire(1)).thenReturn(true);
    when(mockStore.isEmpty()).thenReturn(false);

    boolean result1 = ship.fireTorpedo(FiringMode.SINGLE);

    when(mockStore.fire(1)).thenReturn(false);

    boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);
    verify(mockStore,times(2)).fire(1);

    assertEquals(true,result1);
    assertEquals(false,result2);

  }
  @Test
  public void remaining_decision_branches(){
    ship = new GT4500(mockStore,mockStore2);
    when(mockStore.fire(1)).thenReturn(true);
    when(mockStore2.fire(1)).thenReturn(true);

    when(mockStore.isEmpty()).thenReturn(true);
    when(mockStore2.isEmpty()).thenReturn(false);

    boolean result = ship.fireTorpedo(FiringMode.SINGLE);  //elsütjük a secondaryt

    when(mockStore2.isEmpty()).thenReturn(true);
    result = ship.fireTorpedo(FiringMode.SINGLE);

    when(mockStore.isEmpty()).thenReturn(false);
    result = ship.fireTorpedo(FiringMode.SINGLE); //elsütjük a primaryt

    when(mockStore2.isEmpty()).thenReturn(true);
    when(mockStore.isEmpty()).thenReturn(true);
    result = ship.fireTorpedo(FiringMode.SINGLE); 

    when(mockStore.isEmpty()).thenReturn(false);
    result = ship.fireTorpedo(FiringMode.SINGLE);

    assertEquals(true, result);
  }


}
