package com.github.diegopacheco.java.sanbox.cassandradatastax.main;

import java.util.UUID;

import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.google.common.base.Objects;

@Table(name = "users", readConsistency="QUORUM", writeConsistency="QUORUM")
public class User {
	
	  public enum Gender { FEMALE, MALE }

      @PartitionKey
      @Column(name = "user_id")
      private UUID userId;

      @Column 
      private int year;

      private String name;
      private String email;

      public User() {}

      public User(String name, String email,int year) {
          this.userId = UUIDs.random();
          this.name = name;
          this.email = email;
          this.year = year;
      }

      public UUID getUserId() {
          return userId;
      }

      public void setUserId(UUID userId) {
          this.userId = userId;
      }

      public String getName() {
          return name;
      }

      public void setName(String name) {
          this.name = name;
      }

      public String getEmail() {
          return email;
      }

      public void setEmail(String email) {
          this.email = email;
      }

      public int getYear() {
          return year;
      }

      public void setYear(int year) {
          this.year = year;
      }

      @Override
      public boolean equals(Object other) {
          if (other == null || other.getClass() != this.getClass())
              return false;

          User that = (User)other;
          return Objects.equal(userId, that.userId)
              && Objects.equal(name, that.name)
              && Objects.equal(email, that.email)
              && Objects.equal(year, that.year);
      }

      @Override
      public int hashCode() {
          return Objects.hashCode(userId, name, email, year);
      }
      
	  @Override
	  public String toString() {
		 return  Objects.toStringHelper(getClass()).
				 add("name", getName()).
				 add("email", getEmail()).
				 add("year", getYear()).
				 toString();
      }
	
}
