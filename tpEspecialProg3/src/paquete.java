public class paquete {

    private int id;
    private String codigo;
    private double pesoKg;
    private boolean contieneAlimentos;
    private int nivelUrgencia;

    public paquete(int id, String codigo, double pesoKg, boolean contieneAlimentos, int nivelUrgencia) {
        this.id = id;
        this.codigo = codigo;
        this.pesoKg = pesoKg;
        this.contieneAlimentos = contieneAlimentos;
        this.nivelUrgencia = nivelUrgencia;
    }

    public int getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public boolean isContieneAlimentos() {
        return contieneAlimentos;
    }

    public int getNivelUrgencia() {
        return nivelUrgencia;
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
