package com.piesoftsol.oneservice.common.integration.util;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import com.piesoftsol.oneservice.common.integration.annotations.EnableJdbc;
import com.piesoftsol.oneservice.common.integration.annotations.EnableNoDataBase;

import static com.piesoftsol.oneservice.common.integration.config.OneServiceInit.oneServiceBootClass;

public class NoDataBaseCondition implements Condition {
	
	private static final String METHOD = "NoDataBaseCondition";
	
	private static final AppLogger LOGGER = new AppLogger(NoDataBaseCondition.class.getName());

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		
		String message = "";
		if(oneServiceBootClass == null) {
			return false;
		}

		try {
			if (oneServiceBootClass.isAnnotationPresent(EnableNoDataBase.class)) {
				if (oneServiceBootClass.isAnnotationPresent(EnableJdbc.class)) {
					message = "EnableNoDataBase can't combined with Anyother Database methods. Please removed other database Methods.";
					LOGGER.error(METHOD, message);
					throw new IllegalArgumentException(message);
				}else {
					return true;
				}
			}
		}catch(IllegalArgumentException e) 
	    {
	        try {
				throw e;
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(-100);
			} // rethrowing the exception 
	    }
		
		return false;
	}

}
