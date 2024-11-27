package com.example.JAVAFX.CRISTINADIAZCABELLO.Dao;

import com.example.JAVAFX.CRISTINADIAZCABELLO.modelos.DiaEstadoAnimoCR;

import java.sql.*;
import java.time.LocalDate;

public class DiaEstadoAnimoCRDAOclass implements DiaEstadoAnimoCRDAO {

    private final Connection connection;

    public DiaEstadoAnimoCRDAOclass(Connection connection) {
        this.connection = connection;
    }

    @Override
    public DiaEstadoAnimoCR findByFechaAndMomento(LocalDate fecha, String momento) {
        String query = "SELECT * FROM Dia_EstadoAnimo_CR WHERE fecha = ? AND momento_dia = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setDate(1, Date.valueOf(fecha));
            ps.setString(2, momento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new DiaEstadoAnimoCR(
                            rs.getDate("fecha"),
                            rs.getInt("id_estado"),
                            rs.getString("momento_dia"),
                            rs.getString("descripcion")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void insert(DiaEstadoAnimoCR diaEstadoAnimoCR) {
        String query = "INSERT INTO Dia_EstadoAnimo_CR (fecha, id_estado, momento_dia, descripcion) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setDate(1, (Date) diaEstadoAnimoCR.getFecha());
            ps.setInt(2, diaEstadoAnimoCR.getIdEstado());
            ps.setString(3, diaEstadoAnimoCR.getMomentoDia());
            ps.setString(4, diaEstadoAnimoCR.getDescripcion());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(DiaEstadoAnimoCR diaEstadoAnimoCR) {
        String query = "UPDATE Dia_EstadoAnimo_CR SET id_estado = ?, momento_dia = ?, descripcion = ? WHERE fecha = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, diaEstadoAnimoCR.getIdEstado());
            ps.setString(2, diaEstadoAnimoCR.getMomentoDia());
            ps.setString(3, diaEstadoAnimoCR.getDescripcion());
            ps.setDate(4, (Date) diaEstadoAnimoCR.getFecha());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(LocalDate fecha, String momento) {
        String query = "DELETE FROM Dia_EstadoAnimo_CR WHERE fecha = ? AND momento_dia = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setDate(1, Date.valueOf(fecha));
            ps.setString(2, momento);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

