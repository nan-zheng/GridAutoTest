package com.workday.gridx.autotest


object GitHubHttpClient {
  private lazy val client = new GitHubHttpClient
  def getInstance(): GitHubHttpClient = client
}

sealed class GitHubHttpClient extends SimpleHttpClient {
  def get[T](uri: String)(implicit m: Manifest[T]): T = {
    println(uri)
    val res = GitHubHttpClient.getInstance.getString(uri)
    JasonUtils.fromJson[T](res)(m)
  }
}