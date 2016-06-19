package com.qinqin.utls;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * 类型转换器 ，模拟对Calendar进行转换
 * @author qinqin11460
 *
 */

public class SingleValueCalendarConverter implements Converter  {

	@Override
	public boolean canConvert(Class type) {
		// TODO Auto-generated method stub
		return type.equals(GregorianCalendar.class);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		// TODO Auto-generated method stub
		Calendar calendar = (Calendar) source;
		
		writer.setValue(String.valueOf(calendar.getTime().getTime()));
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		// TODO Auto-generated method stub
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(new Date(Long.parseLong(reader.getValue())));
		return calendar;
	}
	
}
