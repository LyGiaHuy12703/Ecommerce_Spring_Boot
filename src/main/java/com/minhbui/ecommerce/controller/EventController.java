package com.minhbui.ecommerce.controller;

import com.minhbui.ecommerce.dto.request.ApiResponse;
import com.minhbui.ecommerce.dto.request.EventCreationRequest;
import com.minhbui.ecommerce.dto.request.EventUpdateRequest;
import com.minhbui.ecommerce.dto.response.EventResponse;
import com.minhbui.ecommerce.model.Event;
import com.minhbui.ecommerce.model.Shop;
import com.minhbui.ecommerce.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop")
public class EventController {
    @Autowired
    private EventService eventService;


    @PostMapping("/event")
    ApiResponse<EventResponse> createEvent(@RequestBody EventCreationRequest request) {
        ApiResponse<EventResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(eventService.createEvent(request));
        return apiResponse;
    }

    //lấy tất cả event của shop dành cho shop
    @GetMapping("/event")
    List<EventResponse> getEvents() {
        return eventService.getAll();
    }

    //lấy tất cả event của shop bawngf cách click vào shop
    @GetMapping("/events/{shopId}")
    List<EventResponse> getEventsByShop(@PathVariable("shopId") Long shopId) {
        return eventService.getEventsByShop(shopId);
    }

    @GetMapping("/event/{id}")
    EventResponse getEvent(@PathVariable("id") Long id) {
        return eventService.getById(id);
    }

    @PutMapping("/event/{id}")
    EventResponse updateEvent(@PathVariable("id") Long id, @RequestBody EventUpdateRequest request) {
        return eventService.updateEvent(id, request);
    }

    @DeleteMapping("/event/{id}")
    ApiResponse<String> deleteEvent(@PathVariable("id") Long id) {
        eventService.deleteById(id);
        return ApiResponse.<String>builder()
                .code(1000)
                .message("Event deleted")
                .build();
    }
}
