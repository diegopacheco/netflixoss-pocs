package com.github.diegopacheco.java.sanbox.cassandradatastax.main;

import java.util.UUID;

import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;

@Accessor
public interface UserAccessor {
	
	 @Query("SELECT * FROM users WHERE user_id=:userId")
     public User findByID(@Param("userId") UUID userId);
	
	 @Query("SELECT * FROM users")
     public Result<User> getAll();
	 
}
