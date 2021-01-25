package HCY.SpringSecurityStudy.service;

import HCY.SpringSecurityStudy.dto.NoteDTO;
import HCY.SpringSecurityStudy.entity.ClubMember;
import HCY.SpringSecurityStudy.entity.Note;

import java.util.List;

public interface NoteService {

    Long register(NoteDTO noteDTO);

    NoteDTO get(Long id);

    void modify(NoteDTO noteDTO);

    void remove(Long id);

    List<NoteDTO> getAllWithWriter(String writerEmail);

    default Note dtoToEntity(NoteDTO noteDTO) {
        return Note.builder()
                .id(noteDTO.getId())
                .title(noteDTO.getTitle())
                .content(noteDTO.getContent())
                .writer(ClubMember.builder().email(noteDTO.getWriterEmail()).build())
                .build();
    }

    default NoteDTO entityToDTO(Note note) {
        return NoteDTO.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .createdDate(note.getCreatedDate())
                .modifiedDate(note.getModifiedDate())
                .writerEmail(note.getWriter().getEmail())
                .build();
    }

}
