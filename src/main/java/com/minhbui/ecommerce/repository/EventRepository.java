package com.minhbui.ecommerce.repository;

import com.minhbui.ecommerce.model.Event;
import com.minhbui.ecommerce.model.Shop;
import com.minhbui.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByShop(Shop shop);

    //lấy tất cả event của shop còn hiệu lực
    @Query("select e from Event e join e.shop s where e.disabled=false and e.shop=?1")
    List<Event> findAllNotDisabled(Shop shop);

    //lấy tất cả event của shop còn hết hiệu lực
    @Query("select e from Event e join e.shop s where e.disabled=false ")
    List<Event> findAllDisabled(Shop shop);
}
