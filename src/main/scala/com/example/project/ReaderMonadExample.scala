object ReaderMonadExample extends App {

import cats.data.Reader

// composition example
val upper = Reader((text: String) => text.toUpperCase)
val greet = Reader((name: String) => s"Hello, $name!")

val comb1 = upper.compose(greet)
val comb2 = upper.andThen(greet)

val res1 = comb1.run("Bob")

// dependency injection example
case class Course(descr: String, code: String)

class AuthService {
  def isAuthorized(username: String): Boolean = username.startsWith("J")
}

class CourseService {
  def register(course: Course, isAuthorized: Boolean, name: String) = {
    if (isAuthorized) { s"User $name registered for the course: ${course.code}" }
    else { s"User: $name is not authorised to register for course: ${course.code}" }
  }
}

case class CourseManager(course: Course, username: String, authService: AuthService, courseService: CourseService)

def isAuthorized = Reader[CourseManager, Boolean] { courseMngr => 
  courseMngr.authService.isAuthorized(courseMngr.username)
}

def register(isFull: Boolean) = Reader[CourseManager, String] { courseMngr => 
  courseMngr.courseService.register(courseMngr.course, isFull, courseMngr.username)
}

val result = for {
  authorized <- isAuthorized
  response <- register(authorized)
} yield response

val course = Course("Computer Science", "CS01")
val report = result.run(CourseManager(course, "John", new AuthService, new CourseService))

println(s"$report")

}