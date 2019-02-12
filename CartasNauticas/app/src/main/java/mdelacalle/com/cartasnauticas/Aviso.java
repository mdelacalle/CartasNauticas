package mdelacalle.com.cartasnauticas;

import io.realm.RealmObject;

public class Aviso extends RealmObject {
    String inicio;
    String fin;
    String texto;

    public Aviso(String inicio, String fin, String texto) {
        this.inicio = inicio;
        this.fin = fin;
        this.texto = texto;
    }

    public Aviso() {
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return "Aviso{" +
                "inicio='" + inicio + '\'' +
                ", fin='" + fin + '\'' +
                ", texto='" + texto + '\'' +
                '}';
    }
}
