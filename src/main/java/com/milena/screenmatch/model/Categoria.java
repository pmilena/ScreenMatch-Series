package com.milena.screenmatch.model;

public enum Categoria {
    ACAO ("Action", "ação"),
    ROMANCE ("Romance","romance"),
    DRAMA ("Drama","drama"),
    CRIME ("Crime","crime"),
    COMEDIA ("Comedy","comédia");

    private String categoriaOmdb;
    private String categoriaEmPortugues;

    Categoria(String categoriaOmdb, String categoriaEmPortugues){
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaEmPortugues = categoriaEmPortugues;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para o nome fornecido: " + text);
    }

    public static Categoria fromPortugues(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaEmPortugues.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para o nome fornecido: " + text);
    }

}
