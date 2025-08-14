import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.math.max

/**
 * 修改库的包名
 */

// TODO:新的包名
val newPakcage = "com.omega"
// 被替换的包名
val needToReplacePackage = "com.nova"
val rootDir = "./"
val ignoreDirs = listOf("./demo", "./.idea", "./build", "./.gradle", "./.git")
val checkFileSufix = listOf(".kt", ".java", ".xml")

val rootFile = File(rootDir)
val ignoreFiles = ignoreDirs.map {
    File(rootFile, it).canonicalFile
}

// 修改文件内容
rootFile.scanFile({ f ->
    if (ignoreFiles.any { it.canonicalPath == f.canonicalPath }) {
        emptyList()
    } else {
        f.listFiles()?.toList() ?: emptyList()
    }
}) { f ->
    if (f.isTargetFile()) {
        f.replacePackage()
    }
}

// 移动文件到新的文件目录
val needRemoveDirName = needToReplacePackage.split(".")
val oldPakcageDeep = needRemoveDirName.size
val newDirDeep = newPakcage.split(".").size
var removeIndex = 0
val newDirSubPath = newPakcage.replace('.', '/')
val oldDirToNewDirMap = mutableMapOf<String, String>()
rootFile.scanFile({ f ->
    if (needRemoveDirName[removeIndex] == f.name) {
        removeIndex ++
        if (removeIndex == oldPakcageDeep) {
            removeIndex = 0
            var count = 0
            var parent: File = f
            do {
                parent = parent.parentFile
            } while (++ count < oldPakcageDeep)
            val newFile = File(parent, newDirSubPath)
            oldDirToNewDirMap[f.canonicalPath] = newFile.canonicalPath
        }
    } else {
        removeIndex = 0
    }
    if (ignoreFiles.any { it.canonicalPath == f.canonicalPath }) {
        emptyList()
    } else {
        f.listFiles()?.toList() ?: emptyList()
    }
}) { }

for ((oldPath, newPath) in oldDirToNewDirMap) {
    moveFiles(fromDir = oldPath, toDir = newPath)
}

fun File.scanFile(
    handleDir: (f: File) -> List<File> = { f -> if (f.isFile) { emptyList() } else { f.listFiles()?.toList() ?: emptyList() } },
    handleFile: (f: File) -> Unit
) {
    if (this.isFile) {
        handleFile(this)
    } else {
        val children = handleDir(this)
        for (c in children) {
            c.scanFile(handleDir, handleFile)
        }
    }
}

fun File.isTargetFile(): Boolean {
    return checkFileSufix.any { this.name.endsWith(it) }
}

fun File.replacePackage() {
    println("HandleFile: ${this.canonicalPath}")
    println("===============================")
    val reader = FileReader(this)
    val lines = reader.use {
        it.readLines()
    }

    val writer = FileWriter(this)
    writer.use {
        for (l in lines) {
            if (l.contains(needToReplacePackage)) {
                val newLine = l.replace(needToReplacePackage, newPakcage)
                println("oldLine -> $l, newLine -> $newLine")
                writer.appendLine(newLine)
            } else {
                writer.appendLine(l)
            }
        }
    }
    println("===============================")
    println("")
}

fun moveFiles(fromDir: String, toDir: String) {
    val fromDirFile = File(fromDir)
    val needToMoveFiles = fromDirFile.listFiles() ?: emptyArray()
    val toDirFile = File(toDir)
    if (!toDirFile.exists()) {
        toDirFile.mkdirs()
    }
    for (f in needToMoveFiles) {
        if (f.isDirectory) {
            val newDir = File(toDirFile, f.name)
            if (!newDir.exists()) {
                newDir.mkdirs()
            }
            Files.move(f.toPath(), newDir.toPath(), StandardCopyOption.REPLACE_EXISTING)
        } else {
            Files.move(f.toPath(), toDirFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        }
    }
    var checkDir = fromDirFile
    repeat(max(newDirDeep, oldPakcageDeep)) {
        for (child in (checkDir.listFiles() ?: emptyArray())) {
            if (child.isDirectory && child.listFiles().let { it == null || it.size == 0 }) {
                child.delete()
            }
        }
        checkDir = checkDir.parentFile
    }
}
