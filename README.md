[![Build Status](https://travis-ci.org/jooby-project/kotlin-starter.svg?branch=master)](https://travis-ci.org/jooby-project/kotlin-starter)
# kotlin

Starter project for [Kotlin](http://kotlinlang.org/).

## quick preview

This project contains:

- A simple hello world application with an optional `name` parameter
- Integration tests using [Spek](http://spekframework.org)

[App.kt](https://github.com/jooby-project/kotlin-starter/blob/master/src/main/kotlin/starter/kotlin/App.kt):

```kotlin
import org.jooby.*

/**
 * Kotlin stater project.
 */
class App: Kooby({

  get {
    val name = param("name").value("Jooby")
    "Hello $name!"
  }

})


/**
 * Run application:
 */
fun main(args: Array<String>) {
  run(::App, *args)
}

```

## run

    mvn jooby:run

## tests

    mvn clean package

## help

* Read the [module documentation](http://jooby.org/doc/lang-kotlin)
* Join the [channel](https://gitter.im/jooby-project/jooby)
* Join the [group](https://groups.google.com/forum/#!forum/jooby-project)
