package com.example.OnlineMarketPlace.database;

import com.example.OnlineMarketPlace.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {

    public AppUser findByEmail(String email);

}
