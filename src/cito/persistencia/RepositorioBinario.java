package cito.persistencia;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cito.modelo.BaseDados;

public class RepositorioBinario {

    public void salvar(BaseDados base, String caminho) throws IOException {
        ObjectOutputStream saida = new ObjectOutputStream(new FileOutputStream(caminho));
        saida.writeObject(base);
        saida.close();
    }

    public BaseDados restaurar(String caminho) throws IOException, ClassNotFoundException {
        ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(caminho));
        BaseDados base = (BaseDados) entrada.readObject();
        entrada.close();
        return base;
    }
}
