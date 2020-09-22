package entidadOrganizativa;


import persistencia.EntidadPersistente;
import egreso.Egreso;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", length = 4)
public abstract class Regla extends EntidadPersistente {
    public void verificarAgregadoDeEgreso(Entidad unaEntidad, Egreso unEgreso){

    }

    public void verificarSiEntidadBaseEsIncorporable(){

    }

    public void verificarSiEntidadJuridicaPuedeAgregarEntidadesBase(){

    }


}
