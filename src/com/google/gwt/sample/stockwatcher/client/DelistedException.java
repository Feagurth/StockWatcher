package com.google.gwt.sample.stockwatcher.client;

import java.io.Serializable;

/**
 * Clase para contener la excepci�n generada al a�adir una acci�n marcada como
 * no listada
 * 
 * Implementa serializable para poder enviar la informaci�n de la excepci�n
 * desde el servidor al cliente
 */
public class DelistedException extends Exception implements Serializable {

	/**
	 * Id generado aleatoriamente
	 */
	private static final long serialVersionUID = -4705238702371892767L;

	/**
	 * Variable para almacenar el s�mbolo de la acci�n que ha producido la
	 * excepci�n
	 */
	private String symbol;

	/**
	 * Constructor b�sico de la clase necesario para poder serializar la
	 * excepci�n
	 */
	public DelistedException() {
	}

	/**
	 * Constructor de la clase
	 * 
	 * @param symbol
	 *            S�mbolo de la acci�n que ha generado la excepci�n
	 */
	public DelistedException(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * Funci�n que nos permite recuperar el s�mbolo de la acci�n que ha generado
	 * la excepci�n
	 * 
	 * @return String El s�mbolo de la acci�n que ha generado la excepci�n
	 */
	public String getSymbol() {
		return this.symbol;
	}

}
