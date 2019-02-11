package mdelacalle.com.cartasnauticas;

public class AreaCostera {
    int codigo;
    String area;

    public AreaCostera(int codigo, String area) {
        this.codigo = codigo;
        this.area = area;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
