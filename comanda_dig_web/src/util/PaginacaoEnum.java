package util;

public enum PaginacaoEnum {
	
	HOME("/pages/init.xhtml")

	;
	
	private String value;
	
	
	PaginacaoEnum(String value){
		this.value = value;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}
	
	
}
