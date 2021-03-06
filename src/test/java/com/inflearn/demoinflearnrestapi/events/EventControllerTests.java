package com.inflearn.demoinflearnrestapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inflearn.demoinflearnrestapi.accounts.Account;
import com.inflearn.demoinflearnrestapi.accounts.AccountRepository;
import com.inflearn.demoinflearnrestapi.accounts.AccountRole;
import com.inflearn.demoinflearnrestapi.accounts.AccountService;
import com.inflearn.demoinflearnrestapi.common.BaseControllerTest;
import com.inflearn.demoinflearnrestapi.common.RestDocsConfiguration;
import com.inflearn.demoinflearnrestapi.events.common.AppProperties;
import com.inflearn.demoinflearnrestapi.events.common.TestDescription;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.ui.ModelMap;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.IntStream;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class EventControllerTests extends BaseControllerTest {



    @Autowired
    EventRepository eventRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AppProperties appProperties;
    @Before
    public void setUp(){
        this.eventRepository.deleteAll();
        this.accountRepository.deleteAll();
    }
    @Test
    @TestDescription("??????????????? ???????????? ??????")
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
                .location("?????????")
                .build();

        mockMvc.perform(post("/api/events/")
                        .header(HttpHeaders.AUTHORIZATION, getBearerString())
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

    private String getBearerString() throws Exception {
        return "Bearer "+getAccessToken();
    }

    private String getAccessToken() throws Exception {




        Account keesun = Account.builder()
                .email(appProperties.getUserUsername())
                .password(appProperties.getUserPassword())
                .roles(Set.of(AccountRole.ADMIN,AccountRole.USER))
                .build();

        this.accountService.saveAccount(keesun);
        ResultActions perform = this.mockMvc.perform(RestDocumentationRequestBuilders.post("/oauth/token")
                        .with(httpBasic(appProperties.getClientId(),appProperties.getClientSecret()))
                        .param("username",appProperties.getUserUsername())
                        .param("password",appProperties.getUserPassword())
                        .param("grant_type","password"))
                ;

        MockHttpServletResponse response =  perform.andReturn().getResponse();
        var responseBody = response.getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();
        return parser.parseMap(responseBody).get("access_token").toString();
    }


    @Test
    @TestDescription("BadRequest ?????? ")
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
                .maxPrice(200).limitOfEnrollment(100).location("?????????")
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
    @TestDescription("???????????? ???????????? ??????")
    public void createEvent_Bad_Request_Empty_input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("????????? ?????? ???????????? ??????")
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
                .location("?????????")
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
    @TestDescription("30?????? ???????????? 10?????? ????????? ????????? ????????????")
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


    @Test
    @TestDescription("????????? ???????????? ?????? ????????????")
    public void getEvent() throws Exception {
        //Given
        Event event = this.generateEvent(100);

        //When & Then
        this.mockMvc.perform(get("/api/events/{id}",event.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event"))
        ;
    }

    @Test
    @TestDescription("?????? ???????????? ???????????? ??? 404 ????????????")
    public void getEvent404() throws Exception {
        //Given
        Event event = this.generateEvent(100);

        //When & Then
        this.mockMvc.perform(get("/api/event/11883"))
                .andExpect(status().isNotFound())
        ;
    }


    private Event generateEvent(int index) {
        Event event = Event.builder()
                .name("event " + index)
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018,11,23,14,21))
                .closeEnrollmentDateTime(LocalDateTime.of(2018,11,24,14,21))
                .beginEventDateTime(LocalDateTime.of(2018,11,25,14,21))
                .endEventDateTime(LocalDateTime.of(2018,11,26,14,21))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("?????????")
                .free(false)
                .offline(true)
                .eventStatus(EventStatus.DRAFT)
                .build();

        return this.eventRepository.save(event);
    }

    @Test
    @TestDescription("???????????? ??????????????? ????????????")
    public void updateEvent() throws Exception {
        //given
        Event event = this.generateEvent(200);
        String eventName = "Update Event";
        EventDto eventDto = this.modelMapper.map(event,EventDto.class);
        eventDto.setName(eventName);

        //When & Then
        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                        .header(HttpHeaders.AUTHORIZATION, getBearerString())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(eventName))
                .andExpect(jsonPath("_links.self").exists())
                ;

    }

    @Test
    @TestDescription("???????????? ???????????? ????????? ????????? ?????? ??????")
    public void updateEvent400Empty() throws Exception {
        //given
        Event event = this.generateEvent(200);
        EventDto eventDto = new EventDto();

        //When & Then
        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                        .header(HttpHeaders.AUTHORIZATION, getBearerString())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

    }

    @Test
    @TestDescription("???????????? ????????? ????????? ????????? ?????? ??????")
    public void updateEvent400wrong() throws Exception {
        //given
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event,EventDto.class);
        eventDto.setBasePrice(20000);
        eventDto.setMaxPrice(1000);
        //When & Then
        this.mockMvc.perform(put("/api/events/{id}",event.getId())
                        .header(HttpHeaders.AUTHORIZATION, getBearerString())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;

    }

    @Test
    @TestDescription("???????????? ?????? ????????? ?????? ??????")
    public void updateEvent404() throws Exception {
        //given
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event,EventDto.class);

        //When & Then
        this.mockMvc.perform(put("/api/events/123123")
                        .header(HttpHeaders.AUTHORIZATION, getBearerString())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;

    }
}
