package com.travelbnb.repository;

import com.travelbnb.entity.AppUser;
import com.travelbnb.entity.Reviews;
import org.hibernate.mapping.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewsRepository extends JpaRepository<Reviews, Long> {
    @Query("Select r from Reviews r where r.appUser=:user and r.property=:property")
    Reviews findReviewByUser(@Param("user") AppUser user,@Param("property") Property property);

    @Query("Select r from Reviews r where r.appUser=:user")
    List<Reviews> findByUserReviews(@Param("user") AppUser user);

    //boolean findReviewByUser(AppUser user, com.travelbnb.entity.Property property);
}