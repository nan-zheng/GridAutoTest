package com.workday.gridx.autotest

object TestUtil {

  /**
    * This method is used to test the sorting of the result
    */

  def isSorted[T](list: List[T])(implicit ord: Ordering[T]): Boolean = list match {
    case Nil => true // an empty list is sorted
    case x :: Nil => true // a single-element list is sorted
    case x :: xs => ord.lteq(x, xs.head) && isSorted(xs) // if the first two elements are ordered and the rest are sorted, the full list is sorted too
  }

  /**
    *
    * This method is used to test if there is any duplications in the result
    */

  def isDuplicated[T](list: List[T])(implicit ord: Ordering[T]): Boolean = {
    list.distinct.size == list.size
  }
}
