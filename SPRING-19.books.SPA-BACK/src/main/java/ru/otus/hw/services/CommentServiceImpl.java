package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CommentDto> findCommentByBookId(long bookId) {
        return commentRepository.findCommentsByBookId(bookId)
                .stream()
                .map(CommentMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public CommentDto insert(CommentDto commentDto) {
        return CommentMapper.toDto(this.save(commentDto.getBook().getId(), 0, commentDto.getDescription()));
    }

    @Transactional
    @Override
    public CommentDto update(CommentDto commentDto) {
        var commentOpt = commentRepository.findCommentById(commentDto.getId());
        if (commentOpt.isEmpty()) {
            throw new EntityNotFoundException("ERROR: comment '%d' not found".formatted(commentDto.getBook().getId()));
        }
        Comment comment = this.save(commentDto.getBook().getId(), commentDto.getId(), commentDto.getDescription());
        return CommentMapper.toDto(comment);
    }

    @Transactional
    @Override
    public CommentDto delete(long commentId) {
        Comment commentBefore = commentRepository.findCommentById(commentId).orElseThrow(() ->
                new EntityNotFoundException("ERROR: comment '%d' not found".formatted(commentId)));
        commentRepository.deleteById(commentId);
        Optional<Comment> commentAfter = commentRepository.findCommentById(commentId);
        if (commentAfter.isPresent()) {
            throw new EntityNotFoundException("ERROR: comment '%d' do not deleted".formatted(commentId));
        }
        return CommentMapper.toDto(commentBefore);
    }

    private Comment save(long bookId, long commentId, String description) {
        var book = bookRepository.findAllById(bookId).orElseThrow(() ->
                new EntityNotFoundException("ERROR: book '%d' not found".formatted(bookId)));
        return commentRepository.save(new Comment(commentId, description, book));
    }
}
