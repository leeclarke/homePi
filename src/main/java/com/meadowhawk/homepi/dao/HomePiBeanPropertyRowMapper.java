package com.meadowhawk.homepi.dao;

import org.joda.time.DateTime;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.propertyeditors.CharacterEditor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * Overrides the Spring BeanPropertyRowMapper to add support for auto conversion of joda DateTime
 * @author Lee.Clarke
 *
 * @param <T>  Class
 */
public class HomePiBeanPropertyRowMapper<T> extends BeanPropertyRowMapper<T> {
	public HomePiBeanPropertyRowMapper() {
		super();
	}
	
	
	public HomePiBeanPropertyRowMapper(Class<T> mappedClass){
		super(mappedClass);
	}
	
	@Override
	protected void initBeanWrapper(BeanWrapper bw) {
		bw.registerCustomEditor(DateTime.class, new JodaDateTimeEditor());
		bw.registerCustomEditor(Character.class, new CharacterEditor(true));
	}
}
