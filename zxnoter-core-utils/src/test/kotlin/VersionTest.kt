import org.semver4j.Semver

fun main() {
    var semver = Semver.parse("1.0.0")!!
    var semver2 = Semver.parse("1.1.0")!!
    println(semver2 > semver)
    println(semver.version)
    println(semver.minor)
}