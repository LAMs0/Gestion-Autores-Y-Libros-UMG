package com.example.dao;

import com.example.model.Libro;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class LibroDAO {
    private final EntityManagerFactory emf;

    public LibroDAO() {
        this.emf = Persistence.createEntityManagerFactory("bibliotecaPU");
    }

    public void agregarLibro(Libro libro) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(libro);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Libro> listarLibrosConAutores() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT l FROM Libro l JOIN FETCH l.autor", Libro.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}