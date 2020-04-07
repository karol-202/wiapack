#!/bin/bash
/*usr/bin/kotlin "$0" -- "$@"
exit #*/
// This is very hacky way workaround for not being able to pass arguments to Kotlin script because of kotlinc intercepting them

@file:Repository("https://jcenter.bintray.com")
@file:DependsOn("com.github.ajalt:clikt:2.6.0")
@file:DependsOn("org.jsoup:jsoup:1.13.1")

import com.github.ajalt.clikt.completion.CompletionCandidates
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import java.io.File

class Wiapack : CliktCommand()
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

Wiapack().main(args)
