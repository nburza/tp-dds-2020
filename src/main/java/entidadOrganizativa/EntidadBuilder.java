package entidadOrganizativa;

import presupuesto.DireccionPostal;

import java.util.ArrayList;
import java.util.HashMap;

public class EntidadBuilder {

    private String nombreFicticio;
    private String razonSocial;
    private String tipoEntidad;
    private String cuit;
    private String pais;
    private String provincia;
    private String ciudad;
    private String direccion;
    private String codigoIgj;
    private String tipoEntidadJuridica;
    private String categoriaEmpresa;

    public EntidadBuilder() {
    }

    public void setNombreFicticio(String nombreFicticio) {
        this.nombreFicticio = nombreFicticio;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public void setTipoEntidad(String tipoEntidad) {
        this.tipoEntidad = tipoEntidad;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setCodigoIgj(String codigoIgj) {
        this.codigoIgj = codigoIgj;
    }

    public void setTipoEntidadJuridica(String tipoEntidadJuridica) {
        this.tipoEntidadJuridica = tipoEntidadJuridica;
    }

    public void setCategoriaEmpresa(String categoriaEmpresa) {
        this.categoriaEmpresa = categoriaEmpresa;
    }

    public Entidad crear() {
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
