package com.gezishu.akka.simple

import java.util.concurrent.Executors
import scala.concurrent.Await
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.Promise

object FutureTest extends App {

  implicit val ec = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())

  /**
   * Akka中的一个常见用例是在不需要使用 Actor的情况下并发地执行计算.
   * 如果你发现你只是为了并行地执行一个计算而创建了一堆 Actor, 下面是一种更好（也更快）的方法:
   */
  val f1 = Future {
    "Hello" + "World"
  }

  val f2 = f1 map {
    x => x.length
  }

  val result1 = Await.result(f2, 1 seconds)
  println(result1)

  val f3 = Future.successful(3)

  val f4 = f1 map {
    x =>
      f3 map {
        y => x.length() * y
      }
  }
//  val result2 = Await.result(f4, 1 second)
//  println(result2.value)
  f4 foreach println

  val f5 = f1 flatMap {
    x =>
      f3 map {
        y =>
          x.length() * y
      }
  }
//  val result3 = Await.result(f5, 1 second)
//  println(result3)
  f5 foreach println

  /**
   * 由于 Future 拥有 map, filter 和 flatMap 方法，它可以方便地用于 ‘for comprehension’:
   */
  val f6 = for {
    a ← Future(10 / 2) // 10 / 2 = 5
    b ← Future(a + 1) // 5 + 1 = 6
    c ← Future(a - 1) // 5 - 1 = 4
    if c > 3 // Future.filter
  } yield b * c // 6 * 4 = 24
  // 注意future a, b, c
  // 不是并发执行的
  val result4 = Await.result(f6, 1 second)
  println(result4)
//  f6 foreach println

  /**
   * 当我们知道Actor数量的时候上面的方法就足够了，但是当Actor数量较大时就显得比较笨重。 
   * sequence 和 traverse 两个辅助方法可以帮助处理更复杂的情况。 
   * 这两个方法都是用来将 T[Future[A]] 转换为 Future[T[A]]（T 是 Traversable子类）
   * 
   * Future.sequence 将输入的 List[Future[Int]] 转换为 Future[List[Int]]. 
   * 这样我们就可以将 map 直接作用于 List[Int], 从而得到 List 的总和.
   */
  val listOfFutures = Future.sequence((1 to 100).toList.map(x => Future{x * 2 - 1}))
  val sum1 = Await.result(listOfFutures.map(_.sum),1 second).asInstanceOf[Int]
  println(sum1)
  
  /**
   * traverse 方法与 sequence 类似, 但它以 T[A] 和 A => Future[B] 函数为参数返回一个 Future[T[B]],
   *  这里的 T 同样也是 Traversable 的子类. 例如, 用 traverse 来计算前100个奇数的和:
   *  用 traverse 会快一些，因为它不用创建一个 List[Future[Int]] 临时量.
   */
  val futuresList = Future.traverse((1 to 100).toList)(x => Future{x * 2 -1})
  val sum2 = Await.result(futuresList.map(_.sum), 1 second)
  println(sum2)
  
  /**
   * 方法 fold，它的参数包括一个初始值 , 一个 Future序列和一个 作用于初始值和Future类型返回与初始值相同类型的函数, 
   * 它将这个函数异步地应用于future序列的所有元素，它的执行将在最后一个Future完成之后开始。
   */
  val f7 = for (i <- 1 to 1000) yield Future {i * 2}
  val f8 = Future.fold(f7)(0)(_ + _)
  val sum3 = Await.result(f8, 1 second)
  println(sum3)
  
  /**
   * 如果传给 fold 的序列是空的, 它将返回初始值, 在上例中，这个值是0. 
   * 有时你并不需要一个初始值，而使用序列中第一个已完成的Future的值作为初始值，你可以使用 reduce, 它的用法是这样的:
   */
  val f9 = Future.reduce(f7)(_ + _)
  val sum4 = Await.result(f9, 1 second)
  println(sum4)
  
  ec.shutdown()
}