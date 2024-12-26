//
// Created by zedoC on 2024/12/26.
//

#include "concurrent_preview.h"
#include <iostream>
#include <fstream>
#include <thread>


/// 读取文件
/// @filePath 文件路径
/// @str 文件字符串结果
int readFile(std::filesystem::path *filePath, std::string &str) {
    std::ifstream file(*filePath);
    if (!file.is_open()) {
        return 0;
    }
    str = std::string((std::istreambuf_iterator<char>(file)),
                      std::istreambuf_iterator<char>());
    return 1;
}

ConcurrentPreview::ConcurrentPreview(std::filesystem::path *path, QApplication *app) {
    watching = true;
    thread = std::thread(&ConcurrentPreview::watch, this, path, app);
    thread.detach();
}

ConcurrentPreview::ConcurrentPreview(QApplication *app) : ConcurrentPreview(nullptr, app) {

}


void ConcurrentPreview::watch(std::filesystem::path *path, QApplication *app) {
    while (watching) {
        std::this_thread::sleep_for(std::chrono::milliseconds(interval));
        if (!path || !app)
            continue;

        auto currentModifiedTime = std::filesystem::last_write_time(qss_path);

        if (currentModifiedTime != modifiedTime) {
            modifiedTime = currentModifiedTime;

            // 重新加载文件内容
            std::string content;
            if (readFile(&qss_path, content)) {
                // 动态更新（这里模拟一个更新行为）
                std::cout << "QSS File updated!" << std::endl;
                auto qs = QString::fromStdString(content);
                app->setStyleSheet(qs);
            }
        }
    }
}

