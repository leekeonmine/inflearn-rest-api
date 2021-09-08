package com.inflearn.demoinflearnrestapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors erros){
        if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() != 0 ){
            erros.rejectValue("basePrice","wrongValue","BasePrice is wrong. ");
            erros.rejectValue("maxPrice","wrongValue","MaxPrice is wrong. ");
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if(endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) ||
        endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
        endEventDateTime.isBefore(eventDto.getBeginEventDateTime())){
            erros.rejectValue("endEventDateTime","wrongValue");
        }
    }
}
