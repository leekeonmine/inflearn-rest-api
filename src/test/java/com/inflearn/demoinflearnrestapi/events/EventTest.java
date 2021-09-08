package com.inflearn.demoinflearnrestapi.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(JUnitParamsRunner.class)
public class EventTest {

    @Test
    public void builder(){
        Event event = Event.builder()
                .name("inflearn test")
                .description("rest api development with spring")
                .build();
        assertThat(event).isNotNull();

    }

    @Test
    public void javaBean(){
        Event event = new Event();

        String name = "Event";
        String description = "developtment";
        event.setName(name);
        event.setDescription(description);

        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }

    @Test
    @Parameters(method = "paramsForTestFree")
    public void testFree(int basePrice, int maxPrice, boolean isFree){
        //Given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();
        //When
        event.update();
        //Tjem
        assertThat(event.isFree()).isEqualTo(isFree);

    }

    private Object[] paramsForTestFree(){
        return new Object[] {
                new Object[]{0,0,true},
                new Object[]{100,0,false},
                new Object[]{0,100,false},
                new Object[]{100,200,false}
        };
    }
    @Test
    @Parameters(method = "parametersForTestOffline")
    public void testoffline(String location, boolean isOffline){
        Event event = Event.builder()
                .location(location)
                .build();
        //When
        event.update();
        //Tjem
        assertThat(event.isOffline()).isEqualTo(isOffline);

    }

    private Object[] parametersForTestOffline(){
        return new Object[]  {
                new Object[] {"강남", true},
                new Object[] {null, false},
                new Object[] {"     ",false},
        };
    }
}