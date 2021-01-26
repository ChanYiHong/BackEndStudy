package HCY.SpringSecurityStudy.service;

import HCY.SpringSecurityStudy.dto.NoteDTO;
import HCY.SpringSecurityStudy.entity.Note;
import HCY.SpringSecurityStudy.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoteServiceImpl implements NoteService{

    private final NoteRepository noteRepository;

    @Override
    @Transactional
    public Long register(NoteDTO noteDTO) {
        Note note = dtoToEntity(noteDTO);
        log.info("Register : " + note);
        noteRepository.save(note);
        return note.getId();
    }

    @Override
    public NoteDTO get(Long id) {
        log.info("Find One Note : " + id);
        Optional<Note> result = noteRepository.getNoteWithWriter(id);
        return result.map(this::entityToDTO).orElse(null);
    }

    @Override
    @Transactional
    public void modify(NoteDTO noteDTO) {
        log.info("Modify Note ID : " + noteDTO.getId());
        Note note = noteRepository.getOne(noteDTO.getId());
        note.changeTitle(noteDTO.getTitle());
        note.changeContent(noteDTO.getContent());
    }

    @Override
    @Transactional
    public void remove(Long id) {
        log.info("Remove Note ID : " + id);
        noteRepository.deleteById(id);
    }

    @Override
    public List<NoteDTO> getAllWithWriter(String writerEmail) {
        log.info("Get all Note with email : " + writerEmail);
        List<Note> result = noteRepository.getList(writerEmail);
        return result.stream().map(note -> entityToDTO(note)).collect(Collectors.toList());
    }
}
