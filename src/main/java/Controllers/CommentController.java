package Controllers;

import DTO.AnswerCommentDTO;
import DTO.CreateCommentDTO;
import DTO.ResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @PostMapping
    public ResponseEntity<ResponseDTO<String>> createComment(@Valid @RequestBody CreateCommentDTO createCommentDTO) throws Exception{
        //Lógica

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(false, "\t\n" + "Comentario creado exitosamente"));
    }

    @PostMapping("/{id}/answer")
    public ResponseEntity<ResponseDTO<AnswerCommentDTO>> answerCommentHost(@PathVariable String id) throws Exception{
        //Lógica
        AnswerCommentDTO answer = new AnswerCommentDTO();

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(false, answer));
    }
}
