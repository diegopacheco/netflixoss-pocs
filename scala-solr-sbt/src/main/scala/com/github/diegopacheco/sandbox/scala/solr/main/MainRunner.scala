package com.github.diegopacheco.sandbox.scala.solr.main

object MainRunner extends App {
    
    import jp.sf.amateras.solr.scala._
    
    val client = new SolrClient("http://localhost:8983/solr")
    println("Client: " + client)
    
    val result = client.query("/").getResultAsMap()
    println("Result: " + result)
  
}