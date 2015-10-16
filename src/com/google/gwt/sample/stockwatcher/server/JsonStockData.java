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
	 * Id generado automáticametne
	 */
	private static final long serialVersionUID = 8491800536020050397L;

	// Constante para almacenar el precio máximo que van a alcanzar las acciones
	private static final double MAX_PRICE = 100.0;

	// Constante para almacenar el cambio máximo de precio permitdo para las
	// acciones
	private static final double MAX_PRICE_CHANGE = 0.02;

	/**
	 * Función que nos permite recuperar valores para las acciones
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// Creamos un objeto para generar números aleatorios
		Random rnd = new Random();

		// Creamos un objeto para escribir los resultados
		PrintWriter out = resp.getWriter();

		// Imprimimos el primer carácter de la respuesta JSON, que en este caso
		// se definirá como un array de objetos StockPrice
		out.println('[');

		// Recuperamos los símbolos que identifican las acciones accediendo a
		// los parametos de la petición identificados por 'q' y convirtiéndolos
		// en un array dividiendolos por sus espacios en blanco
		String[] stockSymbols = req.getParameter("q").split(" ");

		// Creamos una variable de control
		boolean firstSymbol = true;

		// Iteramos por todos los símbolos de las acciones
		for (String stockSymbol : stockSymbols) {

			// Calculamos el precio multiplicando un número decimal aleatorio
			// por el precio máximo permitido para las acciones
			double price = rnd.nextDouble() * MAX_PRICE;

			// Calculamos el cambio precio, multiplicando el precio por el
			// maximo cambio de precio permitido y multiplicando el resultado
			// por un número decimal aleatorio comprendido entre -1 y +1
			double change = price * MAX_PRICE_CHANGE
					* (rnd.nextDouble() * 2f - 1f);

			// Verificamos si este es el primer símbolo que vamos a mostrar
			if (firstSymbol) {
				// De ser así, cambiamos la variable de control
				firstSymbol = false;
			} else {
				// Si no es así, escribimos una coma como separador
				out.println("  ,");
			}

			// Abrimos una llave para construir la estructura que almacenará la
			// información de la acción
			out.println("  {");

			// Escribimos el literal del caracter con dos puntos como separador
			out.print("    \"symbol\": \"");

			// Escribimos el valor del símbolo
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
			// información de una acción
			out.println("  }");
		}

		// Escribimos la llave de cierre, finalizando la definición del array de
		// información sobre el precio de las acciones
		out.println(']');

		// Imprimimos el reto de caracteres que pudiesen quedar en el buffer
		out.flush();
	}

}