package br.com.biblia.apps.importador;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@XmlRootElement(name="chapter")
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class CapituloXml {
	
	@XmlAttribute(name="id")
	private String id; 
	
	@XmlElement(name="p")
	private List<VersiculoXml> versiculos;
	
}
