package servicios;

import modelos.Camion;
import modelos.Paquete;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CSVReader {

    public static ArrayList<Camion> leerCamiones(String path) {
        ArrayList<Camion> camiones = new ArrayList<>();

        try {
            InputStream is = CSVReader.class.getResourceAsStream(path);
            if(is == null) {
                System.out.println("Archivo no encontrado "+path+", se inicializará vacío.");
                return camiones;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int total = Integer.parseInt(br.readLine());
            if(total == 0) {
                System.out.println("Total de camiones no válido, se inicializará vacío.");
                return camiones;
            }
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");

                int id = Integer.parseInt(partes[0]);
                String patente = partes[1];
                boolean refrigerado = partes[2].equals("1");
                double capacidad = Double.parseDouble(partes[3]);

                camiones.add(new Camion(id, patente, refrigerado, capacidad));
            }

            br.close();

        } catch (Exception e) {
            System.out.println("ERROR en CSV Reader: "+e.getMessage()+". Se inicializará vacío.");
            return camiones;
        }

        return camiones;
    }

    public static ArrayList<Paquete> leerPaquetes(String path) {
        ArrayList<Paquete> paquetes = new ArrayList<>();

        try {
            InputStream is = CSVReader.class.getResourceAsStream(path);
            if(is == null) {
                System.out.println("Archivo no encontrado "+path+", se inicializará vacío.");
                return paquetes;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int total = Integer.parseInt(br.readLine());
            if(total == 0) {
                System.out.println("Total de paquetes no válido, se inicializará vacío.");
                return paquetes;
            }
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");

                int id = Integer.parseInt(partes[0]);
                String codigo = partes[1];
                double peso = Double.parseDouble(partes[2]);
                boolean alimentos = partes[3].equals("1");
                int urgencia = Integer.parseInt(partes[4]);

                paquetes.add(new Paquete(id, codigo, peso, alimentos, urgencia));
            }

            br.close();

        } catch (Exception e) {
            System.out.println("ERROR en CSV Reader: "+e.getMessage()+". Se inicializará vacío.");
            return paquetes;
        }

        return paquetes;
    }
}
