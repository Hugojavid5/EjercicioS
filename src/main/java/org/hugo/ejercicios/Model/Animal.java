package org.hugo.ejercicios.Model;

import java.sql.Blob;
import java.util.Date;
import java.util.Objects;

/**
 * Representa un animal en la base de datos veterinaria.
 * Incluye información sobre el nombre, especie, raza, sexo, edad, peso, observaciones, fecha de primera consulta y una foto del animal.
 */
public class Animal {
    private int id;
    private String nombre;
    private String especie;
    private String raza;
    private String sexo;
    private int edad;
    private double peso;
    private String observaciones;
    private Date fechaPrimeraConsulta;
    private Blob foto;

    /**
     * Constructor completo de la clase Animal.
     *
     * @param id                  el identificador único del animal
     * @param nombre              el nombre del animal
     * @param especie             la especie del animal
     * @param raza                la raza del animal
     * @param sexo                el sexo del animal
     * @param edad                la edad del animal en años
     * @param peso                el peso del animal en kilogramos
     * @param observaciones       observaciones adicionales sobre el animal
     * @param fechaPrimeraConsulta la fecha de la primera consulta veterinaria
     * @param foto                la foto del animal como un Blob
     */
    public Animal(int id, String nombre, String especie, String raza, String sexo, int edad, double peso, String observaciones, Date fechaPrimeraConsulta, Blob foto) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.sexo = sexo;
        this.edad = edad;
        this.peso = peso;
        this.observaciones = observaciones;
        this.fechaPrimeraConsulta = fechaPrimeraConsulta;
        this.foto = foto;
    }

    /**
     * Constructor vacío para la clase Animal.
     */
    public Animal() {
    }

    /** Getter y setter para cada uno de los atributos de la clase. **/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFechaPrimeraConsulta() {
        return fechaPrimeraConsulta;
    }

    public void setFechaPrimeraConsulta(Date fechaPrimeraConsulta) {
        this.fechaPrimeraConsulta = fechaPrimeraConsulta;
    }

    public Blob getFoto() {
        return foto;
    }

    public void setFoto(Blob foto) {
        this.foto = foto;
    }

    /**
     * Compara si dos objetos Animal son iguales en función de todos sus atributos.
     *
     * @param o el objeto a comparar
     * @return true si los atributos de ambos objetos son iguales, de lo contrario false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return id == animal.id && edad == animal.edad && Double.compare(peso, animal.peso) == 0 && Objects.equals(nombre, animal.nombre) && Objects.equals(especie, animal.especie) && Objects.equals(raza, animal.raza) && Objects.equals(sexo, animal.sexo) && Objects.equals(observaciones, animal.observaciones) && Objects.equals(fechaPrimeraConsulta, animal.fechaPrimeraConsulta) && Objects.equals(foto, animal.foto);
    }

    /**
     * Genera un código hash para el objeto Animal.
     *
     * @return el código hash basado en todos los atributos del objeto
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, especie, raza, sexo, edad, peso, observaciones, fechaPrimeraConsulta, foto);
    }

    /**
     * Convierte el objeto Animal en una representación de cadena.
     *
     * @return una cadena que representa al animal, incluyendo su nombre, especie, raza, sexo, edad, peso, observaciones y fecha de primera consulta
     */
    @Override
    public String toString() {
        return nombre +
                ", " + especie +
                ", " + raza +
                ", " + sexo +
                ", " + edad +
                " años, " + peso +
                "kg. Observaciones: " + observaciones +
                fechaPrimeraConsulta;
    }
}
