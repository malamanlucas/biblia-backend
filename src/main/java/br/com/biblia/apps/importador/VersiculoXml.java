package br.com.biblia.apps.importador;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlValue;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class VersiculoXml {
	
	@XmlValue
	private String content;
	
}
