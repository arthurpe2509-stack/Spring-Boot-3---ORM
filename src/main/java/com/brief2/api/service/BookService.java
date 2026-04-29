package com.brief2.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brief2.api.jpalibrary.entity.Book;
import com.brief2.api.repository.BookRepository;

@Service
public class BookService {
    @Autowired
    private BookRepository repository;

    public List<Book> findAll() {
        return repository.findAll();
    }

    public Optional<Book> findById(Long id) {
        return repository.findById(id);
    }

    public Book save(Book book) {
        return repository.save(book);
    }

    public Optional<Book> findByTitle(String title) {
        return repository.findByTitle(title);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}