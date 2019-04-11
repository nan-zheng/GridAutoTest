package com.workday.gridx.autotest

import com.workday.gridx.autotest.TestUtil.isSorted
import org.scalatest.{FunSuite, Matchers}


class GitHubAPITest extends FunSuite with Matchers {

  //curl https://api.github.com/search/repositories?q=tetris+language:assembly&sort=stars&order=desc

  val term_unicode = "955 不加班的公司名单"
  val term_special = "%20"


  /**
    * This Test is to test Unicode characters can be processed correctly in request and responses.
    *
    */
  test("Test Unicode characters can be correctly processed in the request and responses ") {
    val total: Int = 1

    val request = new GitHubRepositorySearchRequest(term_unicode)
    request.addSort("forks")
    request.addOrder("desc")
    request.addPerPage(5)

    val uri = request.buildRequest()
    val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](uri)

    res.items.size should be(total)

    isSorted(res.items.map(_.forks_count).reverse) should be(true)
    res.items.exists(x => x.description.contains(term_unicode))
  }

  /**
    * This is to test special charaters in the request and responses.
    * it failed because of the sorting order is not
    */
  test("Test Special characters can be correctly processed and sort by star count ") {

    val request = new GitHubRepositorySearchRequest(term_special)
    request.addSort("star")
    request.addOrder("desc")
    request.addPerPage(5)

    val uri = request.buildRequest()
    val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](uri)


    isSorted(res.items.map(_.stargazers_count).reverse) should be(true)
    res.items.exists(x => x.description.contains("%20"))
  }

  /**
    * This Test is to test the exclude key work "NOT" will not be affected by the
    *  exclude conditions
    *
    */
  test("Test NOT key word in scala language ") {
    // val totalPage: Int = 1

    val request = new GitHubRepositorySearchRequest("do not use")
    request.setLanguage("scala")
    request.addSort("forks")
    request.addOrder("desc")
    request.addPerPage(5)

    val uri = request.buildRequest()
    val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](uri)

   // res.items.size should be(totalPage)

    isSorted(res.items.map(_.forks_count).reverse) should be(true)
    res.items.foreach(_.description.toLowerCase().contains("do not use") should be(true))

  }
  /**
    * This Test is to test when the given parameter is missing, an exception is thrown.
    *
    */
  test("Test with empty parameters ") {
    // val totalPage: Int = 5

    val request = new GitHubRepositorySearchRequest("")

    try {
    val uri = request.buildRequest()
    val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](uri)}

    catch {
      case ex: Exception => assert("invalid request ...".equals(ex.getMessage))// problem with this line
    }


  }

  test("Build query with Order but without Sort ") {
     val total: Int = 8

    val request = new GitHubRepositorySearchRequest("something nice")
    request.setLanguage("Java")
    //request.addSort("forks")
    request.addOrder("desc")
    //request.addPerPage(5)

    val uri = request.buildRequest()
    val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](uri)

     res.items.size should be(total)
     res.total_count should be(total)
     res.incomplete_results should be(false)

    //isSorted(res.items.map(_.forks_count).reverse) should be(true)
    res.items.foreach(_.description.toLowerCase().contains("something nice") should be(true))

  }

  test(" 0 per_page result means back to default settings of 30 ") {
    val total: Int = 30

    val request = new GitHubRepositorySearchRequest("scala")
    request.setLanguage("Java")
    //request.addSort("forks")
    request.addOrder("desc")
    request.addPerPage(0)

    val uri = request.buildRequest()
    val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](uri)

    res.items.size should be(total)

    //isSorted(res.items.map(_.forks_count).reverse) should be(true)


  }

  test("Test empty responses ") {

    val request = new GitHubRepositorySearchRequest("something doesn't existed asfsgfdglsmal;amf;smf")
    request.addSort("forks")
    request.addOrder("desc")
    request.addPerPage(5)

    val uri = request.buildRequest()
    val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](uri)

    res.items.size should be(0)
    res.total_count == 0

  }

}