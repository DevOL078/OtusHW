package ru.otus.salamandra.server.domain.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.salamandra.server.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
