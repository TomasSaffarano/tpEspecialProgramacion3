package modelos;

public class Paquete {

    private int id;
    private String codigo;
    private double pesoKg;
    private boolean contieneAlimentos;
    private int nivelUrgencia;

    public Paquete(int id, String codigo, double pesoKg, boolean contieneAlimentos, int nivelUrgencia) {
        this.id = id;
        this.codigo = codigo;
        if(pesoKg <= 0){
            throw new IllegalArgumentException("Peso negativo");
        }
        this.pesoKg = pesoKg;
        this.contieneAlimentos = contieneAlimentos;
        this.nivelUrgencia = nivelUrgencia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(double pesoKg) {
        if(pesoKg <= 0){
            throw new IllegalArgumentException("Peso negativo");
        }
        this.pesoKg = pesoKg;
    }

    public boolean isContieneAlimentos() {
        return contieneAlimentos;
    }

    public void setContieneAlimentos(boolean contieneAlimentos) {
        this.contieneAlimentos = contieneAlimentos;
    }

    public int getNivelUrgencia() {
        return nivelUrgencia;
    }

    public void setNivelUrgencia(int nivelUrgencia) {
        this.nivelUrgencia = nivelUrgencia;
    }

    @Override
    public String toString() {
        return "Paquete{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", pesoKg=" + pesoKg +
                ", contieneAlimentos=" + contieneAlimentos +
                ", nivelUrgencia=" + nivelUrgencia +
                '}';
    }
}
