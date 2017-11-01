package com.github.diegopacheco.netflix.pocs.runtime.health;

import com.netflix.runtime.health.api.Health;
import com.netflix.runtime.health.api.HealthIndicator;
import com.netflix.runtime.health.api.HealthIndicatorCallback;

public class MyHealthIndicator implements HealthIndicator {
    
    @Override
    public void check(HealthIndicatorCallback healthCallback) {
        if ((Math.random()*10) <= 5.0) {
            healthCallback.inform(Health.unhealthy().withDetail("errorRate", " 2 - This is the error message!").build());
        }
        else {
            healthCallback.inform(Health.healthy().build());
        }
    }
}
