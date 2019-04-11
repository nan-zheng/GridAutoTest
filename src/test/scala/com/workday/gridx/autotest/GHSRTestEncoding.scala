package com.workday.gridx.autotest

import org.junit.runner.RunWith
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GHSRTestEncoding extends FunSuite with Matchers {
  val termUnicodeOpenSource = "开源"
  val termContainsSpecialChar = "git%hub"

  val perPage: Int = 5

  /**
    * This Test is to test Unicode characters can be processed correctly in request and responses.
    *
    */
  test("Test Unicode characters can be correctly processed in the request and responses") {
    val request = new GitHubRepositorySearchRequest(termUnicodeOpenSource)
    request.setPerPage(perPage)

    val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](request.buildRequest)

    res.items.size should be(perPage)
    res.items.exists(x => x.description.contains(termUnicodeOpenSource))
  }

  /**
    * This Test is to test Special characters can be processed correctly in request and responses.
    *
    */
  test("Test with special characters can be correctly processed in the request and responses") {
    val request = new GitHubRepositorySearchRequest(termContainsSpecialChar)
    request.setPerPage(perPage)

    val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](request.buildRequest)

    res.items.size should be(perPage)
    res.items.exists(x => x.description.contains(termContainsSpecialChar))
  }

}
