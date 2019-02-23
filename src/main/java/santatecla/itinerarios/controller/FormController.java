package santatecla.itinerarios.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import santatecla.itinerarios.model.Form;
import santatecla.itinerarios.repo.FormRepository;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.IOException;

//@RestController
@Controller
@RequestMapping("forms")
public class FormController {
    private FormRepository repository;

    public FormController(FormRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Form findById(@PathVariable Long id) {
        return this.repository.findById(id).orElseThrow(() -> new EntityNotFoundException(Form.class.getName() + " not found with id " + id));
    }

    @PostMapping
    public Form addForm(@Valid @ModelAttribute Form form, @RequestParam("upload_image") MultipartFile file) throws IOException {
        if (file != null) {
            form.setImage(file.getBytes());
        }
        return this.repository.save(form);
    }

    @DeleteMapping("/{id}")
    public void deleteForm(@PathVariable Long id) {
        this.repository.deleteById(id);
    }
}
