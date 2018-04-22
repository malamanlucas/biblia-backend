package br.com.biblia.apps.importador;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class VersiculoXml {

	@XmlValue
	private String content;
	
	@XmlAttribute(name="osisID")
	private String osisID;
	
	public Integer getVersiculoId() {
		return Integer.valueOf(this.osisID.substring(this.osisID.lastIndexOf('.')+1));
	}
	
}
