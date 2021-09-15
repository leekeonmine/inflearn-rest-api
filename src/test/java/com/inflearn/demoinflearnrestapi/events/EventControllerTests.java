package com.inflearn.demoinflearnrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inflearn.demoinflearnrestapi.common.RestDocsConfiguration;
import com.inflearn.demoinflearnrestapi.events.common.TestDescription;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.stream.IntStream;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EventRepository eventRepository;

    @Test
    @TestDescription("정상적으로 동작하는 코드")
    public void createEvent() throws Exception {

        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,23,14,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,24,14,21))
                .beginEventDateTime(LocalDateTime.of(2018,11,25,14,21))
                .endEventDateTime(LocalDateTime.of(2018,11,26,14,21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .build();

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event))
                        )
                        .andDo(print())
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("id").exists())
                        .andExpect(header().exists(HttpHeaders.LOCATION))
                        .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_UTF8_VALUE))
                        .andExpect(jsonPath("free").value(false))
                        .andExpect(jsonPath("offline").value(true))
                        .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                        .andExpect(jsonPath("_links.self").exists())
                        .andExpect(jsonPath("_links.query-events").exists())
                        .andExpect(jsonPath("_links.update-events").exists())
                        .andDo(document("create-event",
                                links(
                                        linkWithRel("self").description("link to self"),
                                        linkWithRel("query-events").description("link to query events"),
                                        linkWithRel("update-events").description("link to update an existing event")/*,
                                        linkWithRel("profile").description("link to update an existing event")*/
                                ),
                                requestHeaders(
                                        headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                                ),
                                requestFields(
                                        fieldWithPath("name").description("Name of new event"),
                                        fieldWithPath("description").description("description of new event"),
                                        fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                        fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                        fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                        fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                        fieldWithPath("location").description("location of new event"),
                                        fieldWithPath("basePrice").description("base price of new event"),
                                        fieldWithPath("maxPrice").description("max price of new event"),
                                        fieldWithPath("limitOfEnrollment").description("limit of enrolmment")
                                ),
                                responseHeaders(
                                        headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                        headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                                ),
                                relaxedResponseFields(
                                        fieldWithPath("id").description("identifier of new event"),
                                        fieldWithPath("name").description("Name of new event"),
                                        fieldWithPath("description").description("description of new event"),
                                        fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                        fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
                                        fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                        fieldWithPath("endEventDateTime").description("date time of end of new event"),
                                        fieldWithPath("location").description("location of new event"),
                                        fieldWithPath("basePrice").description("base price of new event"),
                                        fieldWithPath("maxPrice").description("max price of new event"),
                                        fieldWithPath("limitOfEnrollment").description("limit of enrolmment"),
                                        fieldWithPath("free").description("it tells if this event is free or not"),
                                        fieldWithPath("offline").description("it tells if this event is offline event or not"),
                                        fieldWithPath("eventStatus").description("event status"),
                                        fieldWithPath("_links.self.href").description("link to self"),
                                        fieldWithPath("_links.query-events.href").description("link to query event list"),
                                        fieldWithPath("_links.update-events.href").description("link to update existing event")/*,
                                        fieldWithPath("_links.profile.href").description("link to profile")*/

                                )
                        ));

    }


    @Test
    @TestDescription("BadRequest 코드 ")
    public void createEvent_BadRequest() throws Exception {

        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,23,14,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,24,14,21))
                .beginEventDateTime(LocalDateTime.of(2018,11,25,14,21))
                .endEventDateTime(LocalDateTime.of(2018,11,26,14,21))
                .basePrice(100)
                .maxPrice(200).limitOfEnrollment(100).location("강남역")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events/")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력값이 비어있는 코드")
    public void createEvent_Bad_Request_Empty_input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("이상한 값이 들어가는 코드")
    public void createEvent_Bad_Request_Wrong_input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,23,14,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,24,14,21))
                .beginEventDateTime(LocalDateTime.of(2018,11,25,14,21))
                .endEventDateTime(LocalDateTime.of(2018,11,22,14,21))
                .basePrice(100000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .build();
        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists());
    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    public void queryEvents() throws Exception{
        //Given
        IntStream.range(0,30).forEach(i ->{
            this.generateEvent(i);
        });
        //When
        this.mockMvc.perform(get("/api/events")
                        .param("page","1")
                        .param("size","10")
                        .param("sort","name,DESC")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"));
    }

    private void generateEvent(int index) {
        Event event = Event.builder()
                .name("event "+ index)
                .description("test event")
                .build();

        this.eventRepository.save(event);
    }
}