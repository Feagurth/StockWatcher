package com.google.gwt.sample.stockwatcher.client;

import java.io.Serializable;

/**
 * Clase para contener la excepción generada al añadir una acción marcada como
 * no listada
 * 
 * Implementa serializable para poder enviar la información de la excepción
 * desde el servidor al cliente
 */
public class DelistedException extends Exception implements Serializable {

	/**
	 * Id generado aleatoriamente
	 */
	private static final long serialVersionUID = -4705238702371892767L;

	/**
	 * Variable para almacenar el símbolo de la acción que ha producido la
	 * excepción
	 */
	private String symbol;

	/**
	 * Constructor básico de la clase necesario para poder serializar la
	 * excepción
	 */
	public DelistedException() {
	}

	/**
	 * Constructor de la clase
	 * 
	 * @param symbol
	 *            Símbolo de la acción que ha generado la excepción
	 */
	public DelistedException(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * Función que nos permite recuperar el símbolo de la acción que ha generado
	 * la excepción
	 * 
	 * @return String El símbolo de la acción que ha generado la excepción
	 */
	public String getSymbol() {
		return this.symbol;
	}

}
