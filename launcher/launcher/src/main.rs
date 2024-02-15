use std::fs;
use std::path;
use std::path::Path;
use std::process::Command;
use std::env;

fn main() {
    println!("Hello, world!");
    if let Ok(entries) = fs::read_dir(Path::new("./")) {
        for entry in entries {
            if let Ok(entry) = entry {
                println!("{}", entry.file_name().to_string_lossy());
            }
        }
    }

    // 获取命令行参数
    let args: Vec<String> = env::args().collect();

    // 打印所有参数
    println!("Program name: {}", args[0]);
    for (i, arg) in args.iter().enumerate().skip(1) {
        println!("Argument {}: {}", i, arg);
    }


}
