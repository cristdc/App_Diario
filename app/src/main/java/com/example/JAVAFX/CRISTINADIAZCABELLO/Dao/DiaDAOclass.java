package com.example.JAVAFX.CRISTINADIAZCABELLO.Dao;

import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.Dia;

import java.sql.*;
import java.time.LocalDate;

public class DiaDAOclass implements DiaDAO {
    private final Connection conexion;

    public DiaDAOclass(Connection conexion) {
        this.conexion = conexion;
    }

    @Override
    public Dia findByFecha(LocalDate fecha) {
        String query = "SELECT * FROM Dia WHERE fecha = ?";
        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setDate(1, java.sql.Date.valueOf(fecha));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Dia(
                            rs.getDate("fecha"),
                            rs.getInt("calidad_sueño"),
                            rs.getString("clima"),
                            rs.getBoolean("siesta"),
                            rs.getString("retos")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void insert(Dia dia) {
        String query = "INSERT INTO Dia (fecha, calidad_sueño, clima, siesta, retos) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setDate(1, (Date) dia.getFecha());
            ps.setInt(2, dia.getCalidadSueno());
            ps.setString(3, dia.getClima());
            ps.setBoolean(4, dia.isSiesta());
            ps.setString(5, dia.getRetos());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Dia dia) {
        String query = "UPDATE Dia SET calidad_sueño = ?, clima = ?, siesta = ?, retos = ? WHERE fecha = ?";
        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setInt(1, dia.getCalidadSueno());
            ps.setString(2, dia.getClima());
            ps.setBoolean(3, dia.isSiesta());
            ps.setString(4, dia.getRetos());
            ps.setDate(5, (Date) dia.getFecha());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(LocalDate fecha) {
        String query = "DELETE FROM Dia WHERE fecha = ?";
        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setDate(1, java.sql.Date.valueOf(fecha));
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

