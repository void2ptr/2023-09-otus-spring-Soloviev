package ru.otus.hw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.model.BatchLink;

import java.util.List;
import java.util.Optional;

public interface BatchLinkRepository extends JpaRepository<BatchLink, Long> {

    List<BatchLink> findAll();

    @Query("SELECT b FROM BatchLink b WHERE b.className = :className AND b.imported = false")
    List<BatchLink> findNotImportedByClassName(String className);

    @Query("SELECT b FROM BatchLink b WHERE b.className = :className AND b.imported = true")
    List<BatchLink> findImportedByClassName(String className);

    @Query("SELECT b FROM BatchLink b WHERE b.className = :className AND b.importLink = :importLink")
    Optional<BatchLink> findByImportLink(@Param("className") String className, @Param("importLink") String id);

}
