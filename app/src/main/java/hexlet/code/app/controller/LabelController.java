package hexlet.code.app.controller;

import hexlet.code.app.dto.LabelCreateDTO;
import hexlet.code.app.dto.LabelDTO;
import hexlet.code.app.dto.LabelUpdateDTO;
import hexlet.code.app.exeption.ResourceNotFoundException;
import hexlet.code.app.mapper.LabelMapper;
import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
@AllArgsConstructor
@Data
public class LabelController {
    private LabelRepository repository;
    private LabelMapper mapper;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<LabelDTO>> index() {
        List<Label> labels = repository.findAll();
        List<LabelDTO> result = labels.stream()
                .map(mapper::map)
                .toList();
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(result.size()));
        ResponseEntity<List<LabelDTO>> response = new ResponseEntity<>(result, headers, HttpStatus.OK.value());
        return response;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public LabelDTO create(@Valid @RequestBody LabelCreateDTO labelData) {
        Label label = mapper.map(labelData);
        repository.save(label);
        return mapper.map(label);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelDTO show(@PathVariable Long id) {
        Label label = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
        return mapper.map(label);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelDTO update(@Valid @RequestBody LabelUpdateDTO labelData, @PathVariable Long id) {
        Label label = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
        mapper.update(labelData, label);
        repository.save(label);
        LabelDTO labelDTO = mapper.map(label);
        return labelDTO;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Label label = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
        repository.deleteById(id);
    }
}
