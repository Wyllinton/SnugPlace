package com.snugplace.demo.Controllers;

import com.snugplace.demo.DTO.Comment.AnswerCommentDTO;
import com.snugplace.demo.DTO.Comment.CreateCommentDTO;
import com.snugplace.demo.DTO.ResponseDTO;
import com.snugplace.demo.Service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ResponseDTO<String>> createComment(@Valid @RequestBody CreateCommentDTO createCommentDTO) throws Exception{
        commentService.createComment(createCommentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(false, "Comentario creado exitosamente"));
    }

    @PostMapping("/answer")
    public ResponseEntity<ResponseDTO<String>> answerCommentHost(@Valid @RequestBody AnswerCommentDTO answerCommentDTO) throws Exception{
        commentService.answerCommentHost(answerCommentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(false, "Comentario respondido exitosamente"));
    }
}
