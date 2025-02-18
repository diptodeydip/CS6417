package com.example.OnlineMarketPlace.database;

import com.example.OnlineMarketPlace.model.AppUser;
import com.example.OnlineMarketPlace.model.Feedback;
import com.example.OnlineMarketPlace.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findAll ();

}

