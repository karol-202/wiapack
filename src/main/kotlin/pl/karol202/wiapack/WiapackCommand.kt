package pl.karol202.wiapack

import com.github.ajalt.clikt.completion.CompletionCandidates
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import java.io.File

class WiapackCommand : CliktCommand()
{
    val rootFile: File by argument("FILE", help = "Path to base HTML file to start with",
                                   completionCandidates = CompletionCandidates.Path).file(mustExist = true,
                                                                                          canBeDir = false,
                                                                                          mustBeReadable = true)
    val outputFile: File? by option("-o", "--output",
                                    help = "Path to file to write the output to").file(canBeDir = false)

    override fun run()
    {
        println("rootFile: $rootFile")
        println("outputFile: $outputFile")
    }
}
