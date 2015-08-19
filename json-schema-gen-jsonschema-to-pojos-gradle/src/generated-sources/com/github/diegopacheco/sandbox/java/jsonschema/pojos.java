
package com.github.diegopacheco.sandbox.java.jsonschema;

import java.util.HashSet;
import java.util.Set;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * Diego Event Schema
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "id",
    "name",
    "friends"
})
public class pojos {

    /**
     * id property
     * 
     */
    @JsonProperty("id")
    private String id;
    /**
     * name property
     * 
     */
    @JsonProperty("name")
    private String name;
    /**
     * friends property
     * 
     */
    @JsonProperty("friends")
    private Set<Object> friends = new HashSet<Object>();

    /**
     * id property
     * 
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * id property
     * 
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    public pojos withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * name property
     * 
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * name property
     * 
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public pojos withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * friends property
     * 
     */
    @JsonProperty("friends")
    public Set<Object> getFriends() {
        return friends;
    }

    /**
     * friends property
     * 
     */
    @JsonProperty("friends")
    public void setFriends(Set<Object> friends) {
        this.friends = friends;
    }

    public pojos withFriends(Set<Object> friends) {
        this.friends = friends;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
