package com.example.froylan.jaguares;

import java.util.List;

/**
 * Created by froylan on 23/11/16.
 */

public class NoticiasModel {
    private List<News> Noticia;

    public List<News> getNoticia() {
        return Noticia;
    }

    public void setNoticia(List<News> noticia) {
        Noticia = noticia;
    }

    public static class News{
        private String titulo;
        private String descripcion;
        private String date;
        private String url;

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDate() {
            return date;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setDate(String date) {
            this.date = date;
        }

        @Override
        public String toString() {
            return titulo+"\n"+date;
        }
    }
}
