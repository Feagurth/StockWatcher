package com.google.gwt.sample.stockwatcher.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Clase para generar valores para las acciones
 */
public class JsonStockData extends HttpServlet {

	/**
	 * Id generado autom�ticametne
	 */
	private static final long serialVersionUID = 8491800536020050397L;

	// Constante para almacenar el precio m�ximo que van a alcanzar las acciones
	private static final double MAX_PRICE = 100.0;

	// Constante para almacenar el cambio m�ximo de precio permitdo para las
	// acciones
	private static final double MAX_PRICE_CHANGE = 0.02;

	/**
	 * Funci�n que nos permite recuperar valores para las acciones
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// Creamos un objeto para generar n�meros aleatorios
		Random rnd = new Random();

		// Creamos un objeto para escribir los resultados
		PrintWriter out = resp.getWriter();

		// Imprimimos el primer car�cter de la respuesta JSON, que en este caso
		// se definir� como un array de objetos StockPrice
		out.println('[');

		// Recuperamos los s�mbolos que identifican las acciones accediendo a
		// los parametos de la petici�n identificados por 'q' y convirti�ndolos
		// en un array dividiendolos por sus espacios en blanco
		String[] stockSymbols = req.getParameter("q").split(" ");

		// Creamos una variable de control
		boolean firstSymbol = true;

		// Iteramos por todos los s�mbolos de las acciones
		for (String stockSymbol : stockSymbols) {

			// Calculamos el precio multiplicando un n�mero decimal aleatorio
			// por el precio m�ximo permitido para las acciones
			double price = rnd.nextDouble() * MAX_PRICE;

			// Calculamos el cambio precio, multiplicando el precio por el
			// maximo cambio de precio permitido y multiplicando el resultado
			// por un n�mero decimal aleatorio comprendido entre -1 y +1
			double change = price * MAX_PRICE_CHANGE
					* (rnd.nextDouble() * 2f - 1f);

			// Verificamos si este es el primer s�mbolo que vamos a mostrar
			if (firstSymbol) {
				// De ser as�, cambiamos la variable de control
				firstSymbol = false;
			} else {
				// Si no es as�, escribimos una coma como separador
				out.println("  ,");
			}

			// Abrimos una llave para construir la estructura que almacenar� la
			// informaci�n de la acci�n
			out.println("  {");

			// Escribimos el literal del caracter con dos puntos como separador
			out.print("    \"symbol\": \"");

			// Escribimos el valor del s�mbolo
			out.print(stockSymbol);

			// Separamos por una coma cada uno de las propiedades
			out.println("\",");

			// Escribimos el literal del precio separado por dos puntos
			out.print("    \"price\": ");

			// Escribimos el precio
			out.print(price);

			// Separamos por una coma cada uno de las propiedades
			out.println(',');

			// Escribimos el literal del cambio separado por dos puntos
			out.print("    \"change\": ");

			// Escribimos el valor del cambio
			out.println(change);

			// Cerramos la llave para finalizar la estructura que almacena la
			// informaci�n de una acci�n
			out.println("  }");
		}

		// Escribimos la llave de cierre, finalizando la definici�n del array de
		// informaci�n sobre el precio de las acciones
		out.println(']');

		// Imprimimos el reto de caracteres que pudiesen quedar en el buffer
		out.flush();
	}

}