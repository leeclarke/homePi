package com.meadowhawk.homepi.model;

import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Provides common method for determining if data should be masked when rendered.
 * @author lee
 */
public abstract class MaskableDataObject {
	@JsonIgnore
	@Transient
	private boolean maskedView = false;

	public boolean isMaskedView() {
		return maskedView;
	}

	public void setMaskedView(boolean maskView) {
		this.maskedView = maskView;
	}
	

}
