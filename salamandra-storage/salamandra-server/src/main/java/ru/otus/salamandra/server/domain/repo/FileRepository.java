package ru.otus.salamandra.server.domain.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.salamandra.server.domain.File;

@Repository
public interface FileRepository extends CrudRepository<File, Long> {
}
