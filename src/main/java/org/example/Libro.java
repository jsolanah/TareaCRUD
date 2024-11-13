package org.example;

public class Libro {
    private String titulo;
    private String autor;
    private String genero;
    private Boolean prestado;

    public Libro(String titulo, String autor, String genero, Boolean prestado) {
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.prestado = prestado;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Boolean getPrestado() {
        return prestado;
    }

    public void setPrestado(Boolean prestado) {
        this.prestado = prestado;
    }

}
