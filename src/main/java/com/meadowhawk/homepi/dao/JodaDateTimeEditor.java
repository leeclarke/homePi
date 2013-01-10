package com.meadowhawk.homepi.dao;

import java.beans.PropertyEditorSupport;

import org.joda.time.DateTime;

/**
 * Supports JodaTimeBeanPropertyRowMapper override, providing conversion of sql.Timestamps into joda DateTime 
 * @author Lee.Clarke
 */
public class JodaDateTimeEditor extends PropertyEditorSupport {
	@Override
	public void setAsText(final String text) throws IllegalArgumentException {
		setValue(new DateTime(text)); // date time in ISO8601 format
										// (yyyy-MM-ddTHH:mm:ss.SSSZZ)
	}

	@Override
	public void setValue(final Object value) {
		super.setValue(value == null || value instanceof DateTime ? value : new DateTime(value));
	}

	@Override
	public DateTime getValue() {
		return (DateTime) super.getValue();
	}

	@Override
	public String getAsText() {
		return getValue().toString(); // date time in ISO8601 format
										// (yyyy-MM-ddTHH:mm:ss.SSSZZ)
	}

}
