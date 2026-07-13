import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactoDAO {

    // Crear un contacto nuevo
    public void crear(String nombre, String telefono, String email, String categoria) {
        String sql = "INSERT INTO contactos (nombre, telefono, email, categoria) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, telefono);
            stmt.setString(3, email);
            stmt.setString(4, categoria);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al crear contacto: " + e.getMessage());
        }
    }

    // Listar todos los contactos
    public List<Contacto> listarTodos() {
        return buscar(""); // reutilizamos el método de búsqueda con texto vacío
    }

    // Buscar contactos por nombre (esta es la parte nueva)
    public List<Contacto> buscar(String texto) {
        List<Contacto> contactos = new ArrayList<>();
        String sql = "SELECT * FROM contactos WHERE nombre ILIKE ? ORDER BY nombre";
        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + texto + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Contacto c = new Contacto(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("telefono"),
                    rs.getString("email"),
                    rs.getString("categoria")
                );
                contactos.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar: " + e.getMessage());
        }
        return contactos;
    }

    // Eliminar contacto
    public void eliminar(int id) {
        String sql = "DELETE FROM contactos WHERE id = ?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar: " + e.getMessage());
        }
    }
}