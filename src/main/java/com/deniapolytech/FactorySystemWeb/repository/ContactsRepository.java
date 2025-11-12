package com.deniapolytech.FactorySystemWeb.repository;

import com.deniapolytech.FactorySystemWeb.model.Contact;
import com.deniapolytech.FactorySystemWeb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactsRepository extends JpaRepository<Contact, Integer> {

    @Query("SELECT c FROM Contact c WHERE c.firstUserId = :userId OR c.secondUserId = :userId")
    List<Contact> findAllContactsByUserId(@Param("userId") int userId);

    default boolean existsContactBetweenUsers(int user1Id, int user2Id) {
        return existsByFirstUserIdAndSecondUserId(user1Id, user2Id) ||
                existsByFirstUserIdAndSecondUserId(user2Id, user1Id);
    }

    boolean existsByFirstUserIdAndSecondUserId(int firstUserId, int secondUserId);

}
