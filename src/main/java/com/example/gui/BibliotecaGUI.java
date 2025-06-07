package com.example.gui;

import com.example.dao.AutorDAO;
import com.example.dao.LibroDAO;
import com.example.model.Autor;
import com.example.model.Libro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BibliotecaGUI extends JFrame {
    private final AutorDAO autorDAO;
    private final LibroDAO libroDAO;

    private JTextField txtTitulo;
    private JTextField txtAnio;
    private JTextField txtAutorNombre;
    private JTextField txtAutorNacionalidad;
    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;

    public BibliotecaGUI() {
        autorDAO = new AutorDAO();
        libroDAO = new LibroDAO();

        configurarVentana();
        initComponents();
    }

    private void configurarVentana() {
        setTitle("Sistema de Gestión de Biblioteca");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Registrar Libro", crearPanelRegistro());
        tabbedPane.addTab("Listado de Libros", crearPanelListado());

        add(tabbedPane);
    }

    private JPanel crearPanelRegistro() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título del Libro
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Título del Libro:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        txtTitulo = new JTextField(20);
        panel.add(txtTitulo, gbc);

        // Año de Publicación
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Año de Publicación:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        txtAnio = new JTextField(20);
        panel.add(txtAnio, gbc);

        // Nombre del Autor
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Nombre del Autor:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        txtAutorNombre = new JTextField(20);
        panel.add(txtAutorNombre, gbc);

        // Nacionalidad del Autor
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Nacionalidad del Autor:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        txtAutorNacionalidad = new JTextField(20);
        panel.add(txtAutorNacionalidad, gbc);

        // Botón Guardar
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(this::guardarLibro);
        panel.add(btnGuardar, gbc);

        return panel;
    }

    private JPanel crearPanelListado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Modelo de tabla
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Título");
        modeloTabla.addColumn("Año");
        modeloTabla.addColumn("Autor");
        modeloTabla.addColumn("Nacionalidad");

        // Tabla
        tablaLibros = new JTable(modeloTabla);
        tablaLibros.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane scrollPane = new JScrollPane(tablaLibros);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Botón Actualizar
        JButton btnActualizar = new JButton("Actualizar Listado");
        btnActualizar.addActionListener(e -> actualizarListadoLibros());
        panel.add(btnActualizar, BorderLayout.SOUTH);

        return panel;
    }

    private void guardarLibro(ActionEvent e) {
        try {
            String titulo = txtTitulo.getText().trim();
            String strAnio = txtAnio.getText().trim();
            String nombreAutor = txtAutorNombre.getText().trim();
            String nacionalidadAutor = txtAutorNacionalidad.getText().trim();

            // Validaciones
            if (titulo.isEmpty() || nombreAutor.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Título y nombre del autor son obligatorios",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Integer anio = strAnio.isEmpty() ? null : Integer.parseInt(strAnio);

            // Crear y guardar autor
            Autor autor = new Autor(nombreAutor, nacionalidadAutor.isEmpty() ? null : nacionalidadAutor);
            autorDAO.agregarAutor(autor);

            // Crear y guardar libro
            Libro libro = new Libro(titulo, anio, autor);
            libroDAO.agregarLibro(libro);

            JOptionPane.showMessageDialog(this, "Libro registrado exitosamente");
            limpiarCampos();
            actualizarListadoLibros();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "El año debe ser un número válido",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtTitulo.setText("");
        txtAnio.setText("");
        txtAutorNombre.setText("");
        txtAutorNacionalidad.setText("");
    }

    private void actualizarListadoLibros() {
        modeloTabla.setRowCount(0); // Limpiar tabla

        List<Libro> libros = libroDAO.listarLibrosConAutores();
        for (Libro libro : libros) {
            modeloTabla.addRow(new Object[]{
                    libro.getId(),
                    libro.getTitulo(),
                    libro.getAnio(),
                    libro.getAutor().getNombre(),
                    libro.getAutor().getNacionalidad()
            });
        }
    }

    public void cerrarConexiones() {
        autorDAO.close();
        libroDAO.close();
    }
}