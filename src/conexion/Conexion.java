package conexion;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {

	public static Connection getConexion() {
		Connection conexion = null;
		var baseDatos = "beauty_center";
		var url = "jdbc:mysql://gateway01.us-west-2.prod.aws.tidbcloud.com:4000/" + baseDatos + "?sslMode=VERIFY_IDENTITY";
		var usuario = "yZ3eFZ3ALGi2bjz.root";
		var password = "ia8UPjqjTfHEeeWl";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conexion = DriverManager.getConnection(url, usuario, password);
		} catch (Exception e) {
			System.out.println("Error al conectarse a la BD: " + e.getMessage());
		}
		return conexion;
	}

	public static void main(String[] args) {
		var conexion = Conexion.getConexion();

		if (conexion != null) {
			System.out.println("Conexion exitosa");
		} else {
			System.out.println("No se pudo");
		}
	}
}