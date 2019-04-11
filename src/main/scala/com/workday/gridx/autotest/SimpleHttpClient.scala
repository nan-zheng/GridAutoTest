package com.workday.gridx.autotest

import java.io.InputStream
import java.net.URI
import java.util.Base64

import org.apache.http.client.HttpClient
import org.apache.http.client.methods.{HttpGet, HttpRequestBase}
import org.apache.http.impl.client.HttpClientBuilder

trait Cred
case class BasicAuthCred(username: String, password:String) extends Cred

class SimpleHttpClient {
  private def createHttpClient(request: HttpRequestBase, cred: Option[Cred] = None, headers: Option[List[(String, String)]] = None): HttpClient = {

    val client = HttpClientBuilder.create.build

    /**
      * if cred is matched with test account from Github, use Basic Authentication,
      * otherwise, build the client request as unauthenticated
      *
      */
    cred match {
      case Some(c: BasicAuthCred) => request.addHeader("Authorization", "Basic " +
        Base64.getEncoder.encode((c.username + ':' + c.password).getBytes))
      case _ => // do nothing
    }

    /**
      *    build basic header + optional header fields
      *
      */
    headers match {
      case Some(list) => list.foreach(t => request.addHeader(t._1, t._2))
      case _ => // do nothing
    }

    client
  }

  /**
    *
    * @param endpoint string with end point details
    * @param cred optional user credentials api-testaccount: testtest.1*
    * @param headers optional field: "ACCEPT: application/vnd.github.mercy-preview+json"
    * @return InputStream of ???
    */
  def get(endpoint: String, cred: Option[Cred] = None, headers: Option[List[(String, String)]] = None): InputStream = {
    val uri = new URI(endpoint)
    val httpGet = new HttpGet(uri)

    val client = createHttpClient(httpGet, cred, headers)
    client.execute(httpGet).getEntity.getContent
  }

  def getString(endpoint: String, cred: Option[Cred] = None, headers: Option[List[(String, String)]] = None): String = {
    scala.io.Source.fromInputStream(get(endpoint, cred, headers)).mkString
  }
}
