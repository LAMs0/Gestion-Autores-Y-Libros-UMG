package com.example.dao;

import com.example.model.Autor;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutorDAO {
    private final EntityManagerFactory emf;

    public AutorDAO() {
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "validate"); // Solo validar estructura
        this.emf = Persistence.createEntityManagerFactory("bibliotecaPU", properties);
    }

    public void agregarAutor(Autor autor) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(autor);
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

    public Autor buscarAutorPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Autor.class, id);
        } finally {
            em.close();
        }
    }

    public List<Autor> listarAutores() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT a FROM Autor a", Autor.class).getResultList();
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