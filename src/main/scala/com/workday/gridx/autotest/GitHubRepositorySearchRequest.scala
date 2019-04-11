package com.workday.gridx.autotest

class GitHubRepositorySearchRequest(term: String) extends GitHubRequest {
  // https://help.github.com/en/articles/searching-for-repositories

  override def getAPIUri(): String = {
    "/search/repositories"
  }

  def setIn(v: String): Unit = addQualifier("in", v)

  def setSize(v: String): Unit = addQualifier("size", v)

  def setForks(v: String): Unit = addQualifier("forks", v)

  def setCreated(v: String): Unit = addQualifier("created", v)

  def setPushed(v: String): Unit = addQualifier("pushed", v)

  def setUser(v: String): Unit = addQualifier("user", v)

  def setRepo(v: String): Unit = addQualifier("repo", v)

  def setLanguage(v: String): Unit = addQualifier("language", v)

  def setStars(v: String): Unit = addQualifier("stars", v)

  def setTopic(v: String): Unit = addQualifier("topic", v)

  override def getTerm(): String = term
}
