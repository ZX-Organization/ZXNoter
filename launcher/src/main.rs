#![windows_subsystem = "windows"]

use std::{env, fs};
use std::os::windows::process::CommandExt;
use std::path::Path;
use std::process::{Command, exit};

struct LauncherInfo {
    jar_path: String,
    required_java_version: String,
}

struct JavaInfo {
    path: String,
    name: String,
    version: String,
}

impl JavaInfo {
    // 添加一个新的关联函数用于创建 JavaInfo 实例
    fn new(path: String, name: String, version: String) -> JavaInfo {
        JavaInfo {
            path,
            name,
            version,
        }
    }
}

fn main() {
    println!("开始查找Java环境...");
    let mut checklist = build_checklist();

    match read_launcher_info("Launcher.ini") {
        Ok(launcher_info) => {
            println!("Jar Path: {}", launcher_info.jar_path);
            println!("Required Java Version: {}", launcher_info.required_java_version);

            let fixed_string = &launcher_info.required_java_version;
            checklist.sort_by(|a, b| {
                if a.contains(fixed_string) && !b.contains(fixed_string) {
                    std::cmp::Ordering::Less // a 包含固定字符串，b 不包含，将 a 移至开头
                } else if !a.contains(fixed_string) && b.contains(fixed_string) {
                    std::cmp::Ordering::Greater // b 包含固定字符串，a 不包含，将 b 移至开头
                } else {
                    a.cmp(b) // 其他情况按照默认比较进行排序
                }
            });


            for list in checklist {

                // 检查 Java 版本是否满足要求
                match check_java_version(&list) {
                    Ok(java_version) => {
                        println!("Detected Java Version: {}", java_version.path);
                        if is_java_version_compatible(&launcher_info.required_java_version, &java_version.version) {
                            println!("Java version is compatible. Starting JAR...");
                            // 启动 JAR 文件
                            match start_jar(&java_version.path, &launcher_info.jar_path) {
                                Ok(_) => {
                                    exit(0);
                                }
                                Err(_) => {}
                            }
                            // Command::new(java_version.path).arg("-jar").arg("zxnoter.jar");
                        } else {
                            println!("Java version is not compatible.");
                        }
                    }
                    Err(err) => eprintln!("Error checking Java version: {}", err),
                }
            }
        }
        Err(err) => eprintln!("Error reading Launcher.ini: {}", err),
    }
}

fn start_jar(java: &str, args: &str) -> Result<String, String> {
    println!("Starting JAR: {}", args);

    let output = Command::new(java)
        .creation_flags(0x08000000)
        .args(args.split_whitespace())
        .output()
        .map_err(|e| format!("Error starting JAR: {}", e))?;

    if output.status.success() {
        Ok("OK!".to_string()) // 假设成功启动后返回 JavaInfo，您可能需要根据实际情况更新这里的信息
    } else {
        Err("Failed to start JAR".to_string())
    }
}

fn read_launcher_info(file_path: &str) -> Result<LauncherInfo, String> {
    let contents = fs::read_to_string(file_path).map_err(|e| format!("Error reading file: {}", e))?;
    let mut jar_path = None;
    let mut required_java_version = None;

    for line in contents.lines() {
        let parts: Vec<&str> = line.trim().splitn(2, '=').collect();
        if parts.len() == 2 {
            match parts[0].trim() {
                "JarPath" => jar_path = Some(parts[1].trim().to_string()),
                "RequiredJavaVersion" => required_java_version = Some(parts[1].trim().to_string()),
                _ => {}
            }
        }
    }

    match (jar_path, required_java_version) {
        (Some(jar_path), Some(required_java_version)) => {
            Ok(LauncherInfo { jar_path, required_java_version })
        }
        _ => Err("Invalid or incomplete Launcher.ini file".to_string()),
    }
}

fn is_java_version_compatible(required_version: &str, detected_version: &str) -> bool {
    required_version == detected_version
}

fn build_checklist() -> Vec<String> {
    let mut checklist = Vec::new();

    // 检查所有驱动器上的 Java 安装目录
    #[cfg(windows)]
    {
        for drive in b'A'..=b'Z' {
            let drive_str = format!("{}:\\", drive as char);
            if fs::metadata(&drive_str).is_ok() {
                let path = format!("{}Program Files\\Java\\", drive_str);
                if let Ok(entries) = fs::read_dir(&path) {
                    for entry in entries.flatten() {
                        if let Some(file_name) = entry.file_name().to_str() {
                            checklist.push(entry.path().to_string_lossy().into_owned());
                        }
                    }
                }
            }
        }
    }


    // 检查 JAVA_HOME 环境变量
    if let Ok(java_home) = env::var("JAVA_HOME") {
        checklist.push(java_home);
    }


    checklist
}

fn check_java_version(java_path: &str) -> Result<JavaInfo, String> {
    let java_exe_path = Path::new(java_path).join("bin").join("java.exe");

    if java_exe_path.exists() {
        let output = Command::new(&java_exe_path)
            .creation_flags(0x08000000)
            .arg("-version")
            .output()
            .map_err(|e| format!("Error executing command: {}", e))?;

        if output.status.success() {
            let stdout = String::from_utf8_lossy(&output.stdout).to_string();
            let stderr = String::from_utf8_lossy(&output.stderr).to_string();

            let version_info = stderr.trim().to_string();
            let version = extract_version(&version_info).unwrap_or_else(|| "Unknown".to_string());
            let name = extract_name(&version_info).unwrap_or_else(|| "Unknown".to_string());

            Ok(JavaInfo::new(java_exe_path.to_string_lossy().into_owned(), format!("{} {}", version, name), version))
        } else {
            Err("Failed to execute command".to_string())
        }
    } else {
        Err(format!("Java executable not found at {}", java_exe_path.display()))
    }
}

fn extract_version(version_info: &str) -> Option<String> {
    let start_index = version_info.find('"')?;
    let end_index = version_info[start_index + 1..].find('"')? + start_index + 1;
    Some(version_info[start_index + 1..end_index].to_string())
}


fn extract_name(version_info: &str) -> Option<String> {
    let start_index = version_info.find("\" ")? + 2;
    let end_index = version_info[start_index..].find('\n')? + start_index - 1;
    Some(version_info[start_index..end_index].to_string())
}
