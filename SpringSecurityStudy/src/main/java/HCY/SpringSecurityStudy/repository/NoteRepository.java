package HCY.SpringSecurityStudy.repository;

import HCY.SpringSecurityStudy.entity.Note;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    @EntityGraph(attributePaths = "writer", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select n from Note n where n.id = :id")
    Optional<Note> getNoteWithWriter(@Param("id") Long id);

    @EntityGraph(attributePaths = "writer", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select n from Note n where n.writer.email = :email")
    List<Note> getList(@Param("email") String email);

}
