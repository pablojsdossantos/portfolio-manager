package pd.santos.portfoliomanager.user.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pd.santos.portfoliomanager.user.model.User;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("INSERT INTO \"portfolio-manager\".user (email, first_name, last_name) VALUES (:email, :firstName, :lastName) RETURNING id, email, first_name, last_name")
    User insert(@Param("email") String email, @Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("UPDATE \"portfolio-manager\".user SET first_name = :firstName, last_name = :lastName WHERE id = :id RETURNING id, email, first_name, last_name")
    User update(@Param("id") Long id, @Param("firstName") String firstName, @Param("lastName") String lastName);

    @Query("SELECT id, email, first_name as firstName, last_name as lastName FROM \"portfolio-manager\".user")
    List<User> findAll();

    @Query("SELECT id, email, first_name as firstName, last_name as lastName FROM \"portfolio-manager\".user WHERE id = :id")
    User findUserById(@Param("id") Long id);

    @Query("DELETE FROM \"portfolio-manager\".user WHERE id = :id")
    void deleteById(@Param("id") Long id);
}
