package com.github.onlinebookstore.repositories.impl;

import com.github.onlinebookstore.model.Book;
import com.github.onlinebookstore.repositories.BookRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    @Override
    public Book save(Book book) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();

            return book;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            throw new DataAccessResourceFailureException("Failed to save Book " + book, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Book> findAll() {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query<Book> query = session.createQuery("from Book b", Book.class);
            List<Book> result = query.getResultList();
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            throw new DataAccessResourceFailureException("Failed to find all Books", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public Optional<Book> findBookById(Long id) {
        Session session = null;
        Transaction transaction = null;

        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Query<Book> query = session.createQuery("from Book b where b.id = :id", Book.class);
            query.setParameter("id", id);
            Book result = query.getSingleResult();
            transaction.commit();
            return Optional.of(result);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            throw new DataAccessResourceFailureException("Failed to find book with id " + id, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
