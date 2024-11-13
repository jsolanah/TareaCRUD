package org.example;

import java.io.IOException;
import java.sql.*;
import java.util.Date;
import java.util.LinkedList;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class PrimaryController {
    public TextField tituloTF;
    public TextField autorTF;
    public TextField generoTF;

    public Button agregarBT;
    public Button modificarBT;
    public Button eliminarBT;
    public TableView tableView;
    public Connection c;
    DatabaseMetaData databaseMetaData;
    public LinkedList<Libro> libros;
    private Libro libro;

    public PrimaryController() {
        this.libros = new LinkedList<Libro>();
    }
    public void openConnection() {
        String db = "biblioteca";
        String host = "localhost";
        String port = "3306";
        String urlConnection = "jdbc:mysql://" + host + ":" + port + "/" + db;
        String user = "root";
        String password = "";
        try {
            this.c = DriverManager.getConnection(urlConnection, user, password);
            System.out.println("Connectado");
            databaseMetaData = c.getMetaData();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        openConnection();
        TableColumn<Libro, String> colTitulo = new TableColumn<>("Titulo");
        TableColumn<Libro, String> colAutor = new TableColumn<>("Autor");
        TableColumn<Libro, String> colGenero = new TableColumn<>("Genero");
        TableColumn<Libro, String> colPrestado = new TableColumn<>("Prestado");
        tableView.getColumns().addAll(colTitulo, colAutor, colGenero, colPrestado);

        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colGenero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        colPrestado.setCellValueFactory(new PropertyValueFactory<>("prestado"));
        cargarDatosDeBD();
        tableView.setItems(FXCollections.observableList(libros));

        tableView.setItems(FXCollections.observableList(libros));

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mostrarDetallesLibro((Libro) newSelection);
            }
        });



    }

    public void cargarDatosDeBD(){
        PreparedStatement pst = null;
        try {
            pst = c.prepareStatement("Select * from Libros");
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                libros.add(new Libro(rs.getString(2), rs.getString(3), rs.getString(4), rs.getBoolean(5)));
            }

            pst.close();
            rs.close();

        }catch (Exception e){
            System.out.println();;
        }
    }

    @FXML
    private void agregarLibro() {
        PreparedStatement pst = null;

        try {
            c.setAutoCommit(false);

            String titulo = tituloTF.getText().toString();
            String autor = autorTF.getText().toString();
            String genero = generoTF.getText().toString();
            if (!titulo.isEmpty()&&!autor.isEmpty()&&!genero.isEmpty()){
                pst = c.prepareStatement("Insert into Libros (titulo, autor, genero) VALUES (?,?,?)");

                pst.setString(1, titulo);
                pst.setString(2, autor);
                pst.setString(3, genero);
                pst.executeUpdate();

                c.commit();
                pst.close();
            }

        } catch (Exception e) {
            try {
                c.rollback();
                System.out.println();
            } catch (SQLException ex) {
                System.out.println();

            }
        }
        actualizarTabla();
    }
    private void eliminarLibroDeLista(Libro libro) {
        libros.remove(libro);
        tableView.getItems().remove(libro);
    }
    @FXML
    private void eliminarLibro() {
        libro = (Libro) tableView.getSelectionModel().getSelectedItem();

        PreparedStatement pst = null;
        try {
            c.setAutoCommit(false);

            pst = c.prepareStatement("DELETE FROM Libros WHERE titulo = ?");
            pst.setString(1, libro.getTitulo());
            pst.executeUpdate();

            c.commit();
            pst.close();
            eliminarLibroDeLista(libro);
        } catch (Exception e) {
            try {
                c.rollback();
                e.printStackTrace();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        actualizarTabla();
    }
    @FXML
    private void modificarLibro() {
        libro = (Libro) tableView.getSelectionModel().getSelectedItem();
        String titulo = tituloTF.getText().toString();
        String autor = autorTF.getText().toString();
        String genero = generoTF.getText().toString();

        PreparedStatement pst = null;
        try {
            c.setAutoCommit(false);

            pst = c.prepareStatement("UPDATE Libros SET titulo = ?, autor = ?, genero = ? WHERE titulo = ?");
            pst.setString(1, titulo);
            pst.setString(2, autor);
            pst.setString(3, genero);
            pst.setString(4, libro.getTitulo());
            pst.executeUpdate();

            c.commit();
            pst.close();
        } catch (Exception e) {
            try {
                c.rollback();
                e.printStackTrace();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        actualizarTabla();
    }
    private void actualizarTabla() {
        tableView.getItems().clear();

        cargarDatosDeBD();
        tableView.setItems(FXCollections.observableList(libros));
        tituloTF.setText("");
        autorTF.setText("");
        generoTF.setText("");
        libro = null;
    }
    private void mostrarDetallesLibro(Libro libro) {
        tituloTF.setText(libro.getTitulo());
        autorTF.setText(libro.getAutor());
        generoTF.setText(libro.getGenero());
    }
}
