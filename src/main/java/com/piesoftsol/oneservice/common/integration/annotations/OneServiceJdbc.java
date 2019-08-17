package com.piesoftsol.oneservice.common.integration.annotations;

import static com.piesoftsol.oneservice.common.integration.util.CommonConstants.ONESERVICE_JDBC_TEMPLATE;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Retention(RUNTIME)
@Target(FIELD)
@Autowired(required=false)
@Qualifier(ONESERVICE_JDBC_TEMPLATE)
public @interface OneServiceJdbc {

}
