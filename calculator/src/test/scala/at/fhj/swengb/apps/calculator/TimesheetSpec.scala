package at.fhj.swengb.apps.calculator

import java.nio.file.{Files, Path, Paths}

import org.scalatest.WordSpecLike


class TimesheetSpec extends WordSpecLike {
  val content = "asdf"
  val condition: Boolean = ???
  "timesheet-calculator" should { //Testsuite
    "timesheet-calculator" in { //Test
      val p: Path = Paths.get("C:\\workspace\\fhj.swengb2017.assignments\\calculator\\timesheet-calculator.adoc")
      Files.readAllLines(p)
      assert (condition)
    }
    /*"work 2" in {

    }
    "work 3" in {

    }
    "work 4" in {

    }*/
  }
}
