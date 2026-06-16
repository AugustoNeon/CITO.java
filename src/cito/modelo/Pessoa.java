package cito.modelo;

import java.io.Serializable;

public abstract class Pessoa implements Identificavel, Serializable {

    protected String nome;

    public Pessoa(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public abstract String papel();

    public String getResumo() {
        return papel() + ": " + nome + " (" + identificador() + ")";
    }

    @Override
    public String toString() {
        return getResumo();
    }
}
