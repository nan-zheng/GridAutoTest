package com.workday.gridx.autotest

import org.junit.runner.RunWith
import org.scalatest.{FunSuite, Matchers}
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GHSRParameterTest extends FunSuite with Matchers {
  val perPage5: Int = 5
  val perPage10: Int = 10

  val termGitHub = "github"
  val languageScala = "scala"



  /**
    * This Test is to test
    * 1. Language search works
    * 2. order by ASC works
    * 3. Sort works
    *
    */
  test("Test key word in scala language and sort by DESC with 5 per page") {
    val request = new GitHubRepositorySearchRequest(termGitHub)
    request.setLanguage("scala")
    request.setSort("forks")
    request.setOrder("desc")
    request.setPerPage(perPage5)

    val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](request.buildRequest)

    res.items.size should be(perPage5)

    TestUtil.isSorted(res.items.map(_.forks_count).reverse) should be(true)
    res.items.foreach(_.description.toLowerCase().contains(termGitHub) should be(true))
  }


  /**
    * Test Sort by forks with asc should work
    */
  test("Sort by forks with asc should work") {
    val request = new GitHubRepositorySearchRequest(termGitHub)
    request.setPerPage(perPage10)
    request.setSort("forks")
    request.setOrder("asc")
    val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](request.buildRequest)

    res.items.size should be(perPage10)
    TestUtil.isSorted(res.items.map(_.forks_count)) should be(true)
  }

  /**
    * Sort by forks with desc should work
    */
  test("Sort by forks with desc should work") {
    val request = new GitHubRepositorySearchRequest(termGitHub)
    request.setPerPage(perPage10)
    request.setSort("forks")
    request.setOrder("desc")
    val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](request.buildRequest)

    res.items.size should be(perPage10)
    TestUtil.isSorted(res.items.map(_.forks_count).reverse) should be(true)
  }

  /**
    * Test Sort by stars with asc should work
    */
  test("Sort by stars with asc should work") {
    val request = new GitHubRepositorySearchRequest(termGitHub)
    request.setPerPage(perPage10)
    request.setSort("stars")
    request.setOrder("asc")
    val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](request.buildRequest)

    res.items.size should be(perPage10)
    TestUtil.isSorted(res.items.map(_.stargazers_count)) should be(true)
  }

  /**
    * Sort by stars with desc should work
    */
  test("Sort by stars with desc should work") {
    val request = new GitHubRepositorySearchRequest(termGitHub)
    request.setPerPage(perPage10)
    request.setSort("stars")
    request.setOrder("desc")
    val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](request.buildRequest)

    res.items.size should be(perPage10)
    TestUtil.isSorted(res.items.map(_.stargazers_count).reverse) should be(true)
  }

  /**
    * Set 10 pages should only return 10 pages
    */
  test("Set 10 pages should only return 10 pages") {
    val request = new GitHubRepositorySearchRequest(termGitHub)
    request.setPerPage(perPage10)
    val res = GitHubHttpClient.getInstance.get[RepositorySearchResult](request.buildRequest)

    res.items.size should be(perPage10)
  }
}