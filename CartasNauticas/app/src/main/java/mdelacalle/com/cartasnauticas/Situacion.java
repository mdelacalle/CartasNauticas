package mdelacalle.com.cartasnauticas;

import io.realm.RealmObject;

public class Situacion extends RealmObject {
    String analisis;
    String inicio;
    String fin;
    String texto;
    String nombre;

    public Situacion(String analisis, String inicio, String fin, String texto, String nombre) {
        this.analisis = analisis;
        this.inicio = inicio;
        this.fin = fin;
        this.texto = texto;
        this.nombre = nombre;
    }

    public Situacion() {
    }

    public String getAnalisis() {
        return analisis;
    }

    public void setAnalisis(String analisis) {
        this.analisis = analisis;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Situacion{" +
                "analisis='" + analisis + '\'' +
                ", inicio='" + inicio + '\'' +
                ", fin='" + fin + '\'' +
                ", texto='" + texto + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
