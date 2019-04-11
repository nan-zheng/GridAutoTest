package com.workday.gridx.autotest

import java.lang

import org.scalatest.{FunSuite, Matchers}
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

import scala.util.{Try, control}


@RunWith(classOf[JUnitRunner])
class GHSRNagativeTest extends FunSuite with Matchers {

  /**
    * This Test is to test when the given parameter is missing, an exception is thrown.
    *
    */
  test("Test with empty parameters ") {
    // val totalPage: Int = 5

    val request = new GitHubRepositorySearchRequest("")

    try {
      val uri = request.buildRequest()
      val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](uri)
    }

    catch {
      case ex: Exception => assert("invalid request ...".equals(ex.getMessage)) // problem with this line
    }
  }

  test("Build query with Order but without Sort ") {
    val pagetotal: Int = 3

    val request = new GitHubRepositorySearchRequest("something")
    request.setLanguage("Java")
    request.setOrder("desc")
    request.setPerPage(pagetotal)

    val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](request.buildRequest)

    res.items.size should be(pagetotal)
    res.incomplete_results should be(false)
  }

  test("Set bad sort key word should still work") {
    val total: Int = 20

    val request = new GitHubRepositorySearchRequest("scala")
    request.setLanguage("Java")
    request.setSort("somethingBad")
    request.setPerPage(total)

    val uri = request.buildRequest()
    val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](uri)

    res.items.size should be(total)
  }

  test(" 0 per_page result means back to default settings of 30 ") {
    val total: Int = 30

    val request = new GitHubRepositorySearchRequest("scala")
    request.setLanguage("Java")
    request.setSort("stars")
    request.setPerPage(0)

    val uri = request.buildRequest()
    val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](uri)

    res.items.size should be(total)
  }

  test(" 101 per_page result means back to max settings of 100 ") {
    val total: Int = 100

    val request = new GitHubRepositorySearchRequest("scala")
    request.setLanguage("Java")
    request.setSort("stars")
    request.setPerPage(101)

    val uri = request.buildRequest()
    val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](uri)

    res.items.size should be(total)
  }

  test("Test empty responses ") {
    val request = new GitHubRepositorySearchRequest("something doesn't existed asfsgfdglsmal;amf;smf")
    request.setSort("forks")
    request.setOrder("desc")
    request.setPerPage(5)

    val uri = request.buildRequest()
    val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](uri)

    res.items.size should be(0)
    res.total_count == 0
  }

  test("Bad page number will return error") {
    val request = new GitHubRepositorySearchRequest("scala")
    request.setLanguage("Java")
    request.setPerPage(100)
    request.setPage(5000)

    val uri = request.buildRequest()

    Option(GitHubHttpClient.getInstance.get[RepositorySearchResult](uri)) match {
      case Some(RepositorySearchResult(0, false, null)) => // expected
      case _ => fail("Unexpected successful")
    }
  }


  /**
    * This is to test special charaters in the request and responses.
    */
  test("Bad Url should return error") {
    val request = GitHubHttpClient.getInstance.getString("https://api.github.com/search/repositories?p=234")
    request.contains("Validation Failed") should be(true)
  }

  /**
    * This is to test special charaters in the request and responses.
    */
  /*test("Run too fast should fail without Auth 10 per min") {
    (0 to 11).foreach(_ => {
      val request = GitHubHttpClient.getInstance.getString("https://api.github.com/search/repositories?p=234")
      request.contains("Validation Failed") should be(true)
    })
  }

  test("Run too fast should fail with Auth 30 per min") {
    (0 to 31).foreach(_ => {
      val request = new GitHubRepositorySearchRequest(termUnicodeOpenSource)
      request.setPerPage(perPage)

      val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](request.buildRequest)
    })
  }
  */
}