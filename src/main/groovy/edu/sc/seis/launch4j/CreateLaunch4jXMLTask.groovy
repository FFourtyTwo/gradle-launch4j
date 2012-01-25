package edu.sc.seis.launch4j

import java.io.File;

import org.gradle.api.internal.ConventionTask

import groovy.xml.MarkupBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Project;
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CreateLaunch4jXMLTask extends DefaultTask {

    static final Logger LOGGER = LoggerFactory.getLogger(CreateLaunch4jXMLTask)

    

    @OutputFile
    File getXmlOutFile() {
        return project.launch4j.getXmlOutFileForProject(project)
    }

    @TaskAction
    def void writeXmlConfig() {
        Launch4jPluginExtension configuration = project.launch4j
        def classpath = project.configurations.runtime.collect { "lib/${it.name}" }
        def file = getXmlOutFile()
        file.parentFile.mkdirs()
        def writer = new BufferedWriter(new FileWriter(file))
        def xml = new MarkupBuilder(writer)
        xml.launch4jConfig() {
            dontWrapJar(configuration.dontWrapJar)
            headerType(configuration.headerType)
            jar(configuration.jar)
            outfile(configuration.outfile)
            errTitle(configuration.errTitle)
            cmdLine(configuration.cmdLine)
            chdir(configuration.chdir)
            priority(configuration.priority)
            downloadUrl(configuration.downloadUrl)
            supportUrl(configuration.supportUrl)
            customProcName(configuration.customProcName)
            stayAlive(configuration.stayAlive)
            manifest(configuration.manifest)
            icon(configuration.icon)
            classPath() {
                mainClass(configuration.mainClassName)
                classpath.each() { val -> cp(val ) }
            }
            versionInfo() {
                fileVersion(configuration.version )
                txtFileVersion(configuration.version )
                fileDescription() { }
                copyright() { }
                productVersion(configuration.version )
                txtProductVersion(configuration.version )
                productName(project.name )
                internalName(project.name )
                originalFilename(configuration.outfile )
            }
            jre() {
                path() {}
                minVersion('1.5.0' )
            }
        }
        writer.close()
    }

}