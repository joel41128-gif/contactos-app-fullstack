import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

public class ServidorApp {

    static ContactoDAO contactoDAO = new ContactoDAO();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
        server.createContext("/contactos", ServidorApp::manejarContactos);
        server.setExecutor(null);
        server.start();
        System.out.println("Servidor corriendo en http://localhost:8081/contactos");
    }

    static void manejarContactos(HttpExchange exchange) throws IOException {
        String metodo = exchange.getRequestMethod();
        String respuesta = "";

        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        exchange.getResponseHeaders().add("Content-Type", "application/json");

        if (metodo.equals("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if (metodo.equals("GET")) {
            String query = exchange.getRequestURI().getQuery(); // ejemplo: buscar=jo
            String textoBusqueda = "";

            if (query != null && query.startsWith("buscar=")) {
                textoBusqueda = query.split("=")[1];
            }

            List<Contacto> contactos = contactoDAO.buscar(textoBusqueda);
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < contactos.size(); i++) {
                Contacto c = contactos.get(i);
                json.append("{")
                    .append("\"id\":").append(c.getId()).append(",")
                    .append("\"nombre\":\"").append(c.getNombre()).append("\",")
                    .append("\"telefono\":\"").append(c.getTelefono() != null ? c.getTelefono() : "").append("\",")
                    .append("\"email\":\"").append(c.getEmail() != null ? c.getEmail() : "").append("\",")
                    .append("\"categoria\":\"").append(c.getCategoria() != null ? c.getCategoria() : "").append("\"")
                    .append("}");
                if (i < contactos.size() - 1) json.append(",");
            }
            json.append("]");
            respuesta = json.toString();

        } else if (metodo.equals("POST")) {
            String cuerpo = new String(exchange.getRequestBody().readAllBytes());
            String nombre = extraerCampo(cuerpo, "nombre");
            String telefono = extraerCampo(cuerpo, "telefono");
            String email = extraerCampo(cuerpo, "email");
            String categoria = extraerCampo(cuerpo, "categoria");

            contactoDAO.crear(nombre, telefono, email, categoria);
            respuesta = "{\"mensaje\":\"contacto creado\"}";

        } else if (metodo.equals("DELETE")) {
            String query = exchange.getRequestURI().getQuery();
            int id = Integer.parseInt(query.split("=")[1]);
            contactoDAO.eliminar(id);
            respuesta = "{\"mensaje\":\"contacto eliminado\"}";
        }

        exchange.sendResponseHeaders(200, respuesta.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(respuesta.getBytes());
        os.close();
    }

    // Función auxiliar para extraer un campo de un JSON simple
    static String extraerCampo(String json, String campo) {
        try {
            return json.split("\"" + campo + "\":\"")[1].split("\"")[0];
        } catch (Exception e) {
            return "";
        }
    }
}