/**
 * 
 */
package net.fluance.app.data.model.identity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("serial")
public class GrantedCompany implements Serializable {

	private Integer id;
	private String code;
	@JsonProperty("staffids")
	private List<CompanyStaffId> staffIds;
	private List<PatientUnit> patientunits;
	private List<HospService> hospservices;
	
	public GrantedCompany() {
		this(null, null, new ArrayList<PatientUnit>(), new ArrayList<HospService>(), new ArrayList<CompanyStaffId>());
	}
	
	/**
	 * @param id
	 * @param code
	 * @param patientunits
	 * @param hospservices
	 */
	public GrantedCompany(Integer id, String code, List<PatientUnit> patientunits, List<HospService> hospservices) {
		this(id, code, patientunits, hospservices, new ArrayList<CompanyStaffId>());
	}

	/**
	 * @param id
	 * @param code
	 * @param patientunits
	 * @param hospservices
	 * @param staffIds
	 */
	public GrantedCompany(Integer id, String code, List<PatientUnit> patientunits, List<HospService> hospservices,
			List<CompanyStaffId> staffIds) {
		super();
		this.id = id;
		this.code = code;
		this.patientunits = patientunits;
		this.hospservices = hospservices;
		this.staffIds = staffIds;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * @return the patientunits
	 */
	public List<PatientUnit> getPatientunits() {
		return patientunits;
	}
	
	/**
	 * @param patientunits the patientunits to set
	 */
	public void setPatientunits(List<PatientUnit> patientunits) {
		this.patientunits = patientunits;
	}
	
	/**
	 * @return the hospservices
	 */
	public List<HospService> getHospservices() {
		return hospservices;
	}
	
	/**
	 * @param hospservices the hospservices to set
	 */
	public void setHospservices(List<HospService> hospservices) {
		this.hospservices = hospservices;
	}

	/**
	 * @return the staffIds
	 */
	public List<CompanyStaffId> getStaffIds() {
		return staffIds;
	}

	/**
	 * @param staffIds the staffIds to set
	 */
	public void setStaffIds(List<CompanyStaffId> staffIds) {
		this.staffIds = staffIds;
	}

	public boolean addPatientUnit(PatientUnit unit) {
		return patientunits.add(unit);
	}
	
	public boolean addHospService(HospService service) {
		return hospservices.add(service);
	}
	
}
