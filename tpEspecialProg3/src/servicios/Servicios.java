package servicios;

import modelos.Camion;
import modelos.Paquete;

import java.util.*;

public class Servicios {

    private ArrayList<Camion> camiones;
    private ArrayList<Paquete> paquetes;



    private HashMap<String,Paquete> paquetePorCodigo;
    //optimizacion servicio 1


    private List<Paquete> paquetesAlimentos;
    private List<Paquete> paquetesNoAlimentos;
    //optimizacion servicio 1


    private int estadosGenerados;
    private double mejorPeso;
    private HashMap<Camion, ArrayList<Paquete>> mejorAsignacion;
    private HashMap<Camion,Double> pesoActual;
    //variables para optimizacion de paso de informacion en metodo backtracking


    private HashMap<Camion, ArrayList<Paquete>> asignacionGreedy;
    //variables de clase optimizacion greedy

    /*
     * O(C + P)
     * Leer camiones: O(C)
     * Leer paquetes: O(P)
     * Inicializar HashMap por código: O(P)
     *
     * O(C + P + P) => O(C + P)
     */
    public Servicios(String pathCamiones, String pathPaquetes) {
        camiones = new ArrayList<>();
        camiones = CSVReader.leerCamiones(pathCamiones);
        paquetes = new ArrayList<>();
        paquetes = CSVReader.leerPaquetes(pathPaquetes);
        this.iniciarListasPaquetes();
    }




    /*
     * Expresar la complejidad temporal del servicio 1:
     * O(1)
     * La complejidad se optimiza por guardar el map paquetes por codigo.
     */
    public Paquete servicio1(String codigoPaquete) {

        /*Servicio 1: Dado un código de paquete (String), retornar toda la información
        del paquete asociado. En caso de no existir, retornar null.*/

        if (paquetes == null || paquetes.isEmpty()) {
            return null;
        }

        if (codigoPaquete == null || codigoPaquete.isEmpty()) {
            return null;
        }
        return paquetePorCodigo.get(codigoPaquete);
    }

    /*
     * complejidad temporal O(1) guardando en cache local del servicio las listas
     * paquetes alimentos y paquetes no alimentos, iniciandolas en constructor.
     */
    public List<Paquete> servicio2(boolean contieneAlimentos) {
        /* Dado un booleano que indica si se buscan paquetes que
         * contienen alimentos (true) o que no contienen alimentos (false), retornar el
         * listado de paquetes correspondiente. */
        if (paquetes == null|| paquetes.isEmpty()) {
            return new ArrayList<>();
        }

        List<Paquete> paquetesQueCumplen = new ArrayList<>();
        if (contieneAlimentos)
            return paquetesAlimentos==null||paquetesAlimentos.isEmpty()? new ArrayList<>(): paquetesAlimentos;
        else
            return paquetesNoAlimentos==null||paquetesNoAlimentos.isEmpty()? new ArrayList<>(): paquetesNoAlimentos;
    }

    /*
     * Expresar la complejidad temporal del servicio 3: O(P) p: cantidad paquetes
     * recorro una vez el array de paquetes
     */
    public List<Paquete> servicio3(int urgenciaMinima, int
            urgenciaMaxima) {
        /* Dados dos valores enteros que representan un nivel de urgencia
         * mínimo y máximo, retornar todos los paquetes cuyo nivel de urgencia se
         * encuentre dentro de ese rango (inclusive).*/

        if (paquetes == null|| paquetes.isEmpty()) {
            return new ArrayList<>();
        }

        if (urgenciaMinima < 0 || urgenciaMaxima < 0) {
            return new ArrayList<>();
        }

        if (urgenciaMinima > urgenciaMaxima) {
            int aux = urgenciaMaxima;
            urgenciaMaxima = urgenciaMinima;
            urgenciaMinima = aux;
            System.out.println("Se han dislexeado los numeros, los di vuelta...");
        }

        List<Paquete> paquetesQueCumplen = new ArrayList<>();
        for (Paquete paquete : paquetes) {
            int urgencia = paquete.getNivelUrgencia();
            if (urgenciaMinima <= urgencia &&
                    urgencia <= urgenciaMaxima) {
                paquetesQueCumplen.add(paquete);
            }
        }
        return paquetesQueCumplen;
    }




    /*
     *
     *
     *   SEGUNDA PARTE
     *
     *
     *
     *
Se desea establecer una asignación de todos los paquetes a los camiones
disponibles con el objetivo de minimizar el peso total de los paquetes que no
pudieron ser asignados a ningún camión.
Se sabe que existen ciertas restricciones para asignar un paquete a un camión:
• Primero, ningún camión podrá superar su capacidad máxima de carga. La
capacidad de cada camión está definida en el archivo de entrada.
• Segundo, los paquetes que contienen alimentos sólo podrán ser asignados
a camiones refrigerados.
     *
     *
     */




    //O((C+1)^P)
    // porque es O(P + private backtracking), gana el del recursivo
    public double backtracking() {
        this.estadosGenerados = 0;
        mejorPeso = Double.MAX_VALUE;
        mejorAsignacion = new HashMap<>();

        HashMap<Camion, ArrayList<Paquete>> asignacionActual = new HashMap<>();

        pesoActual = new HashMap<>();

        if(paquetes==null || paquetes.isEmpty()) {
            System.out.println("No hay paquetes en el sistema, inicializar.");
            return 00.00;
        }

        if(camiones==null|| camiones.isEmpty()) {
            System.out.println("No hay camiones en el sistema, inicializar.");
            double pesoTotal = 0;
            for (Paquete p: paquetes) {
                pesoTotal += p.getPesoKg();
            }
            return pesoTotal;
        }

        for (Camion c : camiones) {

            asignacionActual.put(c, new ArrayList<>());

            pesoActual.put(c, 0.0);
        }

        backtracking(
                0,
                asignacionActual,
                0.0);

        this.mostrarMejorAsignacion();

        return mejorPeso;
    }


    /*
    Cada paquete genera C+1 decisiones
    (asignarlo a alguno de los C camiones
    o dejarlo sin asignar).

    Profundidad = P

    Cantidad de estados

    (C+1)^P

    Las verificaciones son O(1)

    Por lo tanto

    O((C+1)^P) */

    // TOTAL: O((C+1)^P) p paquetes, c camiones
    private void backtracking(
            int indicePaquete,
            HashMap<Camion, ArrayList<Paquete>> asignacionActual,
            double pesoNoAsignadoActual) {

        this.estadosGenerados++;

        //PODA
        if (pesoNoAsignadoActual >= mejorPeso)
            return;

        // CASO BASE
        if (indicePaquete == paquetes.size()) {

            if (pesoNoAsignadoActual <= mejorPeso) {

                mejorPeso = pesoNoAsignadoActual;

                mejorAsignacion = copiarAsignacion(
                        asignacionActual);
            }

            return;
        }

        Paquete paquete =
                paquetes.get(indicePaquete);

        // OPCION 1:
        // Intentar asignarlo a cada camión válido

        for (Camion camion : camiones) {

            if (puedeAsignarse(
                    paquete,
                    camion)) {

                asignacionActual
                        .get(camion)
                        .add(paquete);

                double pesoCamionPrevio = pesoActual.get(camion);

                pesoActual.put(camion, pesoCamionPrevio+ paquete.getPesoKg());

                backtracking(
                        indicePaquete + 1,
                        asignacionActual,
                        pesoNoAsignadoActual);

                // BACKTRACK

                asignacionActual
                        .get(camion)
                        .remove(paquete);

                pesoActual.put(camion, pesoCamionPrevio);
            }
        }

        // OPCION 2:
        // dejar el paquete afuera

        backtracking(
                indicePaquete + 1,
                asignacionActual,
                pesoNoAsignadoActual
                        + paquete.getPesoKg());
    }


/*
    *Ordenar camiones

    O(C log C)

    Ordenar paquetes

    O(P log P)

    Asignación

    Cada camión puede recorrer todos los paquetes.

    O(C·P)

    Total

    O(C log C + P log P + C·P)*/

    public double greedy(){

        ArrayList<Camion> camionesGreedy = new ArrayList<>();
        ArrayList<Paquete> paquetesGreedy = new ArrayList<>();
        estadosGenerados=0;

        asignacionGreedy = new HashMap<>();

        if(paquetes==null || paquetes.isEmpty()) {
            System.out.println("No hay paquetes en el sistema, inicializar.");
            return 00.00;
        }

        paquetesGreedy = new ArrayList<Paquete>(paquetes);

        if(camiones==null || camiones.isEmpty()) {
            System.out.println("No hay camiones en el sistema, inicializar.");
            double pesoTotal = 0;
            for (Paquete p: paquetesGreedy) {
                pesoTotal += p.getPesoKg();
            }
            return pesoTotal;
        }


        //paquetes por peso y luego urgencia
        paquetesGreedy.sort(
                Comparator.comparingDouble(
                                Paquete::getPesoKg
                        ).reversed()
                        .thenComparing(
                                Comparator.comparingInt(Paquete::getNivelUrgencia)
                                        .reversed()
                        ));

        camionesGreedy = new ArrayList<>(this.camiones);
        //vamos a ordenar camiones por peso
        camionesGreedy.sort(
                Comparator.comparingDouble(
                        Camion::getCapacidadKg
                ).reversed()
        );


        for(Camion camion : camionesGreedy){
            asignacionGreedy.put(camion, new ArrayList<>());
        }



        //primero por cada camion para llenarlos todos...


        for(Camion camion : camionesGreedy){

            double pesoDisponible = camion.getCapacidadKg();
            Iterator<Paquete> it = paquetesGreedy.iterator();

            while(it.hasNext()){

                estadosGenerados++;

                Paquete paq = it.next();

                //si no es refrigerado el camion pero paquete es de alimentos, se saltea...
                if(paq.isContieneAlimentos()
                        && !camion.isRefrigerado()){
                    continue;
                }

                if(paq.getPesoKg() <= pesoDisponible){

                    asignacionGreedy.get(camion).add(paq);

                    pesoDisponible -= paq.getPesoKg();

                    it.remove();
                    //al eliminarlo sobre el iterador lo elimino de ambas
                }

                if(pesoDisponible == 0){
                    break; //paso al proximo camion
                }
            }
        }

        double resto = 0.0;
        if(!paquetesGreedy.isEmpty()){
            for(Paquete paquete : paquetesGreedy){
                resto +=paquete.getPesoKg();
            }
        }
        this.mostrarAsignacionGreedy(resto);

        return resto;
    }



    /*
    *
    * Metodos privados internos
    *
    * */


    private void mostrarAsignacionGreedy(Double resto) {

        System.out.println("===== SOLUCION OBTENIDA GREEDY =====");

        for (Camion camion : asignacionGreedy.keySet()) {

            Double pesoCamion = 00.00;
            System.out.println("Camion " + camion.getPatente());

            for (Paquete paquete : asignacionGreedy.get(camion)) {

                System.out.println(
                        "   - " + paquete.getCodigo()
                                + " (" + paquete.getPesoKg() + " kg)"
                );

                pesoCamion += paquete.getPesoKg();
            }

            System.out.println("Peso asignado del camion: "+pesoCamion+"kg.");
            System.out.println();
        }
        System.out.println("Peso no asignado: "+resto+"kg.");
        System.out.println(
                "Cantidad de candidatos considerados: "
                        + estadosGenerados
        );
    }


    private void mostrarMejorAsignacion() {

        System.out.println("===== SOLUCION OBTENIDA BACKTRACKING =====");

        for (Camion camion : mejorAsignacion.keySet()) {

            System.out.println(
                    "Camion " + camion.getPatente()
                            + " (" + camion.getCapacidadKg() + " kg)"
            );

            double pesoCamion = 0;

            for (Paquete paquete : mejorAsignacion.get(camion)) {

                System.out.println(
                        "   - " + paquete.getCodigo()
                                + " (" + paquete.getPesoKg() + " kg)"
                );

                pesoCamion += paquete.getPesoKg();
            }

            System.out.println(
                    "   Peso cargado: "
                            + pesoCamion
                            + " kg"
            );

            System.out.println();
        }

        System.out.println(
                "Peso no asignado: "
                        + mejorPeso
                        + " kg"
        );
        System.out.println("Estados generados: " + estadosGenerados);

    }

    //usado en constructor
    private void iniciarListasPaquetes(){
        paquetePorCodigo = new HashMap<>();
        paquetesAlimentos = new ArrayList<>();
        paquetesNoAlimentos = new ArrayList<>();
        if(this.paquetes!=null){
            for(Paquete paquete:paquetes){
                paquetePorCodigo.put(paquete.getCodigo(),paquete);
                if(paquete.isContieneAlimentos())
                    paquetesAlimentos.add(paquete);
                else
                    paquetesNoAlimentos.add(paquete);
            }
        }
    }


    // O(1) optimizacion por map peso actual
    private boolean puedeAsignarse(
            Paquete paquete,
            Camion camion) {

        if (paquete.isContieneAlimentos()
                && !camion.isRefrigerado()) {

            return false;
        }

        return pesoActual.get(camion) +
                paquete.getPesoKg()
                <= camion.getCapacidadKg();

    }



    //O(C + P) C camiones y P paquetes
    private HashMap<Camion, ArrayList<Paquete>> copiarAsignacion(
            HashMap<Camion, ArrayList<Paquete>> original) {

        HashMap<Camion, ArrayList<Paquete>> copia =
                new HashMap<>();

        for (Camion c : original.keySet()) {

            copia.put(
                    c,
                    new ArrayList<>(
                            original.get(c)));
        }

        return copia;
    }




}
