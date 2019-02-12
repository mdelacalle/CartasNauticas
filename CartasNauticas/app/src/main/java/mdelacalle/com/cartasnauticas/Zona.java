package mdelacalle.com.cartasnauticas;

import io.realm.RealmObject;

public class Zona extends RealmObject {
    String texto;
    String nombre;

    public Zona(String texto, String nombre) {
        this.texto = texto;
        this.nombre = nombre;
    }

    public Zona() {
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Zona{" +
                "texto='" + texto + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
