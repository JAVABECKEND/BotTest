package com.example.bottest.repository;

import com.example.bottest.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<Users,Long> {

    Optional<Users> findByChatId(String chatId);

    @Query(value = "insert into users_storys values (users_id=?1,storys_id=?2)",nativeQuery = true)
    Optional<Long> saveUserIDAndStoryId(Long userid,Long storyid);

}
