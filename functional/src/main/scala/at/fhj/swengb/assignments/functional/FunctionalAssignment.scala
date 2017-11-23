package at.fhj.swengb.assignments.functional

/**
  * In this assignment you have the chance to demonstrate basic understanding of
  * functions like map/filter/foldleft a.s.o.
  **/
object FunctionalAssignment {

  /**
    * A function which returns its parameters in a changed order. Look at the type signature.
    */
  def flip[A, B](t: (A, B)): (B, A) = t.swap

  /**
    * given a Seq[A] and a function f : A => B, return a Seq[B]
    */
  def unknown[A, B](as: Seq[A], fn: A => B): Seq[B] = as.map(fn)

  /**
    * Returns the absolute value of the parameter i.
    *
    * @param i a value, either with a positive or a negative sign.
    * @return
    */
  def abs(i: Int): Int = if (i < 0) -i else i


  // Describe with your own words what this function does.
  // in the comment below, add a description what this function does - in your own words - and give
  // the parameters more descriptive names.
  //
  // Afterwards, compare your new naming scheme with the original one.
  // What did you gain with your new names? What did you loose?
  // The function takes a sequence, a number(accumlator) and a function (that combines 2 values) as parameters. It
  // uses the function to combine all the elements of the list, starting with the accumulator and most left item.
  /**
    *
    * @param List //Sequence or List of Integers
    * @param accumulator  //The value the function starts off with and the number that keeps getting used as a parameter for the function
    * @param func //A function that combines 2 numbers (accumulator + a number from the list)
    * @tparam listitems //The individual numbers of the list
    * @tparam accvalue //The current value of the accumulator
    * @return
    */
  def op[listitems, accvalue](List: Seq[listitems], accumulator: accvalue)
                             (func: (accvalue, listitems) => accvalue): accvalue = List.foldLeft(accumulator)(func)

  /**
    * implement the summation of the given numbers parameter.
    * Use the function 'op' defined above.
    *
    * @param numbers //
    * @return
    */

  def sum(numbers: Seq[Int]): Int = op(numbers, 0)((a: Int, b: Int) => a + b)


  /**
    * calculate the factorial number of the given parameter.
    *
    * for example, if we pass '5' as parameter, the function should do the following:
    *
    * 5! = 5 * 4 * 3 * 2 * 1
    *
    * @param i parameter for which the factorial must be calculated
    * @return i!
    */
  def fact(i: Int): Int = if (i == 0) 1 else i * fact(i - 1)

  /**
    * compute the n'th fibonacci number
    *
    * hint: use a internal loop function which should be written in a way that it is tail
    * recursive.
    *
    * https://en.wikipedia.org/wiki/Fibonacci_number
    */
  def fib(n: Int): Int = if (n <= 0) 0 else if (n == 1) 1 else if (n == 2) 1 else fib(n - 1) + fib(n - 2)

  /**
    * Implement a isSorted which checks whether an Array[A] is sorted according to a
    * given comparison function.
    *
    * Implementation hint: you always have to compare two consecutive elements of the array.
    * Elements which are equal are considered to be ordered.
    */
  def isSorted[A](as: Array[A], gt: (A, A) => Boolean): Boolean = {
    def srt(n: Int): Boolean = {
      if (n >= as.length - 1) true
      else !(gt(as(n), as(n + 1)) && srt(n + 1))
    }

    srt(0)
  }


  /**
    * Takes both lists and combines them, element per element.
    *
    * If one sequence is shorter than the other one, the function stops at the last element
    * of the shorter sequence.
    */
  def genPairs[A, B](as: Seq[A], bs: Seq[B]): Seq[(A, B)] = as.zip(bs)

  // a simple definition of a linked list, we define our own list data structure
  sealed trait MyList[+A]

  case object MyNil extends MyList[Nothing]

  case class Cons[+A](head: A, tail: MyList[A]) extends MyList[A]

  // the companion object contains handy methods for our data structure.
  // it also provides a convenience constructor in order to instantiate a MyList without hassle
  object MyList {

    def sum(list: MyList[Int]): Int = list match {
      case MyNil => 0
      case Cons(head, tail) => head + sum(tail)
    }

    def product(list: MyList[Int]): Int = list match {
      case MyNil => 1
      case Cons(head, tail) => if (head != 0) head * product(tail) else 0
    }
        def apply[A](as: A*): MyList[A] = {
          if (as.isEmpty) MyNil
          else Cons(as.head, apply(as.tail: _*))
        }

    }
  }



