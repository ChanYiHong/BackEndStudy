package HCY.SpringSecurityStudy.controller;

import HCY.SpringSecurityStudy.dto.NoteDTO;
import HCY.SpringSecurityStudy.service.NoteService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/notes/")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping(value = "")
    public ResponseEntity<Long> register(@RequestBody NoteDTO noteDTO) {

        log.info("Register Controller");
        Long id = noteService.register(noteDTO);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NoteDTO> read(@PathVariable("id") Long id) {

        NoteDTO noteDTO = noteService.get(id);
        return new ResponseEntity<>(noteDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Result<NoteDTO>> getList(@RequestParam("email") String email) {

        log.info("---------getList---------");
        log.info(email);

        List<NoteDTO> result = noteService.getAllWithWriter(email);
        return new ResponseEntity<>(new Result<>(result, result.size()), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> remove(@PathVariable("id") Long id) {

        log.info("Delete controller");
        log.info(id);

        noteService.remove(id);
        return new ResponseEntity<>("removed", HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> modify(@PathVariable("id") Long id, @RequestBody NoteDTO noteDTO) {

        log.info("Modify Controller");
        log.info(noteDTO);

        noteService.modify(noteDTO);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @Data
    @AllArgsConstructor
    class Result<T> {
        private List<T> data;
        private int totalCount;
    }
}
