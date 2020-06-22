package br.com.hrom.testcontainerdemo

import br.com.hrom.testcontainerdemo.book.Book
import br.com.hrom.testcontainerdemo.book.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class App : CommandLineRunner {

    @Autowired
    lateinit var bookRepository: BookRepository

    override fun run(vararg args: String?) {
        bookRepository.save(Book(name = "As Crônicas de Gelo e Fogo", author = "George R. R. Martin"))
        bookRepository.save(Book(name = "Dom Casmurro", author = "Machado de Assis"))
        bookRepository.save(Book(name = "Guia do mochileiro das galáxias", author = "Douglas Adams"))

        println(bookRepository.get("As Crônicas de Gelo e Fogo"))
        println(bookRepository.get("Dom Casmurro"))
        println(bookRepository.get("Memórias Póstumas de Brás Cubas"))
    }
}

fun main(args: Array<String>) {
    runApplication<App>(*args)
}
