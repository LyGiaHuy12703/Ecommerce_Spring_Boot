package com.minhbui.ecommerce.mapper;

import com.minhbui.ecommerce.dto.request.EventCreationRequest;
import com.minhbui.ecommerce.dto.request.EventUpdateRequest;
import com.minhbui.ecommerce.dto.response.EventResponse;
import com.minhbui.ecommerce.model.Event;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {
    Event toEvent(EventCreationRequest request);
    void updateEvent(EventUpdateRequest request, @MappingTarget Event event);
    EventResponse toEventResponse(Event event);
    List<EventResponse> toEventResponseList(List<Event> events);
}
