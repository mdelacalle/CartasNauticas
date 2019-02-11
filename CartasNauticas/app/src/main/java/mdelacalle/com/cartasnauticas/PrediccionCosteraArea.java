package mdelacalle.com.cartasnauticas;

import io.realm.RealmObject;

public class PrediccionCosteraArea extends RealmObject {
    String productor;
    String nombre;
    String area;
    String elaborado;
    String inicio;
    String fin;
    String notaLegal;
    Aviso aviso;
    Situacion situacion;
    Prediccion prediccion;
    Tendencia tendencia;

    public String getProductor() {
        return productor;
    }

    public void setProductor(String productor) {
        this.productor = productor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getElaborado() {
        return elaborado;
    }

    public void setElaborado(String elaborado) {
        this.elaborado = elaborado;
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

    public String getNotaLegal() {
        return notaLegal;
    }

    public void setNotaLegal(String notaLegal) {
        this.notaLegal = notaLegal;
    }

    public Aviso getAviso() {
        return aviso;
    }

    public void setAviso(Aviso aviso) {
        this.aviso = aviso;
    }

    public Situacion getSituacion() {
        return situacion;
    }

    public void setSituacion(Situacion situacion) {
        this.situacion = situacion;
    }

    public Prediccion getPrediccion() {
        return prediccion;
    }

    public void setPrediccion(Prediccion prediccion) {
        this.prediccion = prediccion;
    }

    public Tendencia getTendencia() {
        return tendencia;
    }

    public void setTendencia(Tendencia tendencia) {
        this.tendencia = tendencia;
    }

    @Override
    public String toString() {
        return "PrediccionCosteraArea{" +
                "productor='" + productor + '\'' +
                ", nombre='" + nombre + '\'' +
                ", area='" + area + '\'' +
                ", elaborado='" + elaborado + '\'' +
                ", inicio='" + inicio + '\'' +
                ", fin='" + fin + '\'' +
                ", notaLegal='" + notaLegal + '\'' +
                ", aviso=" + aviso +
                ", situacion=" + situacion +
                ", prediccion=" + prediccion +
                ", tendencia=" + tendencia +
                '}';
    }
}
