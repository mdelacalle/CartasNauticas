package mdelacalle.com.cartasnauticas;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Prediccion extends RealmObject {
    String inicio;
    String fin;
    RealmList<Zona> zonas;

    public Prediccion(String inicio, String fin, RealmList<Zona> zonas) {
        this.inicio = inicio;
        this.fin = fin;
        this.zonas = zonas;
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

    public RealmList<Zona> getZonas() {
        return zonas;
    }

    public void setZonas(RealmList<Zona> zonas) {
        this.zonas = zonas;
    }

    @Override
    public String toString() {
        return "Prediccion{" +
                "inicio='" + inicio + '\'' +
                ", fin='" + fin + '\'' +
                ", zonas=" + zonas +
                '}';
    }
}
