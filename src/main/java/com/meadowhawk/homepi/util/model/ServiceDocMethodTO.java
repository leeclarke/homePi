/**
 * 
 */
package com.meadowhawk.homepi.util.model;

/**
 *
 * @author lee
 */
public class ServiceDocMethodTO {
	private String endPointName;
	private String endPointRequestType;
	private String endPointPath;
	private String endPointDescription;
	private String[] endPointProvides;
	private String[] sampleLinks;
	private String[] consumes;
	private String[] errors;
	
	/**
	 * @return the endPointRequestType
	 */
	public String getEndPointRequestType() {
		return endPointRequestType;
	}
	/**
	 * @param endPointRequestType the endPointRequestType to set
	 */
	public void setEndPointRequestType(String endPointRequestType) {
		this.endPointRequestType = endPointRequestType;
	}
	/**
	 * @return the endPointPath
	 */
	public String getEndPointPath() {
		return endPointPath;
	}
	/**
	 * @param endPointPath the endPointPath to set
	 */
	public void setEndPointPath(String endPointPath) {
		this.endPointPath = endPointPath;
	}
	/**
	 * @return the endPointDescription
	 */
	public String getEndPointDescription() {
		return endPointDescription;
	}
	/**
	 * @param endPointDescription the endPointDescription to set
	 */
	public void setEndPointDescription(String endPointDescription) {
		this.endPointDescription = endPointDescription;
	}
	/**
	 * @return the endPointProvides
	 */
	public String[] getEndPointProvides() {
		return endPointProvides;
	}
	/**
	 * @param endPointProvides the endPointProvides to set
	 */
	public void setEndPointProvides(String[] endPointProvides) {
		this.endPointProvides = endPointProvides;
	}
	/**
	 * @return the sampleLinks
	 */
	public String[] getSampleLinks() {
		return sampleLinks;
	}
	/**
	 * @param sampleLinks the sampleLinks to set
	 */
	public void setSampleLinks(String[] sampleLinks) {
		this.sampleLinks = sampleLinks;
	}
	public void setEndPointName(String endPointName) {
		this.endPointName = endPointName;
	}
	public String getEndPointName() {
		return endPointName;
	}
	public void setConsumes(String[] consume) {
		this.consumes = consume;		
	}
	public String[] getConsumes() {
		return consumes;
	}
	public String[] getErrors() {
		return errors;
	}
	public void setErrors(String[] errors) {
		this.errors = errors;
	}
}
