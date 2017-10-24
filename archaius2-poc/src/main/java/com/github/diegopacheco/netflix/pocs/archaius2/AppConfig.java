package com.github.diegopacheco.netflix.pocs.archaius2;

import com.netflix.archaius.api.annotations.Configuration;
import com.netflix.archaius.api.annotations.DefaultValue;
import com.netflix.archaius.api.annotations.PropertyName;

@Configuration(prefix = "app")
public interface AppConfig {
	
    @DefaultValue("true")
    @PropertyName(name = "has.persistence")
    public boolean isPersistenceEnable();
	
    @DefaultValue("app_test")
    @PropertyName(name = "name.simple")
    public String getNameSimple();
    
    @DefaultValue("???")
    @PropertyName(name = "JAVA.HOME")
    public String getJavaHome();
    
}
