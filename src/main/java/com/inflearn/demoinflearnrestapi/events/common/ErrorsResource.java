package com.inflearn.demoinflearnrestapi.events.common;

import com.inflearn.demoinflearnrestapi.events.Event;
import com.inflearn.demoinflearnrestapi.index.IndexController;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
public class ErrorsResource extends Resource<Event> {
    public ErrorsResource(Event event, Link... links) {
        super(event, links);
        add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
    }
}