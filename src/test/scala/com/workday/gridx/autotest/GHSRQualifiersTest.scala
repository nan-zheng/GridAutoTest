package com.workday.gridx.autotest

import org.junit.runner.RunWith
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GHSRQualifiersTest extends FunSuite with Matchers {
  val perPage5: Int = 5
  val perPage10: Int = 5

  val termWithKeyWord = "do not use"
  val languageScala = "scala"
  val termGitHub = "git"

  /**
    * This Test is to test
    * 1. exclude key work "NOT" will not be affected by the exclude conditions
    * 2. Best matches by default
    */
  test("Test NOT key word with 5 per page") {
    val request = new GitHubRepositorySearchRequest(termWithKeyWord)
    request.setPerPage(perPage5)
    val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](request.buildRequest)

    res.items.size should be(perPage5)
    res.items.foreach(_.description.toLowerCase().contains(termWithKeyWord) should be(true))
  }


  /**
    * Test sort by Best matches, the top 5 should all contains common term
    */
  test("Sort by best matches should be by default enabled") {
    val request = new GitHubRepositorySearchRequest(termGitHub)
    request.setPerPage(perPage5)
    val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](request.buildRequest)

    res.items.size should be(perPage10)
    res.items.foreach(_.description.toLowerCase().contains(termGitHub) should be(true))
  }


  /**
    * Test set language works
    */
  test("Test key word in scala language with 10 per page") {
    val request = new GitHubRepositorySearchRequest(termGitHub)
    request.setLanguage("scala")
    request.setPerPage(perPage10)
    val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](request.buildRequest)

    res.items.size should be(perPage10)
    res.items.foreach(_.language.toLowerCase().contains(languageScala) should be(true))
  }

  /**
    * Test set user works with all user's repo return
    *
    */
  test("Test set user works with all user's repo return") {
    val request = new GitHubRepositorySearchRequest("")
    request.setUser("defunkt")
    request.setPerPage(perPage5)
    val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](request.buildRequest)

    res.items.size should be(perPage5)
    res.items.foreach(_.owner.login.toLowerCase().contains("defunkt") should be(true))
  }

  /**
    * Test set fork works with all fork = number repo returned
    *
    */
  test("Test set fork works with all fork = number repo return") {
    val request = new GitHubRepositorySearchRequest("")
    request.setForks("5")
    request.setPerPage(perPage5)
    val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](request.buildRequest)

    res.items.size should be(perPage5)
    res.items.foreach(_.forks_count should be(5))
  }

  /**
    * Repo name search should return result
    *
    */
  test("Repo name search should return result") {
    val request = new GitHubRepositorySearchRequest("")
    request.setRepo("Microsoft/vscode")
    request.setPerPage(perPage5)
    val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](request.buildRequest)

    res.items.size should not be(0)
  }
}
