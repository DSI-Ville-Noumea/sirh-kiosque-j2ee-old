package nc.noumea.mairie.kiosque.abs.planning.vo;

import com.dhtmlx.planner.data.DHXCollection;

public class CustomDHXCollection extends DHXCollection {

	public String classNameTr;
	
	public CustomDHXCollection(String value, String label, boolean className) {
		super(value, label);
		if(className) {
			this.classNameTr = "linePair";
		}else{
			this.classNameTr = "lineImpair";
		}
	}

	public String getClassNameTr() {
		return classNameTr;
	}

	public void setClassNameTr(String classNameTr) {
		this.classNameTr = classNameTr;
	}

}
