package com.deniapolytech.FactorySystemWeb.repository;

import com.deniapolytech.FactorySystemWeb.model.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ContactsRepository extends JpaRepository<Contact, Integer> {

    @Query("SELECT c FROM Contact c WHERE c.firstUser.id = :userId OR c.secondUser.id = :userId")
    List<Contact> findAllContactsByUserId(@Param("userId") int userId);

    default Boolean existsContactBetweenUsers(int user1Id, int user2Id) {
        return existsByFirstUserIdAndSecondUserId(user1Id, user2Id) ||
                existsByFirstUserIdAndSecondUserId(user2Id, user1Id);
    }

    boolean existsByFirstUserIdAndSecondUserId(int firstUserId, int secondUserId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Contact c WHERE (c.firstUser.id = :userId1 AND c.secondUser.id = :userId2) OR (c.firstUser.id = :userId2 AND c.secondUser.id = :userId1)")
    void deleteContactBetweenUsers(@Param("userId1") int userId1, @Param("userId2") int userId2);
}
