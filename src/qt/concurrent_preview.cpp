//
// Created by zedoC on 2024/12/26.
//

#include "concurrent_preview.h"



/// 读取文件
/// @filePath 文件路径
/// @str 文件字符串结果
int readFile(const std::filesystem::path *filePath, std::string &str) {
    std::ifstream file(*filePath);
    if (!file.is_open()) {
        return 0;
    }
    str = std::string((std::istreambuf_iterator<char>(file)),
                      std::istreambuf_iterator<char>());
    return 1;
}

ConcurrentPreview::ConcurrentPreview(QApplication *app, std::filesystem::path *path) : app(app), path(path) {
    if (!app)
        throw std::runtime_error("sb");
}

void ConcurrentPreview::Start() {
    if (watching)
        return;
    watching = true;
    thread = std::thread(&ConcurrentPreview::watch, this);
    thread.detach();
}

void ConcurrentPreview::Stop() {
    if (!watching)
        return;
    watching = false;
    thread.join();
}

ConcurrentPreview::~ConcurrentPreview() {
    Stop();
}

void ConcurrentPreview::watch() {
    while (watching) {
        std::this_thread::sleep_for(std::chrono::milliseconds(interval));
        if (!path)
            continue;

        auto currentModifiedTime = std::filesystem::last_write_time(*path).time_since_epoch().count();
        if (currentModifiedTime != modifiedTime) {
            modifiedTime = currentModifiedTime;

            std::string content;
            if (readFile(path, content)) {
                auto qs = QString::fromStdString(content);
                app->setStyleSheet(qs);
            }
        }
    }
}

