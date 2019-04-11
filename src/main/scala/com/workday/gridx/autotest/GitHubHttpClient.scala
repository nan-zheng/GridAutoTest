package com.workday.gridx.autotest

object GitHubHttpClient {
  private lazy val client = new GitHubHttpClient
  def getInstance(): GitHubHttpClient = client
}

sealed class GitHubHttpClient extends SimpleHttpClient {
  val cred = Option(BasicAuthCred("api-testaccount", "testtest.1*"))

  def get[T](uri: String)(implicit m: Manifest[T]): T = {
    println(uri)
    val res = GitHubHttpClient.getInstance.getString(uri, cred)
    JasonUtils.fromJson[T](res)(m)
  }
}