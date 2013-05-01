package com.meadowhawk.homepi.util.model;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Path;

/**
 * Container for structuring Service Documentation. 
 * @author lee
 */
public class ServiceDocTO {
	private String serviceName;
	private String serviceDescription;

	private List<ServiceDocMethodTO> methodDocs = new ArrayList<ServiceDocMethodTO>();
	private String servicePath;
	private Class<?> clazz; 
	
	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	/**
	 * @return the serviceDescription
	 */
	public String getServiceDescription() {
		return serviceDescription;
	}
	/**
	 * @param serviceDescription the serviceDescription to set
	 */
	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}
	/**
	 * @return the methodDocs
	 */
	public List<ServiceDocMethodTO> getMethodDocs() {
		return methodDocs;
	}
	/**
	 * @param methodDocs the methodDocs to set
	 */
	public void setMethodDocs(List<ServiceDocMethodTO> methodDocs) {
		this.methodDocs = methodDocs;
	}

	public void setServicePath(String path) {
		this.servicePath = path;
	}
	
	/**
	 * @return the servicePath
	 */
	public String getServicePath() {
		return servicePath;
	}
	
	public void setClass(Class<?> servClass) {
		this.clazz = servClass;
	}

	public Class<?> setClass() {
		return this.clazz;
	}
	
}
