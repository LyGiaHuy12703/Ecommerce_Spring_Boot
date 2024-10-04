package com.minhbui.ecommerce.service;

import com.minhbui.ecommerce.dto.request.EventCreationRequest;
import com.minhbui.ecommerce.dto.request.EventUpdateRequest;
import com.minhbui.ecommerce.dto.response.EventResponse;
import com.minhbui.ecommerce.exception.AppCatchException;
import com.minhbui.ecommerce.exception.AppException;
import com.minhbui.ecommerce.exception.ErrorCode;
import com.minhbui.ecommerce.mapper.EventMapper;
import com.minhbui.ecommerce.model.Event;
import com.minhbui.ecommerce.model.Shop;
import com.minhbui.ecommerce.model.User;
import com.minhbui.ecommerce.repository.EventRepository;
import com.minhbui.ecommerce.repository.ShopRepository;
import com.minhbui.ecommerce.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventService {
     EventRepository eventRepository;
     EventMapper eventMapper;
     ShopRepository shopRepository;
     UserRepository userRepository;

    @PreAuthorize("hasRole('SHOP')")
    public EventResponse createEvent(EventCreationRequest request) {
        //lấy email người dùng
        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();

        User userId = userRepository.findByEmail(emailOwner).orElse(null);

        if (userId == null) {
            throw new AppCatchException(ErrorCode.USER_NOT_FOUND);
        }

        Shop shop = shopRepository.findByOwner(userId).orElse(null);
        if (shop == null) {
            throw new AppCatchException(ErrorCode.SHOP_NOT_FOUND);
        }

        Event event = eventMapper.toEvent(request);
        event.setShop(shop);
        event.setDisabled(false);

        EventResponse eventResponse = eventMapper.toEventResponse(eventRepository.save(event));

        eventResponse.setDisabled(false);
        return eventResponse;
    }
    //method for shop
    @PreAuthorize("hasRole('SHOP')")
    public List<EventResponse> getAll() {
        //lấy email người dùng
        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();

        User owner = userRepository.findByEmail(emailOwner).orElse(null);

        if (owner == null) {
            throw new AppCatchException(ErrorCode.USER_NOT_FOUND);
        }

        Shop shop = shopRepository.findByOwner(owner).orElse(null);
        if (shop == null) {
            throw new AppCatchException(ErrorCode.SHOP_NOT_FOUND);
        }

        return eventMapper.toEventResponseList(shop.getEvents());
    }

    //lấy tất cả event của shop còn hiệu lực method for user
    @PreAuthorize("hasRole('USER')")
    public List<EventResponse> getEventsByShop(Long shopId) {
        Shop shop = shopRepository.findById(shopId).orElse(null);
        if (shop == null) {
            throw new AppCatchException(ErrorCode.SHOP_NOT_FOUND);
        }
        return eventMapper.toEventResponseList(eventRepository.findAllNotDisabled(shop));
    }
    public EventResponse getById(Long id) {

        Event event = eventRepository.findById(id).orElse(null);

        return eventMapper.toEventResponse(event);
    }

    @PreAuthorize("hasRole('SHOP')")
    public EventResponse updateEvent(Long id, EventUpdateRequest request) {
        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = userRepository.findByEmail(emailOwner).orElse(null);
        if (owner == null) {
            throw new AppCatchException(ErrorCode.USER_NOT_FOUND);
        }

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new AppCatchException(ErrorCode.EVENT_NOT_FOUND));

        if(!event.getShop().getOwner().getEmail().equals(emailOwner)) {
            throw new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER);
        }

        eventMapper.updateEvent(request, event);

        return eventMapper.toEventResponse(eventRepository.save(event));
    }

    @PreAuthorize("hasRole('SHOP')")
    public void deleteById(Long id) {
        String emailOwner = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = userRepository.findByEmail(emailOwner).orElse(null);
        if (owner == null) {
            throw new AppCatchException(ErrorCode.USER_NOT_FOUND);
        }

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new AppCatchException(ErrorCode.EVENT_NOT_FOUND));

        if(!event.getShop().getOwner().getEmail().equals(emailOwner)) {
            throw new AppCatchException(ErrorCode.YOU_ARE_NOT_OWNER);
        }

        eventRepository.deleteById(id);
    }
}
