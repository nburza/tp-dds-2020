package entidadOrganizativa;

import presupuesto.DireccionPostal;

import java.util.ArrayList;
import java.util.HashMap;

public class EntidadBuilder {

    public EntidadBuilder() {
    }

    public Entidad crearEntidadQueCorresponda(String nombreFicticio, String razonSocial, String tipoEntidad, String cuit, String pais, String provincia, String ciudad, String direccion, String codigoIgj, String tipoEntidadJuridica, String categoriaEmpresa) {
        Entidad entidad;
        if (tipoEntidad.equals("base")) {
            entidad = new EntidadBase(nombreFicticio, razonSocial, new ArrayList<>());
        } else {
            DireccionPostal direccionPostal = new DireccionPostal(pais, provincia, ciudad, direccion);
            if (tipoEntidadJuridica.equals("oss")) {
                entidad = new OrganizacionSectorSocial(razonSocial, nombreFicticio, Long.parseLong(cuit), direccionPostal, Integer.valueOf(codigoIgj), new ArrayList<>());
            } else {
                entidad = new Empresa(razonSocial, nombreFicticio, Long.parseLong(cuit), direccionPostal, Integer.valueOf(codigoIgj), parsearCategoriaEmpresa(categoriaEmpresa), new ArrayList<>());
            }
        }
        return entidad;
    }

    private CategoriaEmpresa parsearCategoriaEmpresa(String categoriaEmpresa) {
        HashMap<String, CategoriaEmpresa> DiccionarioDeCategorias = new HashMap<>();
        DiccionarioDeCategorias.put("micro", CategoriaEmpresa.MICRO);
        DiccionarioDeCategorias.put("pequenia",CategoriaEmpresa.PEQUENIA);
        DiccionarioDeCategorias.put("medianaTramo1", CategoriaEmpresa.MEDIANA_TRAMO_1);
        DiccionarioDeCategorias.put("medianaTramo2", CategoriaEmpresa.MEDIANA_TRAMO_2);
        return DiccionarioDeCategorias.get(categoriaEmpresa);
    }
}
