package egreso;

import persistencia.EntidadPersistente;

import javax.persistence.Entity;
import javax.persistence.Enumerated;

//@Entity??
public enum EstadoValidacion
{
    VALIDO,
    PENDIENTE
}