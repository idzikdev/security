package pl.idzikdev.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.idzikdev.auth.models.MyUser;

@Repository
public interface UserRepository extends JpaRepository<MyUser, Integer> {
    @Query(value = "select user from MyUser user where user.username=?1")
    MyUser findMyUsersByName (String username);
}
