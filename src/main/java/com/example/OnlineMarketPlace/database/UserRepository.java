package com.example.OnlineMarketPlace.database;

import com.example.OnlineMarketPlace.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
