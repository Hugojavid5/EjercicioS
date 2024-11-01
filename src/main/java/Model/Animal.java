package Model;

import java.io.InputStream;
import java.sql.Blob;
import java.time.LocalDate;
import java.util.Objects;


public class Animal {
    private int id;
    private String nombre;
    private String especie;
    private String raza;
    private String sexo;
    private int edad;
    private int peso;
    private String observaciones;
    private LocalDate fecha_primera_consulta;
    private Blob foto;


    public Animal(int id, String nombre, String especie, String raza, String sexo, int edad, int peso, String observaciones, LocalDate fecha_primera_consulta, Blob foto) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.sexo = sexo;
        this.edad = edad;
        this.peso = peso;
        this.observaciones = observaciones;
        this.fecha_primera_consulta = fecha_primera_consulta;
        this.foto = foto;
    }
    public Animal()
    {

    }
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
    public int getPeso() {
        return peso;
    }
    public void setPeso(int peso) {
        this.peso = peso;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    public LocalDate getFecha_primera_consulta() {
        return fecha_primera_consulta;
    }
    public void setFecha_primera_consulta(LocalDate fecha_primera_consulta) {
        this.fecha_primera_consulta = fecha_primera_consulta;
    }
    public Blob getFoto() {
        return foto;
    }
    public void setFoto(Blob foto) {
        this.foto = foto;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return id == animal.id;
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


}
