package com.workday.gridx.autotest

import org.apache.http.client.methods.RequestBuilder

import scala.collection.mutable.ListBuffer

trait GitHubRequest {

  val fields= scala.collection.mutable.Map[String, String]()
  val qualifiers = new ListBuffer[String]()

  def isNullOrEmpty(s:String):Boolean = s== null || s.isEmpty

  def buildRequest(): String = {
    val request = RequestBuilder.get().setUri(s"https://api.github.com/$getAPIUri")
    val mergedQualifiers = qualifiers.mkString(" ")
    val termAndQualifiers = if (isNullOrEmpty(getTerm())) {
      mergedQualifiers }
     else {
      if (isNullOrEmpty(mergedQualifiers)) getTerm() else getTerm + " " + mergedQualifiers
    }
    request.addParameter("q", termAndQualifiers)
    fields.foreach(f => request.addParameter(f._1, f._2))
    request.build().getURI.toASCIIString
  }

  def addQualifier(name: String, value: String): Unit = {
    if (!isNullOrEmpty(value)) qualifiers += s"$name:$value"
  }

  def setSort(sort: String): Unit = {
    fields += ("sort" -> sort)
  }

  def setOrder(order: String): Unit = {
    fields += ("order" -> order)
  }

  def setPerPage(perPage: Int): Unit = {
    fields += ("per_page" -> String.valueOf(perPage))
  }

  def setPage(page: Int): Unit = {
    fields += ("page" -> String.valueOf(page))
  }

  def getTerm(): String

  def getAPIUri(): String
}

