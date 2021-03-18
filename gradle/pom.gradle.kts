fun configurePOM(publication: MavenPublication, project: Project) {
    publication.apply {
        pom {
            name.set("${groupId}:${artifactId}")
            url.set("https://github.com/Virelion/buildata")
            description.set(project.description)
            inceptionYear.set("2021")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    id.set("Virelion")
                    name.set("Maciej Ziemba")
                    email.set("m.ziemba95@gmail.com")
                }
            }
            scm {
                val projectPath = "Virelion/buildata"
                connection.set("scm:git:git://github.com/${projectPath}.git")
                developerConnection.set("scm:git:ssh://github.com:${projectPath}.git")
                url.set("https://github.com/${projectPath}/tree/master")
            }
        }
    }
}
extra["configurePOM"] = ::configurePOM