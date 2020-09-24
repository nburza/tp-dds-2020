/*import mediosDePago.MedioDePago;
import mediosDePago.TarjetaCredito;
//import proveedor.*;
import java.io.Console;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class EjemploCreacionDireccionPostal {
    public static void main(String[] args)  {
        //Esto claramente repite codigo, pero como probablemente lo descartaremos y es solo para mostrar, no se si es correcto pero podria crear interfaces para pulir el main

        Scanner scan =new Scanner(System.in);
        List<Pais> paises = new ServicioUbicacionMercadoLibre().getPaises();
        Iterator<Pais> itPais= paises.iterator();
        int posicion=0;
        while (itPais.hasNext())
        {
            System.out.println(itPais.next().getName() + ", Numero: " + posicion++);
        }

        System.out.println("Ingrese numero de pais:");

        int numeroPais= scan.nextInt();

        Pais pais =paises.get(numeroPais);

        posicion=0;
        List<Provincia> provincias = new ServicioUbicacionMercadoLibre().getProvincias(pais);
        Iterator<Provincia> itProvincia= provincias.iterator();
        while (itProvincia.hasNext())
        {
            System.out.println(itProvincia.next().getName() + ", Numero: " + posicion++);
        }
        System.out.println("Ingrese numero de provincia:");
        int numeroProvincia= scan.nextInt();


        Provincia provincia=provincias.get(numeroProvincia);

        posicion=0;
        List<Ciudad> ciudades = new ServicioUbicacionMercadoLibre().getCiudades(provincia);

        Iterator<Ciudad> itCiudad= ciudades.iterator();
        while (itCiudad.hasNext())
        {
            System.out.println(itCiudad.next().getName() + ", Numero: " + posicion++);
        }
        System.out.println("Ingrese numero de Ciudad:");

        int numeroCiudad = scan.nextInt();
        Ciudad ciudad=ciudades.get(numeroCiudad);



        System.out.println("Ingrese codigo Postal:");

        String codigopostal=scan.next();


        DireccionPostal nuevaDireccion =new DireccionPostal(pais,provincia,ciudad,codigopostal);

        System.out.println("DireccionPostalCreada.");

        System.out.println("Pais: "+nuevaDireccion.getPais().getName());
        System.out.println("Provincia: "  +nuevaDireccion.getProvincia().getName());
        System.out.println("Ciudad: " +nuevaDireccion.getCiudad().getName());
        System.out.println("Codigo Postal: " +nuevaDireccion.getDireccion());

    }

 //   public void mostrarElementoDeLaLista (List lista){
    //     int posicion=0;
    //     Iterator it =lista.iterator();
    //     while (it.hasNext())
    //    {
    //         System.out.println(it.next().getName() + ", Numero: " + posicion++)
    //    }
 //  }
}
*/