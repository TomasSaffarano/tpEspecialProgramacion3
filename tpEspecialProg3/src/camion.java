public class camion {

    private int id;
    private String patente;
    private boolean refrigerado;
    private double capacidadKg;

    public camion(int id, String patente, boolean refrigerado, double capacidadKg) {
        this.id = id;
        this.patente = patente;
        this.refrigerado = refrigerado;
        this.capacidadKg = capacidadKg;
    }

    public int getId() {
        return id;
    }

    public String getPatente() {
        return patente;
    }

    public boolean isRefrigerado() {
        return refrigerado;
    }

    public double getCapacidadKg() {
        return capacidadKg;
    }

    @Override
    public String toString() {
        return "Camion{" +
                "id=" + id +
                ", patente='" + patente + '\'' +
                ", refrigerado=" + refrigerado +
                ", capacidadKg=" + capacidadKg +
                '}';
    }
}
