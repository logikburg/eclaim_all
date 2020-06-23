package hk.org.ha.eclaim.model.maintenance;

public class DocumentWebVo {
	private Integer documentUid;
	private String documentDesc;
	private String documentType;
	private String recState;
	
	public int getDocumentUid() {
		return documentUid;
	}

	public void setDocumentUid(Integer documentUid) {
		this.documentUid = documentUid;
	}

	public String getDocumentDesc() {
		return documentDesc;
	}

	public void setDocumentDesc(String documentDesc) {
		this.documentDesc = documentDesc;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getRecState() {
		return recState;
	}

	public void setRecState(String recState) {
		this.recState = recState;
	}
}
