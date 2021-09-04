package com.inflearn.demoinflearnrestapi.events;

import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
}