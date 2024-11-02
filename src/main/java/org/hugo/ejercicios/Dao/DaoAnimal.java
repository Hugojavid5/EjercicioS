package org.hugo.ejercicios.Dao;

import org.hugo.ejercicios.BBDD.ConexionBBDD;
import org.hugo.ejercicios.Model.Animal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;

public class DaoAnimal {
    public static Animal getAnimal(int id) {
        ConexionBBDD connection;
        Animal animal = null;
        try {
            connection = new ConexionBBDD();
            String consulta = "SELECT id, nombre, especie, raza, sexo, edad, peso, observaciones, fecha_primera_consulta, foto FROM animales WHERE id = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int animalId = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String especie = rs.getString("especie");
                String raza = rs.getString("raza");
                String sexo = rs.getString("sexo");
                int edad = rs.getInt("edad");
                double peso = rs.getDouble("peso");
                String observaciones = rs.getString("observaciones");
                Date fecha_primera_consulta = rs.getDate("fecha_primera_consulta");
                Blob foto = rs.getBlob("foto");
                animal = new Animal(animalId, nombre, especie, raza, sexo, edad, peso, observaciones, fecha_primera_consulta, foto);
            }
            rs.close();
            connection.closeConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return animal;
    }
    public static ObservableList<Animal> cargarListado() {
        ConexionBBDD connection;
        ObservableList<Animal> animalList = FXCollections.observableArrayList();
        try {
            connection = new ConexionBBDD();
            String consulta = "SELECT id, nombre, especie, raza, sexo, edad, peso, observaciones, fecha_primera_consulta, foto FROM animales";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String especie = rs.getString("especie");
                String raza = rs.getString("raza");
                String sexo = rs.getString("sexo");
                int edad = rs.getInt("edad");
                double peso = rs.getDouble("peso");
                String observaciones = rs.getString("observaciones");
                Date fecha_primera_consulta = rs.getDate("fecha_primera_consulta");
                Blob foto = rs.getBlob("foto");
                Animal animal = new Animal(id, nombre, especie, raza, sexo, edad, peso, observaciones, fecha_primera_consulta, foto);
                animalList.add(animal);
            }
            rs.close();
            connection.closeConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return animalList;
    }
    public static boolean modificar(Animal animal, Animal animalNuevo) {
        ConexionBBDD connection;
        PreparedStatement pstmt;
        try {
            connection = new ConexionBBDD();
            String consulta = "UPDATE animales SET nombre = ?, especie = ?, raza = ?, sexo = ?, edad = ?, peso = ?, observaciones = ?, fecha_primera_consulta = ?, foto = ? WHERE id = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, animalNuevo.getNombre());
            pstmt.setString(2, animalNuevo.getEspecie());
            pstmt.setString(3, animalNuevo.getRaza());
            pstmt.setString(4, animalNuevo.getSexo());
            pstmt.setInt(5, animalNuevo.getEdad());
            pstmt.setDouble(6, animalNuevo.getPeso());
            pstmt.setString(7, animalNuevo.getObservaciones());
            pstmt.setDate(8, new java.sql.Date(animalNuevo.getFechaPrimeraConsulta().getTime()));
            pstmt.setBlob(9, animalNuevo.getFoto());
            pstmt.setInt(10, animal.getId());
            int filasAfectadas = pstmt.executeUpdate();
            pstmt.close();
            connection.closeConexion();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static int insertar(Animal animal) {
        ConexionBBDD connection;
        PreparedStatement pstmt;
        try {
            connection = new ConexionBBDD();
            String consulta = "INSERT INTO animales (nombre, especie, raza, sexo, edad, peso, observaciones, fecha_primera_consulta, foto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmt = connection.getConnection().prepareStatement(consulta, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, animal.getNombre());
            pstmt.setString(2, animal.getEspecie());
            pstmt.setString(3, animal.getRaza());
            pstmt.setString(4, animal.getSexo());
            pstmt.setInt(5, animal.getEdad());
            pstmt.setDouble(6, animal.getPeso());
            pstmt.setString(7, animal.getObservaciones());
            pstmt.setDate(8, new java.sql.Date(animal.getFechaPrimeraConsulta().getTime()));
            pstmt.setBlob(9, animal.getFoto());
            int filasAfectadas = pstmt.executeUpdate();
            if (filasAfectadas > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    pstmt.close();
                    connection.closeConexion();
                    return id;
                }
            }
            pstmt.close();
            connection.closeConexion();
            return -1;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return -1;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean eliminar(Animal animal) {
        ConexionBBDD connection;
        PreparedStatement pstmt;
        try {
            connection = new ConexionBBDD();
            String consulta = "DELETE FROM animales WHERE id = ?";
            pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, animal.getId());
            int filasAfectadas = pstmt.executeUpdate();
            pstmt.close();
            connection.closeConexion();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static Blob convertFileToBlob(File file) throws SQLException, FileNotFoundException {
        ConexionBBDD connection = new ConexionBBDD();
        try (Connection conn = connection.getConnection();
             FileInputStream inputStream = new FileInputStream(file)) {

            Blob blob = conn.createBlob();
            byte[] buffer = new byte[1024];
            int bytesRead;

            try (var outputStream = blob.setBinaryStream(1)) {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            return blob;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}